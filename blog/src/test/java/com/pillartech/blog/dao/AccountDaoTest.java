package com.pillartech.blog.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.notNullValue;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.pillartech.blog.Account;

@ContextConfiguration(locations = { "/persistence-context.xml" })
public class AccountDaoTest extends AbstractTransactionalTestNGSpringContextTests {

	private static final String TABLE_NAME = "ACCOUNTS";

	private Account accountFixture;
	
	@Autowired
	AccountDao target;

	@Autowired
	SessionFactory sessionFactory;

	@BeforeMethod
	public void createAccountFixture() {
		accountFixture = new Account();
		accountFixture.setUsername("todd");
		accountFixture.setEncryptedPassword("password");
	}

	@Test
	public void testCreateAccountAddsARow() {
		int theBeforeCount = super.countRowsInTable(TABLE_NAME);
		target.create(accountFixture);
		int theAfterCount = super.countRowsInTable(TABLE_NAME);

		assertThat(theAfterCount, is(equalTo(1 + theBeforeCount)));
	}

	@Test
	public void testCreateAccountPopulatesTheId() {
		Account theResult = target.create(accountFixture);

		assertThat(theResult.getId(), is(notNullValue()));
	}

	@Test
	public void testCreateAccountPopulatesAllFields() {
		Account theResult = target.create(accountFixture);
		
		assertThatTheDbFieldsMatch(theResult);
	}

	@Test
	public void testUpdateLeavesTheSameNumberOfRows() {
		Account toUpdate = target.create(accountFixture);
		int theBeforeCount = super.countRowsInTable(TABLE_NAME);
		toUpdate.setUsername("The Todd");
		target.update(toUpdate);
		flushHibernateSession();
		int theAfterCount = super.countRowsInTable(TABLE_NAME);

		assertThat(theBeforeCount, is(equalTo(theAfterCount)));
	}

	@Test
	public void testUpdateChangesTheDbRow() {
		Account toUpdate = target.create(accountFixture);
		toUpdate.setUsername("The Todd");
		toUpdate.setEncryptedPassword("encpass");
		toUpdate.setRevoked(true);
		Account theResult = target.update(toUpdate);
		flushHibernateSession();

		assertThatTheDbFieldsMatch(theResult);
	}

	@Test
	public void testDeleteAccountRemovesARow() {
		Account toDelete = target.create(accountFixture);
		int beforeCount = super.countRowsInTable(TABLE_NAME);
		target.delete(toDelete);
		flushHibernateSession();
		int afterCount = super.countRowsInTable(TABLE_NAME);

		assertThat(beforeCount, is(equalTo(afterCount + 1)));
	}

	@Test
	public void testDeleteAccountLeavesNoTrace() {
		Account toDelete = target.create(accountFixture);
		target.delete(toDelete);
		flushHibernateSession();
		int theNumberOfRowsFound = this.simpleJdbcTemplate.queryForInt(
				"select count(0) from accounts where id = ?", toDelete.getId());
		assertThat(theNumberOfRowsFound, is(0));
	}

	@Test
	public void testFindByUsernameReturnsNullWithNothingInTheDb() {
		Account theResult = target.findByUsername("todd");
		assertThat(theResult, is(nullValue()));
	}

	@Test
	public void testFindByUsernameReturnsTheCorrectRow() {
		Account theSource = target.create(accountFixture);
		Account theResult = target.findByUsername(theSource.getUsername());
		assertThat(theResult, is(notNullValue()));
		assertThat(theResult.getId(), is(equalTo(theSource.getId())));
	}


	@Test
	public void testFindByUsernameLimitsByUsername() {
		Account theSource = target.create(accountFixture);
		Account theResult = target.findByUsername(theSource.getUsername() + "-diddy");
		assertThat(theResult, is(nullValue()));
	}

	private void flushHibernateSession() {
		SessionFactoryUtils.getSession(sessionFactory, false).flush();
	}

	private void assertThatTheDbFieldsMatch(Account theSource) {
		Account theDbResult = (Account) simpleJdbcTemplate.queryForObject(
				"select id, username, encrypted_password, revoked from accounts where id = ?",
				new ParameterizedRowMapper<Account>() {
					public Account mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Account a = new Account();
						a.setId(rs.getLong("id"));
						a.setUsername(rs.getString("username"));
						a.setEncryptedPassword(rs.getString("encrypted_password"));
						a.setRevoked(rs.getBoolean("revoked"));
						return a;
					}
				}, theSource.getId());

		assertThat(theDbResult.getId(), is(equalTo(theSource.getId())));
		assertThat(theDbResult.getUsername(), is(equalTo(theSource.getUsername())));
		assertThat(theDbResult.getEncryptedPassword(), is(equalTo(theSource.getEncryptedPassword())));
		assertThat(theDbResult.isRevoked(), is(equalTo(theSource.isRevoked())));
	}

}
