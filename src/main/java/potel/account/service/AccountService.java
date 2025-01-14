package potel.account.service;

import potel.account.vo.Member;

public interface AccountService {

	boolean addMember(Member member);

	boolean updatepw(String password, String email);

	boolean checkEmailAndCellphone(String email, String cellphone);
	
	Member login(String input, String password);

	boolean updateac(Member member);
	


}
