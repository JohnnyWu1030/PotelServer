package potel.forum.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import potel.forum.service.ForumService;
import potel.forum.service.impl.ForumServiceImpl;

@WebServlet("/Forum/UnLikeControl")
public class PostUnlikeController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private ForumService forumService;

	// 在初始化時創建 ForumService 實例
	@Override
	public void init() throws ServletException {
		forumService = new ForumServiceImpl(); // 初始化 ForumService
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 String postId = req.getParameter("postId");
		    String memberId = req.getParameter("memberId");

		    // 驗證參數
		    if (postId == null || memberId == null) {
		        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 錯誤
		        return;
		    }

		    try {
		        // 調用 ForumService 的方法進行邏輯處理
		        boolean isUnliked = forumService.unlikePost(Integer.parseInt(postId), Integer.parseInt(memberId));

		        if (isUnliked) {
		            resp.setStatus(HttpServletResponse.SC_OK); // 200 成功
		        } else {
		            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 內部錯誤
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		    }
	}
}
