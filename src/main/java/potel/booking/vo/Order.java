package potel.booking.vo;

import lombok.Data;

@Data
public class Order {
//	private int orderid;          // 訂單 ID (主鍵，自動遞增)
    private int memberid;         // 會員 ID
    private int roomtypeid;       // 房型 ID
//    private int roomid;           // 房間 ID
    private String expdates;        // 入住日期
    private String expdatee;        // 退房日期
//    private String dates;           // 開始日期
//    private String datee;           // 結束日期
    private int amount;           // 總金額
//    private int refundamount;     // 退款金額
    private int petid;            // 寵物 ID
//    private char orderstate;      // 訂單狀態 (單一字元)
//    private char paymentstate;    // 付款狀態 (單一字元)
//    private char refundstate;     // 退款狀態 (單一字元)
//    private int score;            // 評分
//    private String comment;       // 評論
//    private Date paydatetime;     // 付款時間
//    private Date refunddatetime;   // 退款時間
//    private Date createdate;      // 創建時間
//    private Date modifydate;
}
