package com.pillartech.blog;

import org.apache.commons.lang.StringUtils;

public class Account {
	
	private Long id;
	
	private String username;
	private String encryptedPassword;
	private boolean loggedIn;
	private boolean revoked;

	public boolean passwordMatches(String password) {
		return encryptedPassword.equals(Account.encryptPassword(password));
	}
	
	public void setUnencryptedPassword(String password) {
		this.encryptedPassword = Account.encryptPassword(password);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isRevoked() {
		return revoked;
	}

	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	static final String encryptPassword(String password) {
		return StringUtils.reverse(password);
	}
}
