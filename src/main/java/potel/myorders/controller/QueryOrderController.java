package potel.myorders.controller;

import static potel.utils.Defines.sdf;
import static potel.utils.Defines.sdfd;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import potel.myorders.vo.Order;
import potel.utils.JDBCConstants;

@WebServlet(description = "查詢預約訂房訂單明細", urlPatterns = { "/api/order" })
public class QueryOrderController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String orderid = req.getParameter("orderid");
		System.out.println("[" + sdf.format(new Date()) + "] orderid=" + orderid);
		
		DataSource ds = null;
		try {
			ds = JDBCConstants.getDataSource();
		} catch (NamingException e) {
			e.printStackTrace();
			
		}
		
		try (
		     Connection conn = ds.getConnection();
		     PreparedStatement pstmt = conn.prepareStatement("select o.ROOMID,o.EXPDATES,o.EXPDATEE"
		                                                     + ",o.DATES,o.DATEE,o.AMOUNT,o.REFUNDAMOUNT,o.PETID"
		                                                     + ",o.PAYMENTSTATE,o.REFUNDSTATE,o.SCORE,o.COMMENT,o.PAYDATETIME"
		                                                     + ",o.REFUNDDATETIME,o.CREATEDATE"
		                                                     + ",rt.DESCPT,rt.PETTYPE,rt.IMAGEID RTIMG,rt.ROOMTYPEID"
		                                                     + ",p.NICKNAME,p.IMAGEID PIMG"
		                                                     + ",m.NAME"
		                                                     + " from ORDERS o"
		                                                     + " inner join ROOMTYPE rt on o.ROOMTYPEID=rt.ROOMTYPEID"
		                                                     + " inner join IMAGES im on rt.IMAGEID=im.IMAGEID"
		                                                     + " inner join PETS p on o.PETID=p.PETID"
		                                                     + " inner join MEMBERS m on m.MEMBERID=o.MEMBERID"
		                                                     + " where o.ORDERID=?");		) {
			Object temp = null;
			int pindex = 1;
			pstmt.setInt(pindex++, Integer.valueOf(orderid));
			try(ResultSet rs = pstmt.executeQuery();){
				if(rs.next()) {
					JsonObject jorder = new JsonObject();
					jorder.addProperty("orderid", Integer.valueOf(orderid));
					jorder.addProperty("roomid", rs.getInt("ROOMID"));
					jorder.addProperty("expdates", sdfd.format(rs.getDate("EXPDATES")));
					jorder.addProperty("expdatee", sdfd.format(rs.getDate("EXPDATEE")));
					
					jorder.addProperty("dates", (temp=rs.getDate("DATES"))==null?null:sdfd.format(temp));
					jorder.addProperty("datee", (temp=rs.getDate("DATEE"))==null?null:sdfd.format(temp));
					jorder.addProperty("amount", rs.getInt("AMOUNT"));
					jorder.addProperty("refundamount", rs.getInt("REFUNDAMOUNT"));
					jorder.addProperty("petid", rs.getInt("PETID"));
					jorder.addProperty("createdate", (temp=rs.getDate("CREATEDATE"))==null?null:sdfd.format(temp));
					jorder.addProperty("score", rs.getInt("SCORE"));
					jorder.addProperty("comment", rs.getString("COMMENT"));
					
					
					JsonObject jroomtype = new JsonObject();
					jroomtype.addProperty("roomtypeid", rs.getInt("ROOMTYPEID"));
					jroomtype.addProperty("descpt", rs.getString("DESCPT"));
					jroomtype.addProperty("pettype", rs.getString("PETTYPE"));
					jroomtype.addProperty("imageid", rs.getInt("RTIMG"));
					jorder.add("roomtype", jroomtype);
					
					JsonObject jpet = new JsonObject();
					jpet.addProperty("nickname", rs.getString("NICKNAME"));
					jpet.addProperty("imageid", rs.getInt("PIMG"));
					jorder.add("pet", jpet);
					
					JsonObject jmember = new JsonObject();
					jmember.addProperty("name", rs.getString("NAME"));
					jorder.add("member", jmember);

					Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss")
										.create();
					resp.getWriter().write(gson.toJson(jorder));
				}else {
					System.out.println("沒有資料");
					resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String op = req.getParameter("op");
		
		Gson gson = new GsonBuilder()
							.setDateFormat("yyyy/MM/dd HH:mm:ss")
							.create();
		Order order = gson.fromJson(req.getReader(), Order.class); // 要轉出的物件型態
		DataSource ds = null;
		try {
			ds = JDBCConstants.getDataSource();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if("cancel".equalsIgnoreCase(op)) {
			try (Connection conn = ds.getConnection();
			     PreparedStatement pstmt = conn.prepareStatement("update ORDERS set ORDERSTATE=? where ORDERID=?");) {
				int pind = 1;
				pstmt.setString(pind++, order.getOrderstate());
				pstmt.setInt(pind++, order.getOrderid());
				int effect = pstmt.executeUpdate();
				System.out.println("op=" + op + ", orderid=" + order.getOrderid() + ", effect=" + effect);
				
				JsonObject jres = new JsonObject();
				jres.addProperty("respcode", 0);
				jres.addProperty("respmsg", "取消訂單成功(" + effect + ")");
				resp.getWriter().write(gson.toJson(jres));
			} catch (SQLException e) {
				e.printStackTrace();
				JsonObject jres = new JsonObject();
				jres.addProperty("respcode", 1);
				jres.addProperty("respmsg", "取消訂單失敗(" + e + ")");
				resp.getWriter().write(gson.toJson(jres));
			}
		}else if("score".equalsIgnoreCase(op)) {
			try (Connection conn = ds.getConnection();
			     PreparedStatement pstmt = conn.prepareStatement("update ORDERS set SCORE=?,COMMENT=? where ORDERID=?");) {
				int pind = 1;
				pstmt.setInt(pind++, order.getScore());
				pstmt.setString(pind++, order.getComment());
				pstmt.setInt(pind++, order.getOrderid());
				int effect = pstmt.executeUpdate();
				System.out.println("op=" + op + ", orderid=" + order.getOrderid() + ", effect=" + effect);
			
				JsonObject jres = new JsonObject();
				jres.addProperty("respcode", 0);
				jres.addProperty("respmsg", "訂單評分成功(" + effect + ")");
				resp.getWriter().write(gson.toJson(jres));
			} catch (SQLException e) {
				e.printStackTrace();
				
				JsonObject jres = new JsonObject();
				jres.addProperty("respcode", 1);
				jres.addProperty("respmsg", "訂單評分失敗(" + e + ")");
				resp.getWriter().write(gson.toJson(jres));
			}
		}else {
			// TODO: 目前會送來都是一定有確定的動作, 可先不處理
		}
	}
}
