package potel.myorders.vo;

import lombok.Data;

@Data
public class Product {
	private int prdid;
	private String prdname;
	private int price;
	private int imageid;
	private String prddesc;
	private String status;
}
