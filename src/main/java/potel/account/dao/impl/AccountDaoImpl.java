package potel.account.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import potel.account.dao.AccountDao;

import potel.account.vo.Member;

public class AccountDaoImpl implements AccountDao {
	private DataSource ds;

	public AccountDaoImpl() throws NamingException {
		ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/potel");
	}

	@Override
	public List<Member> selectAll() {
		String sql = "SELECT * FROM account";
		List<Member> members = new ArrayList<>();

		try (Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			System.out.println("Database connection established.");

			while (rs.next()) {
				Member member = new Member();
				member.setName(rs.getString("name"));
				member.setCellphone(rs.getString("cellphone"));
				member.setAddress(rs.getString("address"));
				member.setBirthday(rs.getString("birthday"));
				member.setEmail(rs.getString("email"));
				member.setPasswd(rs.getString("passwd"));
				members.add(member);
			}

			System.out.println("Finished retrieving accounts.");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return members.isEmpty() ? null : members;
	}

	@Override
	public boolean insertMember(Member member) {
		String sql = "INSERT INTO members (name, cellphone, address ,gender , email, passwd ,birthday ) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, member.getName());
			stmt.setString(2, member.getCellphone());
			stmt.setString(3, member.getAddress());
			stmt.setString(4, member.getGender() != null ? member.getGender().toString() : null);
			stmt.setString(5, member.getEmail());
			stmt.setString(6, member.getPasswd());
			stmt.setString(7, member.getBirthday());
//			stmt.setInt(8, member.getImageid());
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean selectForReset(String email, String cellphone) {
		String sql = "SELECT 1 FROM members Where EMAIL = ?  and CELLPHONE = ? ";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, email);
			pstmt.setString(2, cellphone);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int updatePassword(String password, String email) {
		String sql = "update members set passwd = ? where email = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, password);
			stmt.setString(2, email);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public boolean updateac(Integer memberid, String cellphone, String address, String email, String passwd) {
		String sql = "";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, memberid);
			pstmt.setString(2, cellphone);
			pstmt.setString(3, address);
			pstmt.setString(4, email);
			pstmt.setString(5, passwd);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int updateAccount(Member member) {
		String sql = "update members set cellphone = ?  address =?  email = ? passwd = ?  where memberid = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, member.getCellphone());
			stmt.setString(2, member.getAddress());
			stmt.setString(3, member.getEmail());
			stmt.setString(4, member.getPasswd());
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public Member selectMemeber(String input, String password) {
		String sql = "SELECT * FROM members Where (EMAIL = ?  or  CELLPHONE = ?)  and PASSWD = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, input);
			pstmt.setString(2, input);
			pstmt.setString(3, password);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
//					Member member = new Member();
//					member.setMemberid(rs.getInt("MEMBERID"));
//					member.setName(rs.getString("NAME"));
//					member.setCellphone(rs.getString("CELLPHONE"));
//					member.setAddress(rs.getString("ADDRESS"));
//					member.setGender(rs.getString("GENDER").charAt(0));
//					member.setBirthday(rs.getString("BIRTHDAY"));
//					member.setEmail(rs.getString("EMAIL"));
//					member.setPasswd(rs.getString("PASSWD"));
//					member.setImageid(rs.getInt("IMAGEID"));
//					
					  Member member = new Member();
				        
				        // 取得 MEMBERID 並印出
				        int memberId = rs.getInt("MEMBERID");
				        member.setMemberid(memberId);
				        System.out.println("MemberID: " + memberId);
				        
				        // 取得 NAME 並印出
				        String name = rs.getString("NAME");
				        member.setName(name);
				        System.out.println("Name: " + name);
				        
				        // 取得 CELLPHONE 並印出
				        String cellphone = rs.getString("CELLPHONE");
				        member.setCellphone(cellphone);
				        System.out.println("Cellphone: " + cellphone);
				        
				        // 取得 ADDRESS 並印出
				        String address = rs.getString("ADDRESS");
				        member.setAddress(address);
				        System.out.println("Address: " + address);
				        
				        // 取得 GENDER 並印出
				        String genderStr = rs.getString("GENDER");
				        char gender = genderStr.charAt(0);
				        member.setGender(gender);
				        System.out.println("Gender: " + gender);
				        
				        // 取得 BIRTHDAY 並印出
				        String birthday = rs.getString("BIRTHDAY");
				        member.setBirthday(birthday);
				        System.out.println("Birthday: " + birthday);
				        
				        // 取得 EMAIL 並印出
				        String email = rs.getString("EMAIL");
				        member.setEmail(email);
				        System.out.println("Email: " + email);
				        
				        // 取得 PASSWD 並印出
				        String passwd = rs.getString("PASSWD");
				        member.setPasswd(passwd);
				        System.out.println("Passwd: " + passwd);
				        
				        // 取得 IMAGEID 並印出
				        int imageId = rs.getInt("IMAGEID");
				        member.setImageid(imageId);
				        System.out.println("ImageID: " + imageId);
				        
				        return member;
				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}
}