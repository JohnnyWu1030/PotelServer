package potel.myorders.service;

import java.text.ParseException;

import javax.naming.NamingException;

import potel.myorders.vo.PrdOrder;
import potel.myorders.vo.ResponseObject;

public interface ProductOrderService {
	public ResponseObject queryPrdOrders(String memberid, String orderstate, String datestart, String dateend) throws NamingException, ParseException;
	public ResponseObject queryPrdOrder(String prdorderid) throws NamingException;
	public ResponseObject updatePrdOrder(String op, PrdOrder prdorder) throws NamingException;
}
