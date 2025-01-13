package potel.forum.vo;

import lombok.Data;
@Data
public class Comment {
    private int commentId; // COMMENTID
    private int postId;    // POSTID
    private int memberId;  // MEMBERID
    private String content; // CONTENT
    private String createDate; // CREATEDATE
    private String modifyDate; // MODIFYDATE 
}
