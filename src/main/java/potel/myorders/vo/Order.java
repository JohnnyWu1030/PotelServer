package potel.myorders.vo;

import lombok.Data;

@Data
public class Order {
	private int orderid;
	private int memberid;
	private String orderstate;
	private int score;
	private String comment;
}
