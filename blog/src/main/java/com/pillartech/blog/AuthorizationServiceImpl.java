package com.pillartech.blog;

import java.util.Arrays;
import java.util.List;

public class AuthorizationServiceImpl implements AuthorizationService {

	@Override
	public boolean isAccessGranted(String username, String resource) {
		return getAuthorizedResourcesByUsername(username).contains(resource);
	}

	@Override
	public List<String> getAuthorizedResourcesByUsername(String username) {
		// Pull username rights from LDAP
		// Cross reference with privileges from main security system
		// Retrieve third party blog engine rights by username
		// Merge results into a coherent set of privs
		return Arrays.asList("NO-RIGHTS-FOR-YOU");
	}

}
