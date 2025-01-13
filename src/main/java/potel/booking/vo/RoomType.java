package potel.booking.vo;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class RoomType {
	private int roomtypeid;         // 房型 ID (主鍵)
    private String descpt;          // 描述
    private int imageid;            // 圖片 ID
    private String imageurl;		// 圖片 URL
    private int price;              // 價格
    private char pettype;           // 寵物類型 (單一字元)
    private int weightl;            // 最低重量
    private int weighth;            // 最高重量
    private char status;            // 狀態 (單一字元)
    private Timestamp createdate;   // 創建時間
    private Timestamp modifydate;   // 修改時間
}

