package potel.booking.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zaxxer.hikari.HikariDataSource;

import potel.booking.dao.BookingDao;
import potel.booking.vo.Order;
import potel.booking.vo.RoomType;

public class BookingDaoImpl implements BookingDao{
	private HikariDataSource ds;
	public BookingDaoImpl() {
		// 建議將數據源配置移到配置類中
		ds = new HikariDataSource();
		ds.setJdbcUrl("jdbc:mysql://114.32.203.170:3306/potel");
		ds.setUsername("root");
		ds.setPassword("TIP102_25541859101");
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
	}
	
	@Override
	public List<RoomType> findAllRoomType() {
		String sql = "select * from roomtype";
		List<RoomType> list = new ArrayList<>();
		try (
			//ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/example2");
			Connection conn = ds.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery()
			
			){
			
			while (rs.next()) {
			    RoomType roomtype = new RoomType();
			    
			    roomtype.setRoomtypeid(rs.getInt("ROOMTYPEID"));
			    roomtype.setDescpt(rs.getString("DESCPT"));
			    roomtype.setImageid(rs.getInt("IMAGEID"));
			    roomtype.setPrice(rs.getInt("PRICE"));
			    roomtype.setPettype(rs.getString("PETTYPE").charAt(0));  // 修改部分
			    roomtype.setWeightl(rs.getInt("WEIGHTL"));
			    roomtype.setWeighth(rs.getInt("WEIGHTH"));
			    roomtype.setStatus(rs.getString("STATUS").charAt(0));     // 確認 STATUS 是否為單字符類型
			    roomtype.setCreatedate(rs.getTimestamp("CREATEDATE"));
			    roomtype.setModifydate(rs.getTimestamp("MODIFYDATE"));
			    list.add(roomtype);
			}
//			System.out.println("finish");
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<RoomType> findRoomTypeById(int roomTypeId) {
		
		String sql = "select * from roomtype where ROOMTYPEID = ?"; //根據 roomTypeId 查詢
		List<RoomType> list = new ArrayList<>();
		try (
			Connection conn = ds.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			){
			pstmt.setInt(1, roomTypeId); // 設置查詢參數
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
			    RoomType roomtype = new RoomType();
			    
			    roomtype.setRoomtypeid(rs.getInt("ROOMTYPEID"));
			    roomtype.setDescpt(rs.getString("DESCPT"));
			    roomtype.setImageid(rs.getInt("IMAGEID"));
			    roomtype.setPrice(rs.getInt("PRICE"));
			    roomtype.setPettype(rs.getString("PETTYPE").charAt(0));  // 修改部分
			    roomtype.setWeightl(rs.getInt("WEIGHTL"));
			    roomtype.setWeighth(rs.getInt("WEIGHTH"));
			    roomtype.setStatus(rs.getString("STATUS").charAt(0));     // 確認 STATUS 是否為單字符類型
			    roomtype.setCreatedate(rs.getTimestamp("CREATEDATE"));
			    roomtype.setModifydate(rs.getTimestamp("MODIFYDATE"));
			    list.add(roomtype);
			}
//			System.out.println("selectIDfinish");
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] findImageDataById(int id) {
		String sql = "SELECT IMAGEDATA FROM IMAGES WHERE IMAGEID = ?";
	    byte[] imageData = null;
	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	         
	        pstmt.setInt(1, id); // 設置查詢參數
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            imageData = rs.getBytes("IMAGEDATA"); // 獲取圖片數據
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return imageData;
	}

	@Override
	public int addOrder(Order order){
		String sql = "INSERT INTO orders (MEMBERID, ROOMTYPEID, EXPDATES, EXPDATEE, AMOUNT, PETID, ROOMID)"
				+ " VALUES (?, ?, ?, ?, ?, ?, 1)";
        
//		DataSource ds = JDBCConstants.getDataSource();
        try (Connection conn = ds.getConnection();
    			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
             int pind = 1;
            pstmt.setInt(pind++, order.getMemberid());
            pstmt.setInt(pind++, order.getRoomtypeid());
            pstmt.setString(pind++,(order.getExpdates()));
            pstmt.setString(pind++,(order.getExpdatee()));
            pstmt.setInt(pind++, order.getAmount());
            pstmt.setInt(pind++, order.getPetid());

           

            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
	

}
