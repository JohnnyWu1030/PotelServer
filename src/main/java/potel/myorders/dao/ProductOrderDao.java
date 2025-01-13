package potel.myorders.dao;

import java.text.ParseException;

import javax.naming.NamingException;

import potel.myorders.vo.PrdOrder;
import potel.myorders.vo.ResponseObject;

public interface ProductOrderDao {
	public ResponseObject queryPrdOrders(String memberid, String status, String datestart, String dateend) throws NamingException, ParseException;
	public ResponseObject queryPrdOrder(String prdorderid) throws NamingException;
	public ResponseObject updatePrdOrder(PrdOrder prdorder) throws NamingException;
	
}
