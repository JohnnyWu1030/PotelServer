package potel.booking.controller;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import potel.booking.service.BookingService;
import potel.booking.service.impl.BookingServiceImpl;
import potel.booking.vo.*;


import potel.booking.vo.Order;

@WebServlet("/booking/addorder") //新增訂單
public class AddOrderController extends HttpServlet{
	private static final long serialVersionUID = 1L;

	private final BookingService bookingService; // 使用 Service
	
	// 構造函數，處理 NamingException
    public AddOrderController() throws NamingException {
        this.bookingService = new BookingServiceImpl(); // 使用 Service
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Response r = new Response();
		Gson gson = new Gson();
        try {
        	
        	Order order = gson.fromJson(request.getReader(),Order.class);
        	System.out.println("order.getExpdates()=" + order.getExpdates() + ", order.getExpdatee()=" + order.getExpdatee());
        	
        	
//            // 從請求中獲取參數，使用小寫變數名稱
//            int memberid = Integer.parseInt(request.getParameter("memberid"));
//            int roomtypeid = Integer.parseInt(request.getParameter("roomtypeid"));
//            int roomid = Integer.parseInt(request.getParameter("roomid"));
//            String expdates = request.getParameter("expdates");
//            String expdatee = request.getParameter("expdatee");
//            String dates = request.getParameter("dates");
//            String datee = request.getParameter("datee");
//            int amount = Integer.parseInt(request.getParameter("amount"));
////            int refundamount = Integer.parseInt(request.getParameter("refundamount"));
//            int petid = Integer.parseInt(request.getParameter("petid"));
////            char orderstate = request.getParameter("orderstate").charAt(0);
////            char paymentstate = request.getParameter("paymentstate").charAt(0);
////            char refundstate = request.getParameter("refundstate").charAt(0);
////            int score = Integer.parseInt(request.getParameter("score"));
////            String comment = request.getParameter("comment");
//            
//            
//
//            // 創建 Order 實例並設置屬性
//            Order order = new Order();
//            order.setMemberid(memberid);
//            order.setRoomtypeid(roomtypeid);
//            order.setRoomid(roomid);
//            order.setExpdates(java.sql.Date.valueOf(expdates));
//            order.setExpdatee(java.sql.Date.valueOf(expdatee));
//            order.setDates(java.sql.Date.valueOf(dates));
//            order.setDatee(java.sql.Date.valueOf(datee));
//            order.setAmount(amount);
////            order.setRefundamount(refundamount);
//            order.setPetid(petid);
////            order.setOrderstate(orderstate);
////            order.setPaymentstate(paymentstate);
////            order.setRefundstate(refundstate);
////            order.setScore(score);
////            order.setComment(comment);

            // 使用 Service 添加訂單
            r.setOrderid(bookingService.createOrder(order));
            
            r.setRc(r.getOrderid());
            r.setRm("Success");

        } catch (Exception e) {
            e.printStackTrace();
            r.setRc(-1);
            r.setRm(e.toString());
        }
        response.getWriter().write(gson.toJson(r));
        
	}

}
