package potel.account.controller;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import potel.account.service.AccountService;
import potel.account.service.impl.AccountServiceImpl;
import potel.account.vo.Member;

@WebServlet("/member/reset")
public class RestPasswordController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AccountService accountService;

	@Override
	public void init() throws ServletException {
		try {
			accountService = new AccountServiceImpl();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		String email = req.getParameter("EMAIL");
		String cellphone = req.getParameter("CELLPHONE");
		System.out.printf(email,cellphone);
		boolean result = accountService.checkEmailAndCellphone(email, cellphone);
		JsonObject respBody = new JsonObject();
		respBody.addProperty("success", result);
		respBody.addProperty("message", result ? "正確" : "不正確");
		resp.getWriter().write(respBody.toString());
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		System.out.printf("doPut");
		Gson gson = new Gson();
		Member member = gson.fromJson(req.getReader(), Member.class);
		

		String newPassword = member.getPasswd();
		String email = member.getEmail();
		System.out.println("email: "+email);
		System.out.println("newPassword: "+newPassword);

		boolean result = accountService.updatepw(newPassword, email);
		JsonObject respBody = new JsonObject();
		respBody.addProperty("success", result);
		respBody.addProperty("message", result ? "成功" : "失敗");
		resp.getWriter().write(respBody.toString());
		
	}
}
