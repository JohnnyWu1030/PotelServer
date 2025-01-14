package potel.account.controller;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import potel.account.service.AccountService;
import potel.account.service.impl.AccountServiceImpl;
import potel.account.vo.Member;

@WebServlet("/member/login")
public class LoginAccountController extends HttpServlet {
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
			String input = req.getParameter("INPUT");
			String passwd = req.getParameter("PASSWD");
			Member result = accountService.login(input,passwd);
			System.out.println("input(" + input + "," + passwd );

			resp.getWriter().write(new Gson().toJson(result, Member.class));
		}

}
