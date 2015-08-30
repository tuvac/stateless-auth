package org.websure.demo;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.websure.demo.domain.User;
import org.websure.demo.service.TokenAuthenticationService;
import org.websure.demo.service.UserAuthentication;
import org.websure.demo.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

	private final TokenAuthenticationService tokenAuthenticationService;

	private final UserService userService;

	protected StatelessLoginFilter(String urlMapping, TokenAuthenticationService tokenAuthenticationService, UserService userDetailsService,
			AuthenticationManager authManager) {
		
		super(new AntPathRequestMatcher(urlMapping));
		this.userService = userDetailsService;
		this.tokenAuthenticationService = tokenAuthenticationService;
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		final User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
		final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		//final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getRawPassword());
		return getAuthenticationManager().authenticate(loginToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
			throws IOException, ServletException {

		// Lookup the complete User object from the database and create an
		// Authentication for it
		final User authenticatedUser = (User) userService.loadUserByUsername(authentication.getName());
		final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

		// Add the custom token as HTTP header to the response
		tokenAuthenticationService.addAuthentication(response, userAuthentication);

		// Add the authentication to the Security context
		SecurityContextHolder.getContext().setAuthentication(userAuthentication);
	}

}
