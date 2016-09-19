package com.it.common.component.security;

import javax.servlet.http.HttpServletRequest;

public interface RoleManager {
	
	public boolean pass(HttpServletRequest request);

}
