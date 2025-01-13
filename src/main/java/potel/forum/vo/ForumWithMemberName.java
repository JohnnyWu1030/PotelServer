package potel.forum.vo;

import lombok.Data;

@Data
public class ForumWithMemberName {
	    // 資料庫中的欄位
	    private int postId; 
	    private int memberId; 
	    private String title; 
	    private String content; 
	    private String createDate; 
	    private String modifyDate; 
	    private Integer imageId; 
	    private String memberName;
}
