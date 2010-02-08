package com.pillartech.blog.exception;

public class AccountRevokedException extends Exception {

	private static final long serialVersionUID = 7103881825333329605L;

	public AccountRevokedException(String msg) {
		super(msg);
	}
}
