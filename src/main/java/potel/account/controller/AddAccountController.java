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

@WebServlet("/member/add")
public class AddAccountController extends HttpServlet {
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
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		Gson gson = new Gson();
		Member newMember = gson.fromJson(req.getReader(), Member.class);
		boolean isAdded = accountService.addMember(newMember);

		JsonObject respBody = new JsonObject();
		respBody.addProperty("message", isAdded ? "Account added successfully" : "Failed to add Account");
		resp.getWriter().write(respBody.toString());
	}
}





///////////////////////////////////////
