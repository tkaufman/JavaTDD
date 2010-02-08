package com.pillartech.blog.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.pillartech.blog.Account;

public class AccountDaoHibernateImpl implements AccountDao  {

	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory factory) {
		this.sessionFactory = factory;
	}
	
	private Session currentSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	public Account findByUsername(String username) {
        return (Account) currentSession()
        	.createQuery("from Account where username=?")
        	.setParameter(0, username)
        	.uniqueResult();
    }

	public Account create(Account account) {
		currentSession().save(account);
		return account;
	}

	public Account update(Account account) {
		currentSession().saveOrUpdate(account);
		return account;
	}

	public void delete(Account account) {
		currentSession().delete(account);
	}
}
