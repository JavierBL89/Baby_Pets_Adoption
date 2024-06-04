/**
 * 
 */
package com.example.bb_pets_adoption.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.bb_pets_adoption.repository.UserRepository;
import com.example.bb_pets_adoption.model.User;
/**
 * 
 */

@RestController
@RequestMapping("/auth") // Base URL for authentication endpoints
public class UserController {

	// create an instance of Logger
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	// endpoints
	
	
	// Endpoint for user registration
	@PostMapping("/register")
	public String registerUser(@RequestBody User user) {
		// log
		logger.info("Registering user with email: {}", user.getEmail());
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(List.of("ROLE_USER"));
		//log
		logger.info("User registered successfully with email: {}", user.getEmail());
		
		return "User registered successfully";
	}
	
	// Endpoint for user login
	@PostMapping("/login")
	public String loginUser(@RequestBody User user) {
		//log
		logger.info("Attempting to log in user with email: {}", user.getEmail());
		
		User foundUser = userRepository.findByEmail(user.getEmail());
		if(foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
			//log
			logger.info("Login successful for user with email: {}", user.getEmail());
			return "Login successful";
		} else {
			//log
            logger.error("Login failed for user with email: {}", user.getEmail());
			return "Invalid email or password";
		}
	}
}
