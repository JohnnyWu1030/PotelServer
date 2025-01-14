package potel.account.service.impl;

import javax.naming.NamingException;

import potel.account.dao.impl.AccountDaoImpl;
import potel.account.service.AccountService;
import potel.account.vo.Member;

public class AccountServiceImpl implements AccountService {
	private AccountDaoImpl accountDao;

	public AccountServiceImpl() throws NamingException {
		this.accountDao = new AccountDaoImpl();
	}

	@Override
	public boolean addMember(Member member) {
		return accountDao.insertMember(member);
	}

	@Override
	public boolean updatepw(String password, String email) {
		return accountDao.updatePassword(password, email) > 0;
	}

	@Override
	public boolean checkEmailAndCellphone(String email, String cellphone) {
		return accountDao.selectForReset(email, cellphone);
	}

	@Override
	public boolean updateac(Member member) {
		return accountDao.updateAccount(member) > 0;
	}

	@Override
	public Member login(String input, String password) {
		if (input ==null || input.isEmpty()) {
			return null;
		}
		if (password ==null || password.isEmpty()) {
			return null;
		}
		return accountDao.selectMemeber(input, password);
	}


}

	
	

