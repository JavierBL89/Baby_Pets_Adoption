/**
 * 
 */
package com.example.bb_pets_adoption.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.bb_pets_adoption.auth.repository.UserRepository;
import com.example.bb_pets_adoption.email.EmailService;
import com.example.bb_pets_adoption.auth.model.User;

/**
 * Controller class for authentication funtionalities, and password reset
 * 
 * Class handles registration, login, password reset requests, and reset password emails.
 * 
 * The class:
 * - Uses Logger to log messages with information for debugging
 * - Uses ResponseEntity to customize the HTTP response with STATUS, HEADERS, AND BODY
 */

@RestController
@RequestMapping("/auth") // make all endpoints start with this prefix for authentication
public class AuthController {

	// create an instance of Logger
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Autowired
	private EmailService emailService;
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
			user.setToken(null);
			userRepository.save(user); // save new user
			
			//log
			logger.info("Register successful for user with email: {}", user.getEmail());
			return ResponseEntity.status(201).body("User registered successfully");
		} else {                                                         
			// If email is found, log the error and return a message
			//log
            logger.error("Registration failed for user with email: {}", user.getEmail());
            return ResponseEntity.status(409).body("The email entered is already registered");

		}
		
		
	}
	
	
	/**
     * Endpoint for user login.
     *
     * @param user - the user object with login details
     * @return a message indicating whether if login was successful or not
     */
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody User user) {
		//log
		logger.info("Attempting to log in user with email: {}", user.getEmail());
		
		Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
		if(foundUser.isPresent() && passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword())) {
			//log
			logger.info("Login successful for user with email: {}", user.getEmail());
			return ResponseEntity.status(201).body("Login successful");
		} else {
			//log
            logger.error("Login failed for user with email: {}", user.getEmail());
            return ResponseEntity.status(401).body("Invalid email or password");
		}
	}
	
	
	/**
	 * Endpoint for forgotten password
	 * 
	 * - Method return type uses a Map< String, String> to decople 
	 *   a payload request from a domain model(User model) following best practices
	 *   
	 * - Uses UUID to generate a random ID(token) and added to reset password link, 
	 *   This token is also saved into User document which will be used to identify 
	 *   the user in order to update the password on the next step of the process
	 * 
	 * @param request - the request with the user's email
     * @return a response indicating success or failure
	 * **/
	@PostMapping("/forgot_password")
	public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request){
		
		String email = request.get("email");   // retreive email from request
		// map necessary to match method return type. Allows a response with message and status
	    Map<String, String> response = new HashMap<>(); 
	    
		//log
		logger.info("Checking email provided is registered: {}", email);
		Optional<User> foundUser = userRepository.findByEmail(email);
		
		if(foundUser.isPresent()) {
			
			User user = foundUser.get(); // cast to a user object
			
			logger.info("Email provided found as registered: {}", email);
			 // generate a random ID unique
			 String token = UUID.randomUUID().toString();
			 // set user token and save
			 user.setToken(token);
			 userRepository.save(user);
			 
			 // reset password url + unique token
			 String passwordResetUrl = "http://localhost:3000/auth/reset_password?token=" + token;
			 // send email following the EmailService object's format   to - subject - body  
	         emailService.sendEmail(user.getEmail(), "Password Reset Request", "To reset your password, click the link below:\n" + passwordResetUrl);

			//log
			logger.info("Found email. Sending password reset email to resgistered user email address: {}", user.getEmail());
			
			response.put("message", "We have sent you an email to reset your password. Check you inbox");
			return ResponseEntity.status(201).body(response);
			
		}else {
			//log
            logger.error("Email not found. Please ensure correct email addres or signUp: {}", email);
            
			response.put("message", "Invalid email or email not registered");
            return ResponseEntity.status(401).body(response);
		}
	}
	
	   
	   /***
	   * Endpoint to reset password
	   * 
	   * Method return type uses a Map< String, String> to decople 
	   * a payload request from a domain model(User model) following best practices
	   *
	   * Retreive the token passed in reset request to identify the user in database
	   * 
	   * @param request contains the token and new password
       * @return a response indicating success or failure
	   * **/
		@PostMapping("/reset_password")
		public ResponseEntity<Map<String, String>> passwordReset(@RequestBody Map<String, String> resetRequest){
			
			
			String token = resetRequest.get("token");          // retreive token from request
		    String newPassword = resetRequest.get("password"); // retreive new password from request
		   
		    Optional<User> foundUser = userRepository.findByToken(token); // identify user in database using the token
		    
		    // map necessary to match method return type. Allows a response with message and status
		    Map<String, String> response = new HashMap<>(); 
		    
			//log
			logger.info("Updating password: {}");
			
			if(foundUser.isPresent()) {
				User user = foundUser.get();
	            user.setPassword(passwordEncoder.encode(newPassword));
	            user.setToken(null);  // reset token to null
	            userRepository.save(user);
				//log
				logger.info("Found email. Sending password reset email to resgistered user email address: {}", user.getEmail());
				
				response.put("message", "Password successfully updated");
				return ResponseEntity.status(201).body(response);
				
			}else {
				//log
	            logger.error("token not found or expired token", token);
	            
	            response.put("message", "Invalid or expired token.");
	            return ResponseEntity.status(401).body(response);
			}
		}
		

}
