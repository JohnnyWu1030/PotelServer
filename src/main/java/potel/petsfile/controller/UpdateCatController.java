package potel.petsfile.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import potel.petsfile.service.PetsFileService;
import potel.petsfile.service.impl.PetsFileServiceImpl;
import potel.petsfile.vo.Cat;

@WebServlet("/PetsFile/updateCat")
public class UpdateCatController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PetsFileService petsfileService;

    public void init() throws ServletException {
        petsfileService = new PetsFileServiceImpl();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            // 讀取請求中的 JSON
            BufferedReader reader = req.getReader();
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // 解析 JSON 請求
            Gson gson = new Gson();
            Cat cat = gson.fromJson(stringBuilder.toString(), Cat.class);

            // 確保從 JSON 請求中獲取了所有必需的屬性
            if (cat.getCatId() == 0 || cat.getCatOwner() == null || cat.getCatName() == null || cat.getCatBreed() == null || cat.getCatGender() == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Missing required fields.");
                return;
            }

            // 呼叫服務層來更新貓的資料
            petsfileService.updateCat(
                cat.getCatId(), 
                cat.getCatOwner(), 
                cat.getCatName(), 
                cat.getCatBreed(), 
                cat.getCatGender(), 
                cat.getCatImages() // 或者設為 null，如果沒有圖片ID
            );

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Cat updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error: " + e.getMessage());
        }
    }
}
