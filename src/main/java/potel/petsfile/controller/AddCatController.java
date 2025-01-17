package potel.petsfile.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import potel.petsfile.service.PetsFileService;
import potel.petsfile.service.impl.PetsFileServiceImpl;
import potel.petsfile.vo.Cat;

@WebServlet("/PetsFile/AddCat")
public class AddCatController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PetsFileService petsfileService;

	// 在初始化時創建 ForumService 實例
	@Override
	public void init() throws ServletException {
		petsfileService = new PetsFileServiceImpl(); // 初始化 ForumService
	}
	
	 @Override
	    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        // 設定回應的內容類型為 JSON
	        resp.setContentType("application/json");
	        resp.setCharacterEncoding("UTF-8");

	        
	        // 解析 JSON 請求內容
	        BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
	        StringBuilder jsonRequest = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            jsonRequest.append(line);
	        }

	        // 使用 Gson 解析 JSON 請求
	        Gson gson = new Gson();
	        Cat newCat = gson.fromJson(jsonRequest.toString(), Cat.class);  // 反序列化 JSON 為 Comment 對象

	        // 获取 Dog 对象的各个字段
	        String catOwner = newCat.getCatOwner();
	        String catName = newCat.getCatName();
	        String catBreed = newCat.getCatBreed();
	        String catGender = newCat.getCatGender();
	        int catImages = newCat.getCatImages();  // 可为 0 或其他有效值

	        // 调用 Service 层来处理添加 Dog
	        boolean isAdded = petsfileService.addCat(catOwner, catName, catBreed, catGender, catImages);

	        // 構建回應
	        PrintWriter out = resp.getWriter();
	        if (isAdded) {
	            resp.setStatus(HttpServletResponse.SC_OK); // 回應成功狀態碼
	            out.write("{\"message\": \"Cat added successfully\"}");
	        } else {
	            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 失敗狀態碼
	            out.write("{\"message\": \"Failed to add Cat\"}");
	        }
	    }
}