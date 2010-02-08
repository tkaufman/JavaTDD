package com.pillartech.blog;

import java.util.HashMap;
import java.util.Map;

import com.pillartech.blog.dao.AccountDao;
import com.pillartech.blog.exception.AccountNotFoundException;
import com.pillartech.blog.exception.AccountRevokedException;

public class LoginServiceImpl implements LoginService{

	private AccountDao dao;
	private Map<String, Integer> failedAttempts;
	
	
	public LoginServiceImpl(AccountDao dao) {
		this.dao = dao;
		this.failedAttempts = new HashMap<String, Integer>();
	}
	
	public void login(String username, String password) throws AccountNotFoundException, AccountRevokedException {
		Account account = findAccountByUsername(username);
		
		if (account.isRevoked()) {
			throw new AccountRevokedException("Account has been revoked for too many failed login attempts.");
		}
		
		if (account.passwordMatches(password)) {
			account.setLoggedIn(true);
		}
		else {
			storeFailedAttempt(account);
			if (failedAttemptsPer(account) > 2) {
				account.setRevoked(true);
			}
		}
	}

	private Account findAccountByUsername(String username) throws AccountNotFoundException {
		try {			
			Account account = dao.findByUsername(username);
			if (account == null) {
				throw new AccountNotFoundException("Account with username " + username + " was not found.");
			}
			return account;
		}
		catch (RuntimeException ex) {
			throw new AccountNotFoundException("Unable to find account due to underlying issue", ex);
		}
	}
		
	private Integer failedAttemptsPer(Account account) {
		return failedAttempts.get(account.getUsername());
	}

	private void storeFailedAttempt(Account account) {
		String key = account.getUsername();
		if (failedAttempts.containsKey(key)) {
			int failures = failedAttempts.get(key);
			failedAttempts.put(key, ++failures);
		}
		else {
			failedAttempts.put(key, 1);
		}
	}
}
