package potel.booking.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import potel.booking.service.BookingService;
import potel.booking.service.impl.BookingServiceImpl;
import potel.booking.vo.RoomType;


@WebServlet("/booking/findroomtype") //顯示房型
public class FindRoomTypeController extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    try {
	        String roomTypeIdParam = req.getParameter("roomtypeid"); // 獲取參數
	        
	        BookingService service = new BookingServiceImpl(); // 實例化service
	        
	        List<RoomType> roomTypes; // 定義房型列表
	        
	        if (roomTypeIdParam != null) { // 提供了 roomTypeId 參數
	            int roomTypeId = Integer.parseInt(roomTypeIdParam); // 轉換為整數
	            roomTypes = service.findRoomTypeById(roomTypeId); // 根據 ID 查詢房型
	        } else {
	            roomTypes = service.findRoomType(); // 查詢所有房型
	        }
	        
	        Gson gson = new Gson();
	        String json = gson.toJson(roomTypes);
	        resp.getWriter().write(json);
	    } catch (NumberFormatException e) {
	        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid roomTypeId parameter.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
