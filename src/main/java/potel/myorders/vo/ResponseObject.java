package potel.myorders.vo;

import lombok.Data;

@Data
public class ResponseObject {
	private int respcode;
	private String respmsg;
	private Object resobj;
}
