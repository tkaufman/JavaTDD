package com.pillartech.blog.exception;

public class AccountNotFoundException extends Exception {

	private static final long serialVersionUID = 6697703848985241689L;

	public AccountNotFoundException(String msg) {
		super(msg);
	}

	public AccountNotFoundException(String msg, Exception ex) {
		super(msg, ex);
	}
}
