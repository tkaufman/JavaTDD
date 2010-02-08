package com.pillartech.blog;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AuthorizationTest {

	private AuthorizationService target;

	@BeforeMethod
	public void setupTarget() {
		target = new AuthorizationServiceImpl();
	}

	@Test
	public void shouldBeARealTestThatDealsWithManyDependencies() {
		// Setup Mock to pull username rights from LDAP
		// Setup Mock for the main security system
		// Setup Mock for the third party blog engine
		List<String> results = target.getAuthorizedResourcesByUsername("todd");
		assertThat(results, hasItem("NO-RIGHTS-FOR-YOU"));
		// Verify mock interactions for LDAP
		// Verify mock interactions for main security system
		// Verify mock interactions for third party blog engine
	}

	@Test
	public void shouldBeEasierToStubOutAMethodForTheIsAccessGranted() {

		target = new PartialAuthorizationServiceImpl();
		assertThat(target.isAccessGranted("todd", "homepage"), is(true));
		assertThat(target.isAccessGranted("homer", "beer-and-donuts"), is(false));
	}

	class PartialAuthorizationServiceImpl extends AuthorizationServiceImpl {
		@Override
		public List<String> getAuthorizedResourcesByUsername(String username) {
			return Arrays.asList("homepage", "loginpage");
		}
	}
}
