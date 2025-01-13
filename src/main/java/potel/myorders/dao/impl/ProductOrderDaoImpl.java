package potel.myorders.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.naming.NamingException;
import javax.sql.DataSource;

import potel.myorders.dao.ProductOrderDao;
import potel.myorders.vo.Member;
import potel.myorders.vo.PrdOrdItem;
import potel.myorders.vo.PrdOrder;
import potel.myorders.vo.Product;
import potel.myorders.vo.ResponseObject;
import potel.utils.Defines;
import potel.utils.JDBCConstants;

public class ProductOrderDaoImpl implements ProductOrderDao {
	@Override
	public ResponseObject queryPrdOrders(String memberid, String status, String datestart, String dateend) throws NamingException, ParseException {
		ResponseObject ro = new ResponseObject();
		DataSource ds = JDBCConstants.getDataSource();
		
		StringBuilder sbsql = new StringBuilder("select po.PRDORDERID,po.AMOUNT,po.STATUS,po.CREATEDATE,po.MODIFYDATE"
	     					+ ",m.MEMBERID,m.NAME,m.CELLPHONE,m.ADDRESS,m.GENDER,m.EMAIL,m.IMAGEID"
	     					+ " from PRDORDERS po"
	     					+ " inner join MEMBERS m on po.MEMBERID=m.MEMBERID"
	     					+ " where po.MEMBERID=?");
		if(!"A".equals(status)) {
			sbsql.append(" and po.STATUS=?");
		}
		if(datestart!=null && !datestart.isEmpty()) {
			sbsql.append(" and po.CREATEDATE>=?");
		}
		if(dateend!=null && !dateend.isEmpty()) {
			sbsql.append(" and po.CREATEDATE<=?");
		}
		System.out.println("sql==>" + sbsql.toString());
		try (
		     Connection conn = ds.getConnection();
		     PreparedStatement pstmt = conn.prepareStatement(sbsql.toString());) {
			int pind = 1;
			pstmt.setInt(pind++, Integer.parseInt(memberid));
			if(!"A".equals(status)) {
				pstmt.setString(pind++, String.valueOf(status));
			}
			if(datestart!=null && !datestart.isEmpty()) {
				pstmt.setString(pind++, datestart + "T00:00:00");
			}
			if(dateend!=null && !dateend.isEmpty()) {
				pstmt.setString(pind++, dateend + "T23:59:59");
			}
			try(ResultSet rs = pstmt.executeQuery();){
				Object temp = null;
				ArrayList<PrdOrder> lspo = new ArrayList<PrdOrder>();
				while(rs.next()) {
					PrdOrder po = new PrdOrder();
					po.setPrdorderid(rs.getInt("PRDORDERID"));
//					po.setMemberid(rs.getInt("MEMBERID"));
					po.setAmount(rs.getInt("AMOUNT"));
					po.setStatus(rs.getString("STATUS").charAt(0));
					po.setCreatedate((temp = rs.getTimestamp("CREATEDATE"))==null?null:Defines.sdf.format(temp));
					po.setModifydate((temp = rs.getTimestamp("MODIFYDATE"))==null?null:Defines.sdf.format(temp));
					
					Member member = new Member();
					member.setMemberid(rs.getInt("MEMBERID"));
					member.setName(rs.getString("NAME"));
					member.setCellphone(rs.getString("CELLPHONE"));
					member.setAddress(rs.getString("ADDRESS"));
					member.setGender(rs.getString("GENDER").charAt(0));
					member.setEmail(rs.getString("ADDRESS"));
					member.setImageid(rs.getInt("IMAGEID"));
					po.setMember(member);
					
					lspo.add(po);
				}
				ro.setRespcode(0);
				ro.setResobj(lspo);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			ro.setRespcode(1);
			ro.setRespmsg("資料庫錯誤");
		}
		return ro;
	}

	@Override
	public ResponseObject queryPrdOrder(String prdorderid) throws NamingException {
		ResponseObject ro = new ResponseObject();
		DataSource ds = JDBCConstants.getDataSource();
		
		try (Connection conn = ds.getConnection();) {
			PrdOrder po = null;
			int pind = 1;

			try(PreparedStatement pstmt = conn.prepareStatement("select po.PRDORDERID,po.AMOUNT,po.STATUS,po.CREATEDATE"
		     					+ ",m.MEMBERID,m.NAME,m.CELLPHONE,m.ADDRESS,m.GENDER,m.EMAIL,m.IMAGEID"
		     					+ " from PRDORDERS po"
		     					+ " inner join MEMBERS m on po.MEMBERID=m.MEMBERID"
		     					+ " where po.PRDORDERID=?");){
				pind = 1;
				pstmt.setInt(pind++, Integer.parseInt(prdorderid));
				try(ResultSet rs = pstmt.executeQuery();){
					Object temp = null;
					if(rs.next()) {
						po = new PrdOrder();
						po.setPrdorderid(rs.getInt("PRDORDERID"));
//						po.setMemberid(rs.getInt("MEMBERID"));
						po.setAmount(rs.getInt("AMOUNT"));
						po.setStatus(rs.getString("STATUS").charAt(0));
						po.setCreatedate((temp = rs.getTimestamp("CREATEDATE"))==null?null:Defines.sdf.format(temp));
//						po.setModifydate((temp = rs.getTimestamp("MODIFYDATE"))==null?null:Defines.sdf.format(temp));
						
						Member member = new Member();
						member.setMemberid(rs.getInt("MEMBERID"));
						member.setName(rs.getString("NAME"));
						member.setCellphone(rs.getString("CELLPHONE"));
						member.setAddress(rs.getString("ADDRESS"));
						member.setGender(rs.getString("GENDER").charAt(0));
						member.setEmail(rs.getString("ADDRESS"));
						member.setImageid(rs.getInt("IMAGEID"));
						po.setMember(member);
					}
				}
			}
			
			if(po!=null) {
				// 有資料, 繼續蒐items和products
				try(PreparedStatement pstmt = conn.prepareStatement("select poi.PRDORDITEMID,poi.PRDCOUNT"
			     					+ ",prd.PRDID,prd.PRDNAME,prd.PRICE,prd.IMAGEID,prd.PRDDESC"
			     					+ " from PRDORDERS po"
			     					+ " inner join PRDORDITEMS poi on poi.PRDORDERID=po.PRDORDERID"
			     					+ " inner join PRODUCTS prd on prd.PRDID=poi.PRDID"
			     					+ " where po.PRDORDERID=?");
									){
					pind = 1;
					pstmt.setInt(pind++, Integer.parseInt(prdorderid));
					try(ResultSet rs = pstmt.executeQuery();){
						po.setPrdorditems(new ArrayList<PrdOrdItem>());
						while(rs.next()) {
							PrdOrdItem poi = new PrdOrdItem();
							poi.setPrdorditemid(rs.getInt("PRDORDITEMID"));
							poi.setPrdcount(rs.getInt("PRDCOUNT"));
							
							Product p = new Product();
							p.setPrdid(rs.getInt("PRDID"));
							p.setPrdname(rs.getString("PRDNAME"));
							p.setPrice(rs.getInt("PRICE"));
							p.setImageid(rs.getInt("IMAGEID"));
							p.setPrddesc(rs.getString("PRDDESC"));
							poi.setProduct(p);
							
							po.getPrdorditems().add(poi);
						}
					}
				}
			}
			
			ro.setRespcode(0);
			ro.setResobj(po);
		} catch (SQLException e) {
			e.printStackTrace();
			ro.setRespcode(e.getErrorCode());
			ro.setRespmsg("資料庫錯誤");
		}
		return ro;
	}

	@Override
	public ResponseObject updatePrdOrder(PrdOrder prdorder) throws NamingException {
		ResponseObject ro = new ResponseObject();
		DataSource ds = JDBCConstants.getDataSource();
		
		try (Connection conn = ds.getConnection();
		     PreparedStatement pstmt = conn.prepareStatement("update PRDORDERS set STATUS=?,MODIFYDATE=CURRENT_TIMESTAMP where PRDORDERID=?");) {
			int pind = 1;

			pstmt.setString(pind++, String.valueOf(prdorder.getStatus()));
			pstmt.setInt(pind++, prdorder.getPrdorderid());
			pstmt.executeUpdate();
			
			ro.setRespcode(0);
			ro.setRespmsg("更新成功");
		} catch (SQLException e) {
			e.printStackTrace();
			ro.setRespcode(e.getErrorCode());
			ro.setRespmsg("更新失敗");
		}
		return ro;
	}
}
