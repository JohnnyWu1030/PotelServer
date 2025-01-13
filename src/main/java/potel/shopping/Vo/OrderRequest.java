package potel.shopping.Vo;

import lombok.Data;

@Data
public class OrderRequest {
	private int prdId;
	private int prdCount;
	private int prdorderid;
	private int memberId;
	private int amount;
	private String status;
}
