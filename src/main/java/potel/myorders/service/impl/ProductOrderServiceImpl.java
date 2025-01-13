package potel.myorders.service.impl;

import java.text.ParseException;

import javax.naming.NamingException;

import potel.myorders.PrdOrderStatusEnum;
import potel.myorders.dao.impl.ProductOrderDaoImpl;
import potel.myorders.service.ProductOrderService;
import potel.myorders.vo.PrdOrder;
import potel.myorders.vo.ResponseObject;

public class ProductOrderServiceImpl implements ProductOrderService{
	private ProductOrderDaoImpl podi;

	public ProductOrderServiceImpl() {
		podi = new ProductOrderDaoImpl();
	}

	@Override
	public ResponseObject queryPrdOrders(String memberid, String orderstate, String datestart, String dateend) throws NamingException, ParseException {
		ResponseObject ro = new ResponseObject();
		if(memberid==null || !memberid.matches("^[0-9]+$")) {
			ro.setRespcode(1);
			ro.setRespmsg("會員編號格式錯誤!");
			return ro;
		}
		
		return podi.queryPrdOrders(memberid, orderstate, datestart, dateend);
	}

	@Override
	public ResponseObject queryPrdOrder(String prdorderid) throws NamingException {
		ResponseObject ro = new ResponseObject();
		if(prdorderid==null || !prdorderid.matches("^[0-9]+$")) {
			ro.setRespcode(1);
			ro.setRespmsg("購物訂單編號格式錯誤!");
			return ro;
		}
		
		return new ProductOrderDaoImpl().queryPrdOrder(prdorderid);
	}

	@Override
	public ResponseObject updatePrdOrder(String op, PrdOrder prdorder) throws NamingException {
		ResponseObject ro = new ResponseObject();
		if(op==null) {
			ro.setRespcode(1);
			ro.setRespmsg("未指定動作!");
			return ro;
		}else if(prdorder==null) {
			ro.setRespcode(1);
			ro.setRespmsg("購物訂單資料為空!");
			return ro;
		}
		
		if("cancel".equals(op)) {
			prdorder.setStatus(PrdOrderStatusEnum.Canceled.getStatus());
			ro = podi.updatePrdOrder(prdorder);
		}
		return ro;
	}
}
