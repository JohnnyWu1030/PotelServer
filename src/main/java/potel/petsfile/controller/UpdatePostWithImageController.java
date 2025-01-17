package potel.petsfile.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import potel.petsfile.service.PetsFileService;
import potel.petsfile.service.impl.PetsFileServiceImpl;

@WebServlet("/PetsFile/UpdatePostWithImage")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB 緩存大小
		maxFileSize = 16 * 1024 * 1024, // 單個檔案最大 16MB
		maxRequestSize = 16 * 1024 * 1024 // 整個請求大小最大 16MB
)
public class UpdatePostWithImageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PetsFileService petsfileService;

	// 在初始化時創建 ForumService 實例
	@Override
	public void init() throws ServletException {
		petsfileService = new PetsFileServiceImpl(); // 初始化 ForumService
	}
	

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		try {
			String postId = req.getParameter("postId");
			String title = req.getParameter("title");
			String content = req.getParameter("content");
			Part imagePart = req.getPart("image");
			System.out.println("update post , imagePart:"+imagePart);
			if (imagePart==null) {
				petsfileService.updatPostWithoutImage(Integer.parseInt(postId), title, content);
			}else {
				InputStream imageStream = imagePart.getInputStream();
				int imageId = petsfileService.saveImageToDatabase(imageStream);
				petsfileService.updatPostWithImage(Integer.parseInt(postId), title, content, imageId);
			}
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write("Post updateed ok");
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("Error: " + e.getMessage());
		}
	}
}
