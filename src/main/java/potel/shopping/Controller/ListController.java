package potel.shopping.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import potel.shopping.Service.Impl.ShopServiceImpl;
import potel.shopping.Vo.Product;


@WebServlet("/shopping/List")
public class ListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("doGet");
        
		String prdtype = req.getParameter("prdtype");
		System.out.println("ListController prdtype=" + prdtype);
		try {
        	 ShopServiceImpl service = new ShopServiceImpl();
             List<Product> products = service.selectAll(prdtype);

             Gson gson = new Gson();
             
            String json = gson.toJson(products);
             
            resp.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
         
        }
    }
}