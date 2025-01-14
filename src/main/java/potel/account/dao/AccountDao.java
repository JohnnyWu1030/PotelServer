package potel.account.dao;

import java.util.List;

import potel.account.vo.Member;

public interface AccountDao {

	List<Member> selectAll();

	boolean insertMember(Member member);

	boolean selectForReset(String email, String cellphone);

	int updatePassword(String password, String email);

	boolean updateac(Integer memberid, String cellphone, String address, String email, String passwd);

	int updateAccount(Member member);

	Member selectMemeber(String input, String password) ;

	
	
}
