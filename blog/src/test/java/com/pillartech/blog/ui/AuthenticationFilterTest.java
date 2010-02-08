package com.pillartech.blog.ui;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.pillartech.blog.ui.AuthenticationFilter;

public class AuthenticationFilterTest {

	private AuthenticationFilter target;
	private MockHttpServletRequest stubRequest;
	private MockHttpServletResponse stubResponse;
	private MockFilterChain stubChain;

	@BeforeMethod
	public void setUp() throws Exception {
		target = new AuthenticationFilter();
		stubRequest = new MockHttpServletRequest();
		stubResponse = new MockHttpServletResponse();
		stubChain = new MockFilterChain();
	}

	@Test
	public void testGoingToLoginBlindlyPassesThrough() throws IOException, ServletException {
		stubRequest.setRequestURI(AuthenticationFilter.LOGIN_TARGET_URI);
		target.doFilter(stubRequest, stubResponse, stubChain);
		String theRedirectionUrl = stubResponse.getRedirectedUrl();
		assertThat(theRedirectionUrl, is(nullValue()));
	}

	@Test
	public void testWithoutUserInSessionRedirectsToLogin() throws IOException, ServletException {
		target.doFilter(stubRequest, stubResponse, stubChain);
		String theRedirectionUrl = stubResponse.getRedirectedUrl();
		assertThat(theRedirectionUrl, is(AuthenticationFilter.LOGIN_TARGET_URI));
	}

	@Test
	public void testWithUserInSessionPassesThrough() throws IOException, ServletException {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("user", "The Todd");
		stubRequest.setSession(session);
		target.doFilter(stubRequest, stubResponse, stubChain);
		String theRedirectionUrl = stubResponse.getRedirectedUrl();
		assertThat(theRedirectionUrl, is(nullValue()));
	}
}
