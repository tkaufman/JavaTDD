package com.pillartech.blog.dao;

import com.pillartech.blog.Account;

public interface AccountDao {

	public Account findByUsername(String username);

	public Account create(Account account);

	public Account update(Account account);

	public void delete(Account account);
}
