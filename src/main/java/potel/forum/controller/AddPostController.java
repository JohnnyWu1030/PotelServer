package potel.forum.controller;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import potel.forum.service.ForumService;
import potel.forum.service.impl.ForumServiceImpl;

@WebServlet("/Forum/AddPost")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB 緩存大小
		maxFileSize = 16 * 1024 * 1024, // 單個檔案最大 16MB
		maxRequestSize = 16 * 1024 * 1024 // 整個請求大小最大 16MB
)
public class AddPostController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ForumService forumService;

	// 在初始化時創建 ForumService 實例
	@Override
	public void init() throws ServletException {
		forumService = new ForumServiceImpl(); // 初始化 ForumService
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Add Post");

		req.setCharacterEncoding("UTF-8");

		try {
			String memberId = req.getParameter("memberId");
			String title = req.getParameter("title");
			String content = req.getParameter("content");
			Part imagePart = req.getPart("image");

			InputStream imageStream = null;
			int imageId = 0;
			if (imagePart != null && imagePart.getSize() > 0) {
				imageStream = imagePart.getInputStream();
				imageId = forumService.saveImageToDatabase(imageStream);
			}
			forumService.addPost(Integer.parseInt(memberId), title, content, imageId);
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write("Post added ok");
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("Error: " + e.getMessage());
		}
	}
}
