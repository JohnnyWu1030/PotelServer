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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import potel.myorders.vo.ResponseObject;
import potel.utils.JDBCConstants;

@WebServlet(description = "查詢訂房訂單列表", urlPatterns = { "/api/orders" })
public class QueryOrdersController extends HttpServlet {
	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String memberid = req.getParameter("memberid");
		String orderstate = req.getParameter("orderstate");
		String datestart = req.getParameter("datestart");
		String dateend = req.getParameter("dateend");
		System.out.println("[" + sdf.format(new Date()) + "] memberid=" + memberid + ", orderstate=" + orderstate);
		
		
		ResponseObject ro = new ResponseObject();
		DataSource ds = null;
		try {
			ds = JDBCConstants.getDataSource();
		} catch (NamingException e) {
			e.printStackTrace();
			ro.setRespcode(1);
			ro.setRespmsg("資料源錯誤");
		}
		
		StringBuilder sql = new StringBuilder("select o.ORDERID,o.ROOMID,o.EXPDATES,o.EXPDATEE"
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
                                   + " where o.MEMBERID=? and o.ORDERSTATE=?");
		if(datestart!=null && !datestart.isEmpty()) {
			sql.append(" and o.CREATEDATE>=?");
		}
		if(dateend!=null && !dateend.isEmpty()) {
			sql.append(" and o.CREATEDATE<=?");
		}
		
		try (
		     Connection conn = ds.getConnection();
		     PreparedStatement pstmt = conn.prepareStatement(sql.toString());) {
			Object temp = null;
			int pindex = 1;
			pstmt.setInt(pindex++, Integer.valueOf(memberid));
			pstmt.setString(pindex++, String.valueOf(orderstate));
			if(datestart!=null && !datestart.isEmpty()) {
				pstmt.setString(pindex++, datestart + "T00:00:00");
				
			}
			if(dateend!=null && !dateend.isEmpty()) {
				pstmt.setString(pindex++, dateend + "T23:59:59");
			}
			
			
			try(ResultSet rs = pstmt.executeQuery();){
				JsonArray jorders = new JsonArray();
				while(rs.next()) {
					JsonObject jorder = new JsonObject();
					jorder.addProperty("orderid", rs.getInt("ORDERID"));
					jorder.addProperty("roomid", rs.getInt("ROOMID"));
					jorder.addProperty("expdates", sdfd.format(rs.getDate("EXPDATES")));
					jorder.addProperty("expdatee", sdfd.format(rs.getDate("EXPDATEE")));
					
					jorder.addProperty("dates", (temp=rs.getDate("DATES"))==null?null:sdfd.format(temp));
					jorder.addProperty("datee", (temp=rs.getDate("DATEE"))==null?null:sdfd.format(temp));
					jorder.addProperty("amount", rs.getInt("AMOUNT"));
					jorder.addProperty("refundamount", rs.getInt("REFUNDAMOUNT"));
					jorder.addProperty("petid", rs.getInt("PETID"));
					jorder.addProperty("createdate", sdfd.format(rs.getDate("CREATEDATE")));
					
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

					
					jorders.add(jorder);
				}
				Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss")
				                             .create();
				resp.getWriter().write(gson.toJson(jorders));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}