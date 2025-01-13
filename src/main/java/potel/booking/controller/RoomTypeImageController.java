package potel.booking.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import potel.booking.dao.Impl.BookingDaoImpl;



@WebServlet("/booking/image")
public class RoomTypeImageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String imageid = req.getParameter("imageid");
        System.out.println("[" + new Date() + "] imageid=" + imageid);
        
        BookingDaoImpl bookingDao = new BookingDaoImpl(); // 實例化 DAO
        
        try {
            int id = Integer.parseInt(imageid); // 確保 imageId 是整數
            byte[] imagedata = bookingDao.findImageDataById(id); // 獲取圖片數據
            
            if (imagedata != null) {
                resp.setContentType("image/jpg");
                resp.getOutputStream().write(imagedata);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found.");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid image ID.");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
        }
    }
}
