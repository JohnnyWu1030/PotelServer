package potel.booking.vo;

import lombok.Data;

@Data
public class Response {
	private int rc; //return code
    private String rm;
    private int orderid;

}
