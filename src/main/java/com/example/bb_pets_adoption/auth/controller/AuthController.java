/**
 * 
 */
package com.example.bb_pets_adoption.auth.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
// @RequestMapping("/auth") // make all endpoints start with this prefix for authentication
public class AuthController {

	// create an instance of Logger
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	 UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder;
	
	private EmailService emailService;
	
	 private JwtUtil jwtUtil; // Utility class to generate and validate JWT tokens
	
	
	 /**
	  * Constructor injection for PasswordEncoder to solve circular references issue
	  * */
	 @Autowired
	 public AuthController(@Lazy PasswordEncoder passwordEncoder, @Lazy EmailService  emailService, @Lazy JwtUtil jwtUtil ) {
	        this.passwordEncoder = passwordEncoder;
	        this.emailService = emailService;
	        this.jwtUtil  = jwtUtil;
	        
	    }
	 
	// endpoints
	
	
	/**
     * Endpoint for user registration using their email and personal details rather then social platform
     * 
     * The registerdBy field is set to  "email" so we can know this in frontend application to set the React app state accordingly 
     * and perform the logout using the appropiate endpoint
     * 
     * @param user The user object containing registration details
     * @return A message indicating whether if registration was successful or not
     */
	@PostMapping(value="/auth/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		// log
		logger.info("Registering user with email: {}", user.getEmail());
		
		// grab user email from request
		String email = user.getEmail();
		
		// Check if the email is already registered
		Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
		
		if(!foundUser.isPresent()) {                   
		    // encode the password before saving the new user
	        String encodedPassword = passwordEncoder.encode(user.getPassword());
	        user.setPassword(encodedPassword);
			user.setRegisteredBy("email");
			user.setRoles(List.of("ROLE_USER"));
			user.setToken(null);
			userRepository.save(user); // save new user
			
			// generate jwt
			String token = jwtUtil.generateToken(user.getEmail());
			 //verification account url + user email + unique token
			 String accountConfirmationUrl = "http://localhost:8080/auth/verify_account?email="+ user.getEmail() + "&token=" + token;
			 
			 // send email following the EmailService object's format   to - subject - body  
	         emailService.sendEmail(user.getEmail(), "Account verification request", 
	        		 "please click the link to verify you email to complete your registration\n " + accountConfirmationUrl);
	         
			//log
			logger.info("Registrartion need account verification from ", user.getEmail());
			return ResponseEntity.status(201).body("User registered successfully");
		} else {                                                         
			// if email is found, log the error and return a message
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
	@PostMapping("/auth/login")
	public ResponseEntity<?> loginUser(@RequestBody User user) {
		//log
		logger.info("Attempting to log in user with email: {}", user.getEmail());
		
		
		Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
		if(foundUser.isPresent() && passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword())) {
			
			User registereduser = foundUser.get();
			
			String token = jwtUtil.generateToken(user.getEmail());  // generate jwt
			Map<String, String> response = new HashMap<>();         // create Map collection instanse to send messages in response body
								
			response.put("token", token);                           // send token as request reponse message
			response.put("message", "Login successful");            // set response message
			
			// update user token and save changes	   
			registereduser.setToken(token);
			userRepository.save(registereduser);
			logger.info("PUTAAA " + user.getToken());
			logger.info("PUTOOO " + token);
		    response.put("registeredBy", foundUser.get().getRegisteredBy());  // send it with response message
			response.put("userName", foundUser.get().getName());
			//log
			logger.info("Login successful for user with email: {}", user.getEmail());
			return ResponseEntity.status(201).body(response);
		} else {
			//log
            logger.error("Login failed for user with email: {}", user.getEmail());
            return ResponseEntity.status(401).body("Invalid email or password");
		}
	}
	
	/**
	 * Endpoint for verifying a user's account
	 *
	 * This method is responsible for verifying a user's account using the token and email provided
	 * in the request parameters. 
	 * The process involves:
	 * - Checking the session and retrieving the user by email
	 * -Validating the token, and updating the user's token in the database.
	 * 
	 * After successful verification, the user is redirected to the home page with the token in the URL
	 *
	 * @param request - the HttpServletRequest object that contains the request the client made to the server
	 * @param response - the HttpServletResponse object that contains the response the server sends to the client
	 * @return ResponseEntity<String> - a response entity indicating the result of the account verification
	 * @throws IOException - if an input or output exception occurs during the redirection process
	 */
	@GetMapping("/auth/verify_account")
    public ResponseEntity<String> verifyAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
        // create a new session if it does not exists
        HttpSession session = request.getSession(true);
        if (session == null) {
            session.isNew();
        }
        
        // retrieve email and token from request parameters
		String email = request.getParameter("email");
		String token = request.getParameter("token");     
	    
	    
	    // log the verification attempt
	    logger.info("Attempting to verify account for email: {}", email);
		
	    // identify user in database using the email
	    Optional<User> foundUser = userRepository.findByEmail(email);
	    
		String redirectUrl = "";
		if(foundUser.isPresent()) {
			User user = foundUser.get();
            user.setToken(token);  // reset token to null
            userRepository.save(user); // save changes
            
			// log successful verification
			logger.info("Account verified successfuly. Redirecting to login page... ");
			// set redirect
			redirectUrl = "http://localhost:3000/login";
		
		}else {
			
			// log error if user is not found or token is invalid
            logger.error("token not found or expired token", token);
            // set redirect
            redirectUrl = "http://localhost:3000/404";   // user not found
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account verification failed. Invalid token or email.");
		}
		
		// handle redirect response and handle potential IO exceptions
	    try {
		    response.sendRedirect(redirectUrl);
            return ResponseEntity.status(201).body("Account Verification successful");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Account Verification failed");
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
	@PostMapping("/auth/forgot_password")
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
			// generate jwt
			String token = jwtUtil.generateToken(user.getEmail()); 
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
		@PostMapping("/auth/reset_password")
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
		
		/***
		* Endpoint to logout the session
		* 
		* Method handles logging out from the application's session and clears the security context
		* 
		* Invalidating the session and clearing the security context is standard practice in Spring Security 
		* to ensure that the user's session and authentication information are completely cleared out when they log out
		* This ensures that the user is fully logged out and any session data is removed
		* 
		* Method return type uses ResponseEntity<String> to allow to customize the HTTP response 
		* with a status code, headers, and body
		*
		* @param request - provides request information for HTTP servlets
	    * @return  response - provides HTTP-specific functionality in sending a response
		* @throws IOException 
		* **/
		@PostMapping("/auth/logout")
	    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
			
			// retreive token from query passed and handle null
			String token = request.getParameter("token");
			if (token == null || token.isEmpty()) {
	            // Log missing token
				logger.info("Logout failed: missing token");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Logout failed: missing token");
	        }
			
			// find user by token
			Optional<User> foundUser = userRepository.findByToken(token);
			
			if(foundUser.isPresent()) {
				User user = foundUser.get();
				user.setToken(null);
				userRepository.save(user);
				
				//  checks if there is an existing HTTP session and invalidates it if found. 'false' prevents creating a new session
		        HttpSession session = request.getSession(false);
		        if (session != null) {
		            session.invalidate();
		        }
		        
		        // clear the authentication from security associated with the current thread
		        SecurityContextHolder.clearContext();
		        
		        // log logot success
	            logger.info("User Logged out successfully");           
		        return ResponseEntity.status(201).body("Logout successful");
			} else {
				
				//log logout failure
	            logger.error("token not found or expired token", token);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Google logout failed");

			}
	      
	    }
		
		/***
		   * Endpoint to logout the session
		   * 
		   * Method handles logging out from your application's session and clears the security context
		   * Method logs out the user from Google, and redirects them to the Google logout endpoint after invalidating their session

		   * Invalidating the session and clearing the security context is standard practice in Spring Security 
		   * to ensure that the user's session and authentication information are completely cleared out when they log out
		   * This ensures that the user is fully logged out and any session data is removed
		   * 
		   * Method return type uses ResponseEntity<String> to allow to customize the HTTP response 
		   * with a status code, headers, and body
		   *
		   * @param request - provides request information for HTTP servlets
	       * @return  response - provides HTTP-specific functionality in sending a response
		   * @throws IOException 
		   * **/
		@PostMapping("/auth/google_logout")
	    public ResponseEntity<String> googleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
			
	        // invalidate the session if it exists. 'false' prevents creating a new session
	        HttpSession session = request.getSession(false);
	        if (session != null) {
	            session.invalidate();
	        }

	        // clear the authentication from security context
	        SecurityContextHolder.clearContext();
	        
	  
	        	// redirect to Google's logout URL
		        String googleLogoutUrl = "https://accounts.google.com/Logout";
	            try {
	                response.sendRedirect(googleLogoutUrl);
	                return ResponseEntity.status(201).body("Logout successful");
	            } catch (IOException e) {
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Google logout failed");
	            }
	   
	        

	    }

}
