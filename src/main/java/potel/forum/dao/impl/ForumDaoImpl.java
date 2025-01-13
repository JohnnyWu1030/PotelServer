package potel.forum.dao.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import potel.utils.Defines;
import com.zaxxer.hikari.HikariDataSource;
import potel.forum.dao.ForumDao;
import potel.forum.vo.Comment;
import potel.forum.vo.CommentWithMemberName;
import potel.forum.vo.ForumWithMemberName;
import potel.forum.vo.Like;

public class ForumDaoImpl implements ForumDao {

	private HikariDataSource ds;

	public ForumDaoImpl() {
		// 配置 HikariCP
		ds = new HikariDataSource();
		ds.setJdbcUrl("jdbc:mysql://114.32.203.170:3306/potel");
//		ds.setJdbcUrl("jdbc:mysql://localhost:3306/potel");
		ds.setUsername("root");
		ds.setPassword("TIP102_25541859101");
//		ds.setPassword("89705131");
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

		// HikariCP 配置
		ds.setIdleTimeout(60000); // 60 秒
		ds.setMaxLifetime(1800000); // 30 分鐘
		ds.setConnectionTimeout(30000); // 30 秒

	}

	@Override
	public List<ForumWithMemberName> selectAll() {
		String sql = "SELECT f.POSTID, f.MEMBERID, f.TITLE, f.CONTENT, f.CREATEDATE, f.MODIFYDATE, f.IMAGEID, m.NAME AS MEMBERNAME FROM forum f JOIN members m ON f.MEMBERID = m.MEMBERID;";
		List<ForumWithMemberName> forums = new ArrayList<>();

		try (Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			System.out.println("Database connection established.");

			while (rs.next()) {
				Object temp = null;
				ForumWithMemberName forum = new ForumWithMemberName();
				forum.setPostId(rs.getInt("POSTID"));
				forum.setMemberId(rs.getInt("MEMBERID"));
				forum.setTitle(rs.getString("TITLE"));
				forum.setContent(rs.getString("CONTENT"));
				forum.setCreateDate((temp = rs.getTimestamp("CREATEDATE")) == null ? null : Defines.sdf.format(temp));
				forum.setModifyDate((temp = rs.getTimestamp("MODIFYDATE")) == null ? null : Defines.sdf.format(temp));
				int imageId = rs.getInt("IMAGEID");
				if (rs.wasNull()) {
					forum.setImageId(null); // 設置為 null
				} else {
					forum.setImageId(imageId);
				}
				 forum.setMemberName(rs.getString("MEMBERNAME")); // 設置 MemberName
			     forums.add(forum);
			}

			System.out.println("Fetched " + forums.size() + " forums from the database.");
		} catch (SQLException e) {
			handleSQLException(e);
		}

		return forums.isEmpty() ? null : forums;
	}

	@Override
	public List<Like> selectlike() {
		String sql = "SELECT * FROM likes";
		List<Like> likes = new ArrayList<>();

		try (Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			System.out.println("Database connection established.");

			while (rs.next()) {
				Like like = new Like();
				like.setLikeId(rs.getInt("LIKEID"));
				like.setMemberId(rs.getInt("MEMBERID"));
				like.setPostId(rs.getInt("POSTID"));
				likes.add(like);
			}

			System.out.println("Fetched " + likes.size() + " likes from the database.");
		} catch (SQLException e) {
			handleSQLException(e);
		}

		return likes.isEmpty() ? null : likes;
	}

	@Override
	public List<CommentWithMemberName> selectComment() {
		String sql = "  SELECT c.COMMENTID, c.POSTID, c.MEMBERID, c.CONTENT, c.CREATEDATE, c.MODIFYDATE, m.NAME AS MEMBERNAME FROM comments c JOIN members m ON  c.MEMBERID = m.MEMBERID";
		List<CommentWithMemberName> comments = new ArrayList<>();

		try (Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			System.out.println("Database connection established.");

			while (rs.next()) {
				Object temp = null;
				CommentWithMemberName comment = new CommentWithMemberName();
				comment.setCommentId(rs.getInt("COMMENTID"));
				comment.setPostId(rs.getInt("POSTID"));
				comment.setMemberId(rs.getInt("MEMBERID"));
				comment.setContent(rs.getString("CONTENT"));
				comment.setCreateDate((temp = rs.getTimestamp("CREATEDATE")) == null ? null : Defines.sdf.format(temp));
				comment.setModifyDate((temp = rs.getTimestamp("MODIFYDATE")) == null ? null : Defines.sdf.format(temp));
				comment.setMemberName(rs.getString("MEMBERNAME"));
				comments.add(comment);
			}

			System.out.println("Fetched " + comments.size() + " comments from the database.");
		} catch (SQLException e) {
			handleSQLException(e);
		}

		return comments.isEmpty() ? null : comments;
	}

	@Override
	public Integer insertImage(InputStream imageStream) {
		int imageId = 0;
		String sql = "INSERT INTO images (IMAGEDATA) VALUES (?)";

		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setBlob(1, imageStream);
			stmt.executeUpdate();

			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					imageId = generatedKeys.getInt(1);
				}
			}
		} catch (SQLException e) {
			handleSQLException(e);
		}

		return imageId;
	}

	@Override
	public void addPost(int memberId, String title, String content, int imageId) {
		String sql = "INSERT INTO forum (MEMBERID, TITLE, CONTENT, IMAGEID) VALUES (?, ?, ?, ?)"; // 去除 CREATEDATE

		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, memberId);
			stmt.setString(2, title);
			stmt.setString(3, content);
			if (imageId > 0) {
				stmt.setInt(4, imageId);
			} else {
				stmt.setNull(4, java.sql.Types.INTEGER);
			}
			stmt.executeUpdate();
		} catch (SQLException e) {
			handleSQLException(e);
		}
	}

	@Override
	public boolean insertComment(Comment comment) {
		String sql = "INSERT INTO comments (POSTID, MEMBERID, CONTENT) VALUES (?, ?, ?)";

		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, comment.getPostId());
			stmt.setInt(2, comment.getMemberId());
			stmt.setString(3, comment.getContent());

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			handleSQLException(e);
			return false;
		}
	}

	public boolean isConnectionValid() {
		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT 1")) {
			return stmt.execute();
		} catch (SQLException e) {
			handleSQLException(e);
			return false;
		}
	}

	private void handleSQLException(SQLException e) {
		if (e.getSQLState() != null && e.getSQLState().startsWith("08")) {
			System.err.println("Database connection issue: " + e.getMessage());
		} else {
			e.printStackTrace();
		}
	}

	public void printConnectionPoolStats() {
		System.out.println("Active Connections: " + ds.getHikariPoolMXBean().getActiveConnections());
		System.out.println("Idle Connections: " + ds.getHikariPoolMXBean().getIdleConnections());
		System.out.println("Total Connections: " + ds.getHikariPoolMXBean().getTotalConnections());
	}

	@Override
	public boolean deletePostAndComments(int postId) {
		try (Connection connection = ds.getConnection()) {
			System.out.println("Database connection established.");

			// 開啟交易
			connection.setAutoCommit(false);

			try {
				// 刪除相關留言
				String deleteCommentsSql = "DELETE FROM comments WHERE POSTID = ?";
				try (PreparedStatement deleteCommentsStmt = connection.prepareStatement(deleteCommentsSql)) {
					deleteCommentsStmt.setInt(1, postId);
					deleteCommentsStmt.executeUpdate();
				}

				String deleteLikesSql = "DELETE FROM likes WHERE POSTID = ?";
				try (PreparedStatement deleteLikesStmt = connection.prepareStatement(deleteLikesSql)) {
					deleteLikesStmt.setInt(1, postId);
					deleteLikesStmt.executeUpdate();
				}
				// 刪除文章
				String deletePostSql = "DELETE FROM forum WHERE POSTID = ?";
				try (PreparedStatement deletePostStmt = connection.prepareStatement(deletePostSql)) {
					deletePostStmt.setInt(1, postId);
					int rowsAffected = deletePostStmt.executeUpdate();

					// 確保文章被刪除
					if (rowsAffected == 0) {
						connection.rollback(); // 如果文章未刪除，回滾交易
						return false;
					}
				}

				// 提交交易
				connection.commit();
				return true;

			} catch (SQLException e) {
				// 發生錯誤時回滾交易
				connection.rollback();
				throw e; // 抛出異常，讓外層捕獲
			} finally {
				// 恢復默認的自動提交模式
				connection.setAutoCommit(true);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteComment(int commentId) {
		String sql = "DELETE FROM comments WHERE COMMENTID = ?";

		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, commentId);

			int affectedRows = pstmt.executeUpdate();

			return affectedRows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void updatPostWithImage(int postId, String title, String content, int imageId) {
		System.out.println("UPDATE SQL WithImage");
		String sql = "UPDATE forum SET TITLE = ?, CONTENT = ?, IMAGEID = ? WHERE POSTID = ?";

		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, title);
			stmt.setString(2, content);
			stmt.setInt(3, imageId);
			stmt.setInt(4, postId);

			stmt.executeUpdate();
		} catch (SQLException e) {
			handleSQLException(e);
		}
	}

	@Override
	public void updatePostWithoutImage(int postId, String title, String content) {
		System.out.println("UPDATE SQL WithoutImage");
		String sql = "UPDATE forum SET TITLE = ?, CONTENT = ?, IMAGEID = NULL WHERE POSTID = ?";

		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, title);
			stmt.setString(2, content);
			stmt.setInt(3, postId);

			stmt.executeUpdate();
		} catch (SQLException e) {
			handleSQLException(e);
		}
	}

	@Override
	public void updatePost(int postId, String title, String content) {
		System.out.println("UPDATE SQL same Image");
		String sql = "UPDATE forum SET TITLE = ?, CONTENT = ? WHERE POSTID = ?";

		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, title);
			stmt.setString(2, content);
			stmt.setInt(3, postId);

			stmt.executeUpdate();
		} catch (SQLException e) {
			handleSQLException(e);
		}
	}

	@Override
	public void updateComment(int commentId, String content) {
		System.out.println("UPDATE SQL comment");
		String sql = "UPDATE comments SET CONTENT = ? WHERE COMMENTID = ?";

		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, content);
			stmt.setInt(2, commentId);

			stmt.executeUpdate();
		} catch (SQLException e) {
			handleSQLException(e);
		}

	}

	@Override
	public boolean addLike(int postId, int memberId) {
		String sql = "INSERT INTO likes (MEMBERID, POSTID) VALUES (?, ?)";
		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, memberId);
			stmt.setInt(2, postId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean cancelLike(int postId, int memberId) {
		String sql = "DELETE FROM likes WHERE MEMBERID = ? AND POSTID = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, memberId);
			stmt.setInt(2, postId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
