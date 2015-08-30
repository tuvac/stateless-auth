package org.websure.demo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.websure.demo.domain.User;

@Service
public class TokenAuthenticationServiceImpl implements TokenAuthenticationService {

	private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

	private static final long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;

	private final TokenHandler tokenHandler;

	@Autowired
	public TokenAuthenticationServiceImpl(@Value("${token.secret}") String secret) {
		tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secret));
	}

	@Override
	public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {

		final User user = authentication.getDetails();
		user.setExpires(System.currentTimeMillis() + TEN_DAYS);
		response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(user));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.websure.demo.service.TokenAuthenticationService#getAuthentication(
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public Authentication getAuthentication(HttpServletRequest request) {

		final String token = request.getHeader(AUTH_HEADER_NAME);
		if (token != null) {
			final User user = tokenHandler.parseUserFromToken(token);
			if (user != null) {
				return new UserAuthentication(user);
			}
		}
		return null;
	}
}
