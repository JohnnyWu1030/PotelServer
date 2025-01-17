package potel.petsfile.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import potel.forum.service.ForumService;
import potel.forum.service.impl.ForumServiceImpl;
import potel.petsfile.service.PetsFileService;
import potel.petsfile.service.impl.PetsFileServiceImpl;

@WebServlet("/PetsFile/AddDog")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB 緩存大小
maxFileSize = 16 * 1024 * 1024, // 單個檔案最大 16MB
maxRequestSize = 16 * 1024 * 1024 // 整個請求大小最大 16MB
)
public class AddDogController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PetsFileService petsfileService;
	private ForumService forumService;


    // 在初始化时创建 PetsFileService 实例
    @Override
    public void init() throws ServletException {
        petsfileService = new PetsFileServiceImpl(); // 初始化 PetsFileService
		forumService = new ForumServiceImpl(); // 初始化 ForumService
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应的内容类型为 JSON
        resp.setCharacterEncoding("UTF-8");

        // 获取 Dog 对象的各个字段
        String dogOwner = req.getParameter("dogOwner");
        String dogName =req.getParameter("dogName");
        String dogBreed =req.getParameter("dogBreed");
        String dogGender = req.getParameter("dogGender");
		Part imagePart = req.getPart("image");
        
        System.out.println("part:"+imagePart.getSize());

		InputStream imageStream = null;
		int imageId = 0;
		if (imagePart != null && imagePart.getSize() > 0) {
			imageStream = imagePart.getInputStream();
			imageId = forumService.saveImageToDatabase(imageStream);
		}
        
        // 调用 Service 层来处理添加 Dog
        boolean isAdded = petsfileService.addDog(dogOwner, dogName, dogBreed, dogGender, imageId);

        // 构建响应
        PrintWriter out = resp.getWriter();
        if (isAdded) {
            resp.setStatus(HttpServletResponse.SC_OK); // 响应成功状态码
            out.write("{\"message\": \"Dog added successfully\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 失败状态码
            out.write("{\"message\": \"Failed to add Dog\"}");
        }
    }
}
