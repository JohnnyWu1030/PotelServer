package potel.forum.vo;

import lombok.Data;

@Data
public class Forum {
    // 資料庫中的欄位
    private int postId; // POSTID
    private int memberId; // MEMBERID
    private String title; // TITLE
    private String content; // CONTENT
    private String createDate; // CREATEDATE
    private String modifyDate; // MODIFYDATE
    private Integer imageId; // POSTIMAGEID (改為 Integer，允許為 null)

}
