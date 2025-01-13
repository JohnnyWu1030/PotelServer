package potel.forum.vo;

import lombok.Data;

@Data
public class CommentWithMemberName {
	private int commentId;
    private int postId;
    private int memberId;
    private String content;
    private String createDate;
    private String modifyDate;
    private String memberName;
}
