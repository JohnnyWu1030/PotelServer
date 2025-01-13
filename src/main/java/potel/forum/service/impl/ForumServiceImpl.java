package potel.forum.service.impl;

import java.io.InputStream;
import java.util.List;
import potel.forum.dao.ForumDao;
import potel.forum.dao.impl.ForumDaoImpl;
import potel.forum.service.ForumService;
import potel.forum.vo.Comment;
import potel.forum.vo.CommentWithMemberName;
import potel.forum.vo.ForumWithMemberName;
import potel.forum.vo.Like;

public class ForumServiceImpl implements ForumService {

	private ForumDao forumDao;

	public ForumServiceImpl() {
		this.forumDao = new ForumDaoImpl();
	}

	@Override
	public List<ForumWithMemberName> getForum() {
		System.out.println("Forum service get forum");
		List<ForumWithMemberName> forums = forumDao.selectAll();
		System.out.println("Retrieved forums: " + forums.size()); // 打印返回的論壇數量
		return forums;
	}

	@Override
	public List<Like> getLike() {
		System.out.println("Forum service get likes");
		List<Like> Likes = forumDao.selectlike();
		System.out.println("Retrieved Likes: " + Likes.size()); // 打印返回的論壇數量
		return Likes;
	}

	@Override
	public List<CommentWithMemberName> getComment() {
		System.out.println("Forum service get Comments");
		List<CommentWithMemberName> Comments = forumDao.selectComment();
		System.out.println("Retrieved Comments: " + Comments.size()); // 打印返回的論壇數量
		return Comments;
	}

	@Override
	public int saveImageToDatabase(InputStream imageStream) {
		return forumDao.insertImage(imageStream);
	}

	@Override
	public void addPost(int memberId, String title, String content, int imageId) {
		forumDao.addPost(memberId, title, content, imageId);
	}

	@Override
	public boolean addComment(Comment comment) {
		return forumDao.insertComment(comment);
	}

	@Override
	public boolean deletePost(int postId) {
		return forumDao.deletePostAndComments(postId);
	}

	@Override
	public boolean deleteComment(int commentId) {
		return forumDao.deleteComment(commentId);
	}
	
	@Override
	public void updatPostWithImage(int postId, String title, String content, Integer imageId) {
		forumDao.updatPostWithImage(postId, title, content, imageId);
	}

	@Override
	public void updatPostWithoutImage(int postId, String title, String content) {
		forumDao.updatePostWithoutImage(postId, title, content);
		
	}

	@Override
	public void updatPost(int postId, String title, String content) {
		forumDao.updatePost(postId, title, content);
	}

	
	@Override
	public void updateComment(int commentId, String content) {
		forumDao.updateComment(commentId, content);
		
	}

	@Override
	public boolean likePost(int postId, int memberId) {
		return forumDao.addLike(postId,memberId);
	}

	@Override
	public boolean unlikePost(int postId, int memberId) {
		return forumDao.cancelLike(postId,memberId);
	}

}
