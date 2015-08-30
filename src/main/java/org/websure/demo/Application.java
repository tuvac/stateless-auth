package org.websure.demo;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.websure.demo.domain.User;
import org.websure.demo.domain.UserRepository;
import org.websure.demo.domain.UserRole;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new StandardPasswordEncoder();
	}

	@Bean(name = "messageSource")
	public ReloadableResourceBundleMessageSource messageSource() {

		ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
		messageBundle.setBasename("classpath:messages");
		messageBundle.setDefaultEncoding("UTF-8");
		messageBundle.setFallbackToSystemLocale(false);
		return messageBundle;
	}

	@Bean
	public InitializingBean insertDefaultUsers() {

		return new InitializingBean() {

			@Autowired
			private UserRepository userRepo;

			@Override
			public void afterPropertiesSet() throws Exception {

				User user = new User();
				user.setUsername("somone@onethe.net");
				user.setNewPassword("test1234");
				user.setPassword(new StandardPasswordEncoder().encode(user.getNewPassword()));
				user.setAccountNonExpired(true);
				user.setAccountNonLocked(true);
				user.setCredentialsNonExpired(true);
				user.setEnabled(true);
				user.grantRole(UserRole.USER);
				userRepo.save(user);

			}

		};

	}

}
