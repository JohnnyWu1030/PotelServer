package potel.booking.vo;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Pet {
	private int petid;            // 寵物 ID (主鍵，自動遞增)
    private char pettype;         // 寵物類型 (單一字元)
    private String nickname;      // 寵物暱稱
    private double weight;        // 寵物重量 (小數，最多 5 位數，1 位小數)
    private String breed;         // 寵物品種
    private int imageid;          // 圖片 ID
    private char status;          // 狀態 (單一字元)
    private Timestamp createdate; // 創建時間
    private Timestamp modifydate; // 修改時間
}
