package com.pillartech.blog;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

@PrepareForTest(Account.class)
public class AccountTest {

	private static final String UNENCRYPTED = "password";
	private static final String ENCRYPTED = "heavilyencryptedpassword";

	@BeforeMethod
	public void prepareStaticMocks() {
		mockStatic(Account.class);
		when(Account.encryptPassword(UNENCRYPTED)).thenReturn(ENCRYPTED);		
	}
	
	@Test
	public void shouldStoreEncryptedPassword() {
		Account target = new Account();
		target.setUnencryptedPassword(UNENCRYPTED);
		
		// Must be called before static method verify
		verifyStatic();
		Account.encryptPassword(UNENCRYPTED);
		
		assertThat(target.getEncryptedPassword(), is(ENCRYPTED));
	}
	
	@Test
	public void shouldUseEncryptionForPasswordMatching() {
		Account target = new Account();
		target.setEncryptedPassword(ENCRYPTED);
		assertThat(target.passwordMatches(UNENCRYPTED), is(true));
		
		// Must be called before static method verify
		verifyStatic();
		Account.encryptPassword(UNENCRYPTED);
	}
	
	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	// JUnit is easier than xml suites
	/*	
		 @RunWith(PowerMockRunner.class)
		 public class ExampleTest {
		 ...
		 }
	*/
}
