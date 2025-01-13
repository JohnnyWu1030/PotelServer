package potel.forum.vo;

import lombok.Data;

@Data
public class Like {
    private int likeId;
    private int memberId;
    private int postId;
}
