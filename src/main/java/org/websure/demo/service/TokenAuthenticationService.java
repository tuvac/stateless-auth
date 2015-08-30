package org.websure.demo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

public interface TokenAuthenticationService {

	void addAuthentication(HttpServletResponse response, UserAuthentication authentication);

	Authentication getAuthentication(HttpServletRequest request);

}
