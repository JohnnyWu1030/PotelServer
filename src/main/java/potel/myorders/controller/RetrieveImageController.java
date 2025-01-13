package potel.myorders.controller;

import static potel.utils.Defines.sdf;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import potel.utils.JDBCConstants;

@WebServlet("/api/image")
public class RetrieveImageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String imageid = req.getParameter("imageid");
		System.out.println("[" + sdf.format(new Date()) + "] imageid=" + imageid);
		DataSource ds = null;
		try {
			ds = JDBCConstants.getDataSource();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
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
