package com.example.userservice;

import com.example.userservice.domain.Role;
import com.example.userservice.domain.User;
import com.example.userservice.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayDeque;

import static java.sql.Types.NULL;

@SpringBootApplication
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}



	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();

	}

	@Bean
	CommandLineRunner run(UserService userService)
	{
		return args ->
		{
			userService.saveRole(new Role(NULL,"ROLE_USER"));
			userService.saveRole(new Role(NULL,"ROLE_MANAGER"));
			userService.saveRole(new Role(NULL,"ROLE_ADMIN"));
			userService.saveRole(new Role(NULL,"ROLE_SUPER_ADMIN"));


//			userService.saveUser(new User(NULL, "김현국","boris0815@naver.com","bms28292", new ArrayDeque<>()));
//			userService.saveUser(new User(NULL, "이현국","boris0816@naver.com","bms28292", new ArrayDeque<>()));
//			userService.saveUser(new User(NULL, "서현국","boris0817@naver.com","bms28292", new ArrayDeque<>()));
//			userService.saveUser(new User(NULL, "박현국","boris0818@naver.com","bms28292", new ArrayDeque<>()));
//			userService.saveUser(new User(NULL, "최현국","boris0819@naver.com","bms28292", new ArrayDeque<>()));

			userService.addNewUser(new User(NULL, "김현국","boris0815@naver.com","bms28292", new ArrayDeque<>()));
			userService.addNewUser(new User(NULL, "이현국","boris0816@naver.com","bms28292", new ArrayDeque<>()));
			userService.addNewUser(new User(NULL, "서현국","boris0817@naver.com","bms28292", new ArrayDeque<>()));
			userService.addNewUser(new User(NULL, "박현국","boris0818@naver.com","bms28292", new ArrayDeque<>()));
			userService.addNewUser(new User(NULL, "최현국","boris0819@naver.com","bms28292", new ArrayDeque<>()));



			userService.updateRoletoUser("boris0815@naver.com", "ROLE_ADMIN");



		};
	}

}
