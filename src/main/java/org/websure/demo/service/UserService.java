package org.websure.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.websure.demo.domain.User;

public interface UserService extends UserDetailsService {

	User createUser(User newUser);

	User findUser(Integer userId);

	User findUserByUsername(String username);

}