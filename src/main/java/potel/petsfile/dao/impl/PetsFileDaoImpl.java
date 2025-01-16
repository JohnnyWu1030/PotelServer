package potel.petsfile.dao.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.sql.DataSource;

import potel.petsfile.dao.PetsFileDao;
import potel.petsfile.vo.Cat;
import potel.petsfile.vo.Dog;
import potel.utils.JDBCConstants;

public class PetsFileDaoImpl implements PetsFileDao {

	private DataSource ds;

	// 建構子，初始化數據源
	public PetsFileDaoImpl() {
		// 建議將數據源配置移到配置類中
//		ds = new HikariDataSource();
//		ds.setJdbcUrl("jdbc:mysql://114.32.203.170:3306/Potel");
//		ds.setUsername("root");
//		ds.setPassword("TIP102_25541859101");
//		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

		try {
			ds = JDBCConstants.getDataSource();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Dog> selectDog() {
		String sql = "SELECT * FROM dog"; // 查詢 dog 表格中的所有資料
		List<Dog> dogs = new ArrayList<>(); // 用來儲存查詢結果

		try (Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			System.out.println("Database connection established.");

			while (rs.next()) {
				Dog dog = new Dog();

				// 設置每一隻狗的資料
				dog.setDogOwner(rs.getString("DOGOWNER"));
				dog.setDogId(rs.getInt("DOGID"));
				dog.setDogName(rs.getString("DOGNAME"));
				dog.setDogBreed(rs.getString("DOGBREED"));
				dog.setDogGender(rs.getString("DOGGENDER"));
				dog.setDogImages(rs.getInt("DOGIMAGES"));

				dogs.add(dog); // 將 Dog 物件加入列表

				// 可選：在控制台輸出每隻狗的資料
				System.out.println("DOGID: " + dog.getDogId() + ", DOGNAME: " + dog.getDogName());
			}

			System.out.println("Fetched " + dogs.size() + " dogs from the database.");
		} catch (Exception e) {
			e.printStackTrace(); // 處理 SQL 異常
		}

		// 如果沒有查詢到資料，返回 null；否則返回查詢結果列表
		return dogs.isEmpty() ? null : dogs;
	}

	@Override
	public List<Cat> selectCat() {
		String sql = "SELECT * FROM cat"; // 查詢 cat 表格中的所有資料
		List<Cat> cats = new ArrayList<>(); // 用來儲存查詢結果

		try (Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			System.out.println("Database connection established.");

			while (rs.next()) {
				Cat cat = new Cat();

				// 設置每一隻貓的資料
				cat.setCatOwner(rs.getString("CATOWNER"));
				cat.setCatId(rs.getInt("CATID"));
				cat.setCatName(rs.getString("CATNAME"));
				cat.setCatBreed(rs.getString("CATBREED"));
				cat.setCatGender(rs.getString("CATGENDER"));
				cat.setCatImages(rs.getInt("CATIMAGES"));

				cats.add(cat); // 將 Cat 物件加入列表

				// 可選：在控制台輸出每隻貓的資料
				System.out.println("CATID: " + cat.getCatId() + ", CATNAME: " + cat.getCatName());
			}

			System.out.println("Fetched " + cats.size() + " cats from the database.");
		} catch (Exception e) {
			e.printStackTrace(); // 處理 SQL 異常
		}

		// 如果沒有查詢到資料，返回 null；否則返回查詢結果列表
		return cats.isEmpty() ? null : cats;
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
		} catch (Exception e) {
			e.printStackTrace();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean addDog(String dogOwner, String dogName, String dogBreed, String dogGender, int dogImages) {
	    String sql = "INSERT INTO dog (DOGOWNER, DOGNAME, DOGBREED, DOGGENDER, DOGIMAGES) VALUES (?, ?, ?, ?, ?)"; // 不需要 DOGID，會自動生成

	    try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

	        // 設置資料庫查詢參數
	        stmt.setString(1, dogOwner);
	        stmt.setString(2, dogName);
	        stmt.setString(3, dogBreed);
	        stmt.setString(4, dogGender);

	        // 處理 DOGIMAGES 參數，根據條件設置
	        if (dogImages > 0) {
	            stmt.setInt(5, dogImages); // 如果提供了圖片ID，設置為非 null
	        } else {
	            stmt.setNull(5, java.sql.Types.INTEGER); // 如果沒有圖片ID，設置為 null
	        }

	        // 執行插入操作
	        int affectedRows = stmt.executeUpdate();
	        return affectedRows > 0; // 如果受影響的行數大於 0，表示操作成功，返回 true
	    } catch (Exception e) {
	        e.printStackTrace(); // 錯誤處理
	        return false; // 出現異常時返回 false
	    }
	}

	@Override
	public boolean addCat(String catOwner, String catName, String catBreed, String catGender, int catImages) {
	    String sql = "INSERT INTO cat (CATOWNER, CATNAME, CATBREED, CATGENDER, CATIMAGES) VALUES (?, ?, ?, ?, ?)"; // 不需要 CATID，會自動生成

	    try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

	        // 設置資料庫查詢參數
	        stmt.setString(1, catOwner);
	        stmt.setString(2, catName);
	        stmt.setString(3, catBreed);
	        stmt.setString(4, catGender);

	        // 處理 CATIMAGES 參數，根據條件設置
	        if (catImages > 0) {
	            stmt.setInt(5, catImages); // 如果提供了圖片ID，設置為非 null
	        } else {
	            stmt.setNull(5, java.sql.Types.INTEGER); // 如果沒有圖片ID，設置為 null
	        }

	        // 執行插入操作
	        int affectedRows = stmt.executeUpdate();
	        return affectedRows > 0; // 如果受影響的行數大於 0，表示操作成功，返回 true
	    } catch (Exception e) {
	        e.printStackTrace(); // 錯誤處理
	        return false; // 出現異常時返回 false
	    }
	}


	@Override
	public boolean deleteDog(int dogId) {
		String sql = "DELETE FROM dog WHERE DOGID = ?"; // 使用 DOGID 刪除資料

		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, dogId); // 設置 DOGID 參數

			// 執行刪除操作，並檢查受影響的行數
			int affectedRows = pstmt.executeUpdate();

			// 如果有刪除的行，返回 true，否則返回 false
			return affectedRows > 0;

		} catch (Exception e) {
			e.printStackTrace(); // 捕獲並輸出錯誤信息
			return false; // 出現錯誤時返回 false
		}
	}

	@Override
	public boolean deleteCat(int catId) {
		String sql = "DELETE FROM cat WHERE CATID = ?"; // 使用 CATID 刪除資料

		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, catId); // 設置 CATID 參數

			// 執行刪除操作，並檢查受影響的行數
			int affectedRows = pstmt.executeUpdate();

			// 如果有刪除的行，返回 true，否則返回 false
			return affectedRows > 0;

		} catch (Exception e) {
			e.printStackTrace(); // 捕獲並輸出錯誤信息
			return false; // 出現錯誤時返回 false
		}
	}

	
	@Override
	public void updatPostWithImage(int postId, String title, String content, int imageId) {
		System.out.println("UPDATE SQL WithImage");
		String sql = "UPDATE forum SET TITLE = ?, CONTENT = ?, IMAGEID = ? WHERE POSTID = ?"; 

		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, title);
			stmt.setString(2, content);
			stmt.setInt(3,imageId);
			stmt.setInt(4, postId);
			
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateDog(int dogId, String dogOwner, String dogName, String dogBreed, String dogGender,
			int dogImages) {
		System.out.println("UPDATE SQL for Dog");

		// 定義 SQL 更新語句
		String sql = "UPDATE dog SET DOGOWNER = ?, DOGNAME = ?, DOGBREED = ?, DOGGENDER = ?, DOGIMAGES = ? WHERE DOGID = ?";

		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			// 設置更新語句中的參數
			stmt.setString(1, dogOwner); // DOGOWNER
			stmt.setString(2, dogName); // DOGNAME
			stmt.setString(3, dogBreed); // DOGBREED
			stmt.setString(4, dogGender); // DOGGENDER

			// 處理 DOGIMAGES，設置圖片ID或null
			if (dogImages > 0) {
				stmt.setInt(5, dogImages); // 設置有效的圖片ID
			} else {
				stmt.setNull(5, java.sql.Types.INTEGER); // 設置為null
			}

			// 設置要更新的DOGID
			stmt.setInt(6, dogId); // DOGID 用於識別需要更新的資料

			// 執行更新操作
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(); // 處理SQL異常
		}
	}

	@Override
	public void updateCat(int catId, String catOwner, String catName, String catBreed, String catGender,
			int catImages) {
		System.out.println("UPDATE SQL for Cat");

		// 定義 SQL 更新語句
		String sql = "UPDATE cat SET CATOWNER = ?, CATNAME = ?, CATBREED = ?, CATGENDER = ?, CATIMAGES = ? WHERE CATID = ?";

		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			// 設置更新語句中的參數
			stmt.setString(1, catOwner); // CATOWNER
			stmt.setString(2, catName); // CATNAME
			stmt.setString(3, catBreed); // CATBREED
			stmt.setString(4, catGender); // CATGENDER

			// 處理 CATIMAGES，設置圖片ID或null
			if (catImages > 0) {
				stmt.setInt(5, catImages); // 設置有效的圖片ID
			} else {
				stmt.setNull(5, java.sql.Types.INTEGER); // 設置為null
			}

			// 設置要更新的CATID
			stmt.setInt(6, catId); // CATID 用於識別需要更新的資料

			// 執行更新操作
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(); // 處理SQL異常
		}
	}

}
