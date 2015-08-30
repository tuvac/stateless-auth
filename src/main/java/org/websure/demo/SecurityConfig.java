package org.websure.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.websure.demo.service.TokenAuthenticationService;
import org.websure.demo.service.UserService;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	public SecurityConfig() {

		super(true);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
				.exceptionHandling()
				.and()
				.anonymous()
				.and()
				.servletApi()
				.and()
				.headers()
				.cacheControl()
				.and()
				.authorizeRequests()

				// allow anonymous resource requests
				.antMatchers("/")
				.permitAll()
				.antMatchers("/favicon.ico")
				.permitAll()
				.antMatchers("/resources/**")
				.permitAll()

				// allow anonymous POSTs to user for registration
				.antMatchers(HttpMethod.POST, "/user/**")
				.permitAll()

				// //allow anonymous GETs to API
				 //.antMatchers(HttpMethod.GET, "/**").permitAll()

				// defined Admin only API area
				.antMatchers("/admin/**")
				.hasRole("ADMIN")

				// all other request need to be authenticated
				.anyRequest()
				.hasRole("USER")
				//.hasAnyRole()
				.and()

				// custom JSON based authentication by POST of
				// {"username":"<name>","password":"<password>"} which sets the
				// token header upon authentication
				.addFilterBefore(new StatelessLoginFilter("/request-token", tokenAuthenticationService, userService, authenticationManager()),
						UsernamePasswordAuthenticationFilter.class)

				// custom Token based authentication based on the header
				// previously given to the client
				.addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {

		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userService).passwordEncoder(new StandardPasswordEncoder());
	}

	@Override
	protected UserService userDetailsService() {

		return userService;
	}
}
