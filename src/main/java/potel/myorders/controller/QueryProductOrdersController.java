package potel.myorders.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import potel.myorders.service.impl.ProductOrderServiceImpl;
import potel.myorders.vo.ResponseObject;
import potel.utils.Defines;

@WebServlet("/api/prdorders")
public class QueryProductOrdersController extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String memberid = req.getParameter("memberid");
		String orderstate = req.getParameter("orderstate");
		String datestart = req.getParameter("datestart");
		String dateend = req.getParameter("dateend");
		
		System.out.println(Defines.sdf.format(new Date()) + " QueryProductOrdersController(" + memberid + ", " + orderstate + "), datestart=" + datestart + ", dateend=" + dateend);
		
		ResponseObject ro = null;
		try {
			ro = new ProductOrderServiceImpl().queryPrdOrders(memberid, orderstate, datestart, dateend);
		} catch (Exception e) {
			ro = new ResponseObject();
			ro.setRespcode(2);
			ro.setRespmsg("查詢錯誤");
			e.printStackTrace();
		}

		String wrstr = new Gson().toJson(ro);
		System.out.println(Defines.sdf.format(new Date()) + " QueryProductOrdersController==>" + wrstr);
		
		resp.getWriter().write(wrstr);
	}
}
