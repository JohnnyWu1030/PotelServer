package potel.shopping.Service.Impl;

import java.util.List;

import javax.naming.NamingException;

import potel.shopping.Dao.ShopDao;
import potel.shopping.Dao.Impl.ShopDaoImpl;
import potel.shopping.Service.ShopService;
import potel.shopping.Vo.OrderRequest;
import potel.shopping.Vo.Product;

public class ShopServiceImpl implements ShopService {

	private ShopDao listDao;

	public ShopServiceImpl() throws NamingException{
		listDao = new ShopDaoImpl();
	}

	@Override
	public List<Product> selectAll(String prdtype) throws NamingException {
		// TODO Auto-generated method stub
		if(prdtype==null) {
			System.out.println("未傳入參數(" + prdtype + ")");
			return null;
		}else if(!prdtype.equalsIgnoreCase("c") && !prdtype.equalsIgnoreCase("d")) {
			System.out.println("傳入參數錯誤(" + prdtype + ")");
			return null;
		}
		return listDao.selectAll(prdtype.toUpperCase());
	}

	
	@Override
	public Product getProduct(int prdId) throws NamingException {
		
		return listDao.select(prdId);
	}

	
	@Override
	public int createOrder(OrderRequest orderRequest) throws NamingException {
		int prdorderid = listDao.insertOrder(orderRequest);
		if (prdorderid <=0) {
			return -1;
		}
		
		orderRequest.setPrdorderid(prdorderid);
		listDao.insertOrderItem(orderRequest);
		return prdorderid;
	}
}