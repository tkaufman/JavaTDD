package com.pillartech.blog;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentMatcher;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.pillartech.blog.dao.AccountDao;
import com.pillartech.blog.exception.AccountNotFoundException;
import com.pillartech.blog.exception.AccountRevokedException;

public class LoginServiceTest {

	private Account mockAccount;
	private AccountDao mockDao;
	private LoginService target;

	@Test
	public void shouldLoginValidPasswords() throws Exception {

		// Stub the password matches method
		mockAccount = mock(Account.class);
		when(mockAccount.passwordMatches(anyString())).thenReturn(true);

		// Stub the dao
		mockDao = mock(AccountDao.class);
		when(mockDao.findByUsername(anyString())).thenReturn(mockAccount);
		
		// Execute the method under test
		target = new LoginServiceImpl(mockDao);
		target.login("todd","password");
		
		// Validate the mock's behavior
		verify(mockAccount).setLoggedIn(true);
		
		// redundant with
		verify(mockAccount, times(1)).setLoggedIn(true);
	}

	// To reduce redundancy
	@BeforeMethod
	public void setupMocksAndTargetForDefaultBehavior() throws Exception {
		mockAccount = mock(Account.class);
		when(mockAccount.getUsername()).thenReturn("todd");
		mockDao = mock(AccountDao.class);
		when(mockDao.findByUsername(anyString())).thenReturn(mockAccount);
		target = new LoginServiceImpl(mockDao);
	}
		
	@Test
	public void shouldNotLoginBadPasswords() throws Exception {

		when(mockAccount.passwordMatches(anyString())).thenReturn(false);

		target.login("todd","password");
		
		verify(mockAccount, never()).setLoggedIn(true);
	}
	
	@Test
	public void shouldInactivateAfterThreeFailedPasswords() throws Exception {

		when(mockAccount.passwordMatches(anyString())).thenReturn(false);

		for (int i=0;i<3;i++) {
			target.login("todd","password");
		}

		verify(mockAccount, atLeastOnce()).setRevoked(true);
	}
	
	@Test
	public void shouldNotInactivateUsersAfterThreeTotalFailedPasswords_RepetitiveApproach() throws Exception {
		when(mockAccount.passwordMatches(anyString())).thenReturn(false);

		Account mockAccount2 = mock(Account.class);
		when(mockAccount2.passwordMatches(anyString())).thenReturn(false);

		when(mockDao.findByUsername(anyString()))
			.thenReturn(mockAccount)
			.thenReturn(mockAccount)
			.thenReturn(mockAccount2);
		
		for (int i=0;i<2;i++) {
			target.login("todd","password");
		}
		target.login("steve", "1234");
		
		verify(mockAccount, never()).setRevoked(anyBoolean());
		verify(mockAccount2, never()).setRevoked(anyBoolean());
	}
	
	@Test
	public void shouldNotInactivateUsersAfterThreeTotalFailedPasswords_CustomMatcherApproach() throws Exception {
		when(mockAccount.passwordMatches(anyString())).thenReturn(false);

		Account mockAccount2 = mock(Account.class);
		when(mockAccount2.passwordMatches(anyString())).thenReturn(false);

		when(mockDao.findByUsername("todd")).thenReturn(mockAccount);
		when(mockDao.findByUsername(argThat(isNotTodd()))).thenReturn(mockAccount2);

		for (int i=0;i<2;i++) {
			target.login("todd","password");
		}
		target.login("steve", "1234");
		
		verify(mockAccount, never()).setRevoked(anyBoolean());
		verify(mockAccount2, never()).setRevoked(anyBoolean());
	}
	
	private class IsNotTodd extends ArgumentMatcher<String> {
		public boolean matches(Object str) {
			return !((String) str).equalsIgnoreCase("todd");
		}
	}
	
	private ArgumentMatcher<String> isNotTodd() {
		return new IsNotTodd();
	}
	
	@Test(expectedExceptions = {AccountNotFoundException.class})
	public void shouldThrowAnExceptionIfAccountIsNotFound() throws Exception {
		when(mockDao.findByUsername(anyString())).thenReturn(null);
		target.login("todd", "password");
	}

	@Test(expectedExceptions = {AccountRevokedException.class})
	public void shouldThrowAnExceptionIfAccountIsInactive() throws Exception {
		Account revokedAccount = new Account();
		revokedAccount.setRevoked(true);
		when(mockDao.findByUsername(anyString())).thenReturn(revokedAccount);

		target.login("todd", "password");
	}
	
	@Test(expectedExceptions = {AccountNotFoundException.class})
	public void shouldThrowBusinessExceptionIfDatabaseIsDown() throws Exception {
		when(mockDao.findByUsername(anyString())).thenThrow(new RuntimeException());
		target.login("todd", "password");
	}
	
	// Some JUnit coolness
/*	
	 @RunWith(MockitoJUnit44Runner.class)
	 public class ExampleTest {
	 
	     @Mock
	     private List list;
	 
	     @Test
	     public void shouldDoSomething() {
	         list.add(100);
	     }
	 }
*/
	
}
