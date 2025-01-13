package potel.myorders.controller;

import java.io.IOException;
import java.util.Date;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import potel.myorders.service.impl.ProductOrderServiceImpl;
import potel.myorders.vo.PrdOrder;
import potel.myorders.vo.ResponseObject;
import potel.utils.Defines;

@WebServlet("/api/prdorder")
public class QueryProductOrderController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private ProductOrderServiceImpl posi;
	
	public QueryProductOrderController() {
		posi = new ProductOrderServiceImpl();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String prdorderid = req.getParameter("prdordid");
		System.out.println(Defines.sdf.format(new Date()) + " QueryProductOrdersController(" + prdorderid + ")");
		
		ResponseObject ro = null;
		try {
			ro = posi.queryPrdOrder(prdorderid);
		} catch (NamingException e) {
			ro = new ResponseObject();
			ro.setRespcode(2);
			ro.setRespmsg("查詢錯誤");
			e.printStackTrace();
		}

		String wrstr = new Gson().toJson(ro);
		System.out.println(Defines.sdf.format(new Date()) + " QueryProductOrderController==>" + wrstr);
		
		resp.getWriter().write(wrstr);
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String op = req.getParameter("op");
		
		Gson gson = new GsonBuilder()
							.setDateFormat("yyyy-MM-dd HH:mm:ss")
							.create();
		String reqstr = req.getReader().readLine();
		System.out.println(Defines.sdf.format(new Date()) + " QueryProductOrderController, reqstr(" + reqstr + ")");
		
		PrdOrder prdorder = gson.fromJson(reqstr, PrdOrder.class); // 要轉出的物件型態
		
		ResponseObject ro = null;
		
		try {
			ro = posi.updatePrdOrder(op, prdorder);
		} catch (NamingException e) {
			e.printStackTrace();
			ro = new ResponseObject();
			ro.setRespcode(1);
			ro.setRespmsg("處理失敗");
		}
		
		String rjson = gson.toJson(ro);
		System.out.println(Defines.sdf.format(new Date()) + " QueryProductOrderController, reqstr(" + reqstr + ")");
		resp.getWriter().write(rjson);
	}
}
