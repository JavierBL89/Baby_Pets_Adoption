/**
 * 
 */
package com.example.bb_pets_adoption.auth.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.bb_pets_adoption.auth.repository.UserRepository;
import com.example.bb_pets_adoption.auth.model.User;
/**
 * 
 * Controller class for User entity
 * 
 * - Uses Logger to log messages with information for debugging
 * - Uses ResponsEntity to customize the HTTP response with STATUS, HEADERS, AND BODY
 */

@RestController
@RequestMapping("/auth") // make all endpoints start with this prefix for authentication
public class UserController {

	// create an instance of Logger
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	// endpoints
	
	
	/**
     * Endpoint for user registration
     *
     * @param user The user object containing registration details
     * @return A message indicating whether if registration was successful or not
     */
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		// log
		logger.info("Registering user with email: {}", user.getEmail());
		
		// Check if the email is already registered
		Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
		
		if(!foundUser.isPresent()) {                                        
			// If email is not found, encode the password and save the new user
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRoles(List.of("ROLE_USER"));
			userRepository.save(user); // save new user
			
			//log
			logger.info("Register successful for user with email: {}", user.getEmail());
			return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
		} else {                                                         
			// If email is found, log the error and return a message
			//log
            logger.error("Registration failed for user with email: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The email provided is already registered");

		}
		
		
	}
	
	// Endpoint for user login
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody User user) {
		//log
		logger.info("Attempting to log in user with email: {}", user.getEmail());
		
		Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
		if(foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword())) {
			//log
			logger.info("Login successful for user with email: {}", user.getEmail());
			return ResponseEntity.status(HttpStatus.CREATED).body("Login successful");
		} else {
			//log
            logger.error("Login failed for user with email: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
		}
	}
}
