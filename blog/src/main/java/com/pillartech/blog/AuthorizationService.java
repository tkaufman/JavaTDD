package com.pillartech.blog;

import java.util.List;

public interface AuthorizationService {

	public boolean isAccessGranted(String username, String resource);
	
	public List<String> getAuthorizedResourcesByUsername(String username);
}
