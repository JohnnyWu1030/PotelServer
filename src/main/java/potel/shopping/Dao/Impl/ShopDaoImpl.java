package potel.shopping.Dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.sql.DataSource;

import potel.shopping.JDBCConstants;
import potel.shopping.Dao.ShopDao;
import potel.shopping.Vo.OrderRequest;
import potel.shopping.Vo.Product;

public class ShopDaoImpl implements ShopDao {
//	private DataSource ds;

	public ShopDaoImpl() {
		// 建議將數據源配置移到配置類中
//		ds = new HikariDataSource();
//		ds.setJdbcUrl("jdbc:mysql://114.32.203.170:3306/potel");
//		ds.setJdbcUrl("jdbc:mysql://localhost:3306/potel");
//		ds.setUsername("root");
//		ds.setPassword("TIP102_25541859101");
//		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

	}

	@Override
	public List<Product> selectAll(String prdtype) throws NamingException {
		String sql = "select * from products where PRDTYPE=?";
		List<Product> list = new ArrayList<>();
		DataSource ds = JDBCConstants.getDataSource();
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			System.out.println("prdtype=" + prdtype);
			pstmt.setString(1, prdtype);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Product product = new Product();
					product.setPrdId(rs.getInt("PRDID"));
					product.setPrdName(rs.getString("PRDNAME"));
					product.setPrice(rs.getInt("PRICE"));
					product.setImageId(rs.getInt("IMAGEID"));
					product.setPrdDesc(rs.getString("PRDDESC"));
					list.add(product);
				}
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public Product select(int prdId) throws NamingException {
		String sql = "select * from products where prdId=?";
		DataSource ds = JDBCConstants.getDataSource();
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			System.out.println("prdId=" + prdId);
			pstmt.setInt(1, prdId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					Product product = new Product();
					product.setPrdId(rs.getInt("prdId"));
					product.setPrdName(rs.getString("PRDNAME"));
					product.setPrice(rs.getInt("PRICE"));
					product.setImageId(rs.getInt("IMAGEID"));
					product.setPrdDesc(rs.getString("PRDDESC"));
					return product;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public int insertOrder(OrderRequest orderRequest) throws NamingException {
		String sql = "insert into prdorders (MEMBERID, AMOUNT, STATUS) values(?, ?, ?)";
	
		DataSource ds = JDBCConstants.getDataSource();
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {
			
			pstmt.setInt(1, orderRequest.getMemberId());
			pstmt.setInt(2, orderRequest.getAmount());
			pstmt.setString(3, orderRequest.getStatus());
			
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

	
	@Override
	public int insertOrderItem(OrderRequest orderRequest) throws NamingException {
		String sql = "insert into prdorditems (PRDORDERID, PRDID, PRDCOUNT) values(?, ?, ?)";
		
		DataSource ds = JDBCConstants.getDataSource();
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
		
			pstmt.setInt(1, orderRequest.getPrdorderid());
			pstmt.setInt(2, orderRequest.getPrdId());
			pstmt.setInt(3, orderRequest.getPrdCount());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
			
}
