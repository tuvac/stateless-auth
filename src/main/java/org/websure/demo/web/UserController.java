package org.websure.demo.web;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.websure.demo.domain.User;
import org.websure.demo.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;	

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@Validated @RequestBody User u) {

		User user = userService.createUser(u);

		URI loc = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{userId}")
				.buildAndExpand(user.getUserId())
				.toUri();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(loc);

		return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public @ResponseBody User findUser(@PathVariable Integer userId) {

		return userService.findUser(userId);
	}
	
}
