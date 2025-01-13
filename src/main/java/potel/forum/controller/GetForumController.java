package potel.forum.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import potel.forum.service.ForumService;
import potel.forum.service.impl.ForumServiceImpl;
import potel.forum.vo.ForumWithMemberName;

@WebServlet("/Forum/Posts")
public class GetForumController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ForumService forumService;

	// 在初始化時創建 ForumService 實例
	@Override
	public void init() throws ServletException {
		forumService = new ForumServiceImpl(); // 初始化 ForumService
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 設定回應的內容類型為 JSON
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		// 獲取 forums 資料
		List<ForumWithMemberName> forums;
		try {
			forums = forumService.getForum(); // 調用 ForumService 的 getForum() 方法來獲取所有論壇貼文資料
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 設置錯誤狀態碼
			resp.getWriter().write("{\"error\": \"Unable to fetch forums\"}");
			return;
		}

		// 使用 Gson 來將 List<Forum> 轉換為 JSON
		Gson gson = new Gson();
		String jsonResponse = gson.toJson(forums);

		// 返回 JSON 數據
		resp.getWriter().write(jsonResponse);
	}
}
