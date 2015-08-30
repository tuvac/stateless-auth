package org.websure.demo.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.websure.demo.domain.User;
import org.websure.demo.domain.UserRepository;
import org.websure.demo.domain.UserRole;

@Service
public class UserServiceImpl implements UserService {
	
	private static final String MSG_INVALID_USER = "Could not find a user matching the supplied credentials";
	
	@Autowired
	private UserRepository userRepo;
		
	
	public UserServiceImpl() {
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserDetails user = userRepo.findByUsername(username.toLowerCase());
		
		if (user == null) {
			throw new UsernameNotFoundException(MSG_INVALID_USER);
		}
		
		return user;
	}
	
	/* (non-Javadoc)
	 * @see org.websure.demo.UserService#createUser(org.websure.demo.User)
	 */
	@Override
	@Transactional
	public User createUser(User newUser) {
		
		newUser.setPassword(new StandardPasswordEncoder()
				.encode(newUser.getPassword()));
		newUser.setAccountNonExpired(true);
		newUser.setAccountNonLocked(true);
		newUser.setCredentialsNonExpired(true);
		newUser.setEnabled(true);
		newUser.grantRole(UserRole.USER);
		return userRepo.save(newUser);
	}
	
	@Override
	@Transactional
	public User findUser(Integer userId) {

		return userRepo.findOne(userId);
	}	


	@Override
	public User findUserByUsername(String username) {

		return userRepo.findByUsername(username);
	}	
	
	public void setUserRepo(UserRepository userRepo) {
	
		this.userRepo = userRepo;
	}



}
