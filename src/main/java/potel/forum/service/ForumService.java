package potel.forum.service;

import java.io.InputStream;
import java.util.List;
import potel.forum.vo.Comment;
import potel.forum.vo.CommentWithMemberName;
import potel.forum.vo.ForumWithMemberName;
import potel.forum.vo.Like;

public interface ForumService {
	
	List<Like> getLike();
	
	List<ForumWithMemberName> getForum();

	List<CommentWithMemberName> getComment();

	int saveImageToDatabase(InputStream imageStream);

	boolean deletePost(int postId);
	
	boolean deleteComment(int commentId);
	
	boolean addComment(Comment newComment);
	
	boolean likePost(int postId, int memberId);

	boolean unlikePost(int postId, int memberId);
	
	void updatPostWithImage(int postId, String title, String content, Integer imageId);
	
	void addPost(int memberId, String title, String content, int imageId);

	void updatPostWithoutImage(int postId, String title, String content);

	void updatPost(int postId, String title, String content);

	void updateComment(int commentId, String content);

}
