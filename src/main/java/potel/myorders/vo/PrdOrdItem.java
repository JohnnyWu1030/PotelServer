package potel.myorders.vo;

import lombok.Data;

@Data
public class PrdOrdItem {
	private int prdorditemid;
//	private int prdid;
	private int prdcount;
	private Product product;
}
