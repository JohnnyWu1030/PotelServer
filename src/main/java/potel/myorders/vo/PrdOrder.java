package potel.myorders.vo;

import java.util.List;

import lombok.Data;

@Data
public class PrdOrder {
	private Integer prdorderid;
	private Integer memberid;
	private Integer amount;
	private Character status;
	private String createdate;
	private String modifydate;
	private Member member;
	private List<PrdOrdItem> prdorditems;
}