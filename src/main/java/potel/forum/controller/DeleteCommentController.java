package potel.forum.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import potel.forum.service.ForumService;
import potel.forum.service.impl.ForumServiceImpl;

@WebServlet("/Forum/comments/*")
public class DeleteCommentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ForumService forumService;

	public void init() throws ServletException {
		forumService = new ForumServiceImpl();
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || !pathInfo.startsWith("/")) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
			response.getWriter().write("Comment ID is missing");
			return;
		}

		try {
			// 解析文章 ID
			int commentId = Integer.parseInt(pathInfo.substring(1));

			// 呼叫服務層刪除文章
			if (forumService.deleteComment(commentId)) {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
				response.getWriter().write("Post not found");
			}
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
			response.getWriter().write("Invalid post ID");
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
			response.getWriter().write("An error occurred while deleting the post");
		}
	}
}
