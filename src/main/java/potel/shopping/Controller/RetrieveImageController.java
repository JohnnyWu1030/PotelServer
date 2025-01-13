package potel.shopping.Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zaxxer.hikari.HikariDataSource;

@WebServlet("/shopping/image")
public class RetrieveImageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private HikariDataSource ds;

	public RetrieveImageController() {
		// 建議將數據源配置移到配置類中
		ds = new HikariDataSource();
		ds.setJdbcUrl("jdbc:mysql://114.32.203.170:3306/potel");
		ds.setUsername("root");
		ds.setPassword("TIP102_25541859101");
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String imageid = req.getParameter("imageid");
		
		try (Connection conn = ds.getConnection();
		     PreparedStatement pstmt = conn.prepareStatement("select IMAGEDATA from IMAGES where IMAGEID=?");) {
			pstmt.setInt(1, Integer.valueOf(imageid));
			try(ResultSet rs = pstmt.executeQuery();){
				if(rs.next()) {
					byte[] imagedata = rs.getBytes("IMAGEDATA");
					resp.setContentType("image/jpg");
					resp.getOutputStream().write(imagedata);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
