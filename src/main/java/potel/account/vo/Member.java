package potel.account.vo;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Member {
	private Integer memberid;
	private String name;
	private String cellphone;
	private String address;
	private Character gender;
	private String birthday;
	private String email;
	private String passwd;
	private Integer imageid;
	private Character status;
	private Timestamp createdate;
	private Timestamp modifydate;
}

