package com.pillartech.blog;

import com.pillartech.blog.exception.AccountNotFoundException;
import com.pillartech.blog.exception.AccountRevokedException;

public interface LoginService {

	public void login(String username, String password) throws AccountNotFoundException, AccountRevokedException;

}
