package potel.petsfile.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import potel.petsfile.service.PetsFileService;
import potel.petsfile.service.impl.PetsFileServiceImpl;
import potel.petsfile.vo.Dog;

@WebServlet("/PetsFile/Dogs")
public class GetDogsController extends HttpServlet{
    private static final long serialVersionUID = 1L;

    private PetsFileService petsfileService;

	public void init() throws ServletException {
		petsfileService = new PetsFileServiceImpl();
	}
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 設定回應的內容類型為 JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // 獲取 Comment 資料
        List<Dog> Dogs;
        try {
        	Dogs = petsfileService.getDog();  
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // 設置錯誤狀態碼
            resp.getWriter().write("{\"error\": \"Unable to get Dogs\"}");
            return;
        }

        // 使用 Gson 來將 List<Forum> 轉換為 JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(Dogs);

        // 返回 JSON 數據
        resp.getWriter().write(jsonResponse);
    }
}
