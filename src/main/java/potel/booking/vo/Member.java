package potel.booking.vo;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class Member {
	private int memberid;         // 會員 ID (主鍵，自動遞增)
    private String name;          // 姓名
    private String cellphone;     // 手機號碼
    private String address;       // 地址
    private char gender;          // 性別 (單一字元)
    private Date birthday;        // 生日
    private String email;         // 電子郵件
    private int imageid;          // 圖片 ID
    private char status;          // 狀態 (單一字元)
    private String passwd;        // 密碼
    private Timestamp createdate; // 創建時間
    private Timestamp modifydate; // 修改時間
}
