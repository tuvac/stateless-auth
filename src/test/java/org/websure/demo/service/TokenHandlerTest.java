package org.websure.demo.service;

import static org.junit.Assert.*;

import java.security.SecureRandom;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.websure.demo.domain.User;
import org.websure.demo.domain.UserRole;

public class TokenHandlerTest {

	private TokenHandler tokenHandler;

	@Before
	public void setUp() throws Exception {
		
		byte[] secret = new byte[70];
		new SecureRandom().nextBytes(secret);
		tokenHandler = new TokenHandler(secret);
	}

	@Test
	public void testTokenHandlerCreateAndParse() throws Exception {

		final User user = new User();
		user.setUsername("someone@onthe.net");
		user.setExpires(new Date(new Date().getTime() + 10000).getTime());
		user.grantRole(UserRole.ADMIN);
		user.setPassword("abc");
		user.setNewPassword("def");
		
		final User parsedUser = tokenHandler.parseUserFromToken(tokenHandler.createTokenForUser(user));
		
		assertEquals(user.getUsername(), parsedUser.getUsername());
		assertTrue(parsedUser.hasRole(UserRole.ADMIN));	
		assertEquals(user.getUsername(), parsedUser.getUsername());
		assertNull(parsedUser.getPassword());
		assertNull(parsedUser.getNewPassword());		
	}

	@Test
	public void testTokenHandlerReturnsNullWhenExpired() throws Exception {
		
		final User user = new User();
		user.setUsername("someone@onthe.net");
		user.setExpires(new Date().getTime() - 10);
		user.grantRole(UserRole.ADMIN);
		
		final User parsedUser = tokenHandler.parseUserFromToken(tokenHandler.createTokenForUser(user));
		
		assertNull(parsedUser);
	}


}
