package com.pillartech.blog.ui;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

public class AuthenticationFilter implements Filter {

	public static final String LOGIN_TARGET_URI = "blog/login.html";

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession();

		if (StringUtils.contains(request.getRequestURI(), LOGIN_TARGET_URI)) {
			chain.doFilter(req, res);
			return;
		}

		String user = (String) session.getAttribute("user");
		if (StringUtils.isEmpty(user)) {
			response.sendRedirect(LOGIN_TARGET_URI);
			chain.doFilter(req, res);
			return;
		}

		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
