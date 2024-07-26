/**
 * 
 */
package com.example.bb_pets_adoption.account_management.Controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bb_pets_adoption.account_management.Repository.UserRepository;
import com.example.bb_pets_adoption.account_management.Service.AccountManagementServiceImpl;
import com.example.bb_pets_adoption.account_management.Model.User;
import com.example.bb_pets_adoption.pet_listing.controller.PetController;

/***
 * Exposes REST endpoints for managing auser data
 * 
 * **/
@RestController
@RequestMapping("/account_management")
public class AccountManagementController {

	    // vars
	    UserRepository userRepository;
	    AccountManagementServiceImpl accountManagementServiceImpl;
	    
	    // create an instance of Logger
		private static final Logger logger = LoggerFactory.getLogger(PetController.class);
			
		
	    // Constructor for dependancies injection
	    @Autowired
	    public AccountManagementController(UserRepository userRepository, AccountManagementServiceImpl accountManagementServiceImpl) {
	    	this.userRepository = userRepository;
	    	this.accountManagementServiceImpl = accountManagementServiceImpl;
	    }
	    
	    
	    /***
	     * Endpoint receives a PUT request for user data update
	     * **/
	    @PutMapping("/update_details")
	    public ResponseEntity<?> updateProfile(    
	    	@RequestParam(value= "token", required = false) String token,
            @RequestParam(value= "name", required = false) String name,
            @RequestParam(value= "lastName", required = false) String lastName,
            @RequestParam(value= "location", required = false) String location) {
	    	

	        // handle null value token
	   	    if (token == null) {
	   	        logger.error("Authorization token is missing");
	   	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing");
	   	    }
	   	    
	   	  // try and catch any errors during application update process
	   	    try {
	   	        // if user is authenticated proceed to update application
	   	 		if (accountManagementServiceImpl.authenticateUserByToken(token)) {
                     Optional<User> foundUser = userRepository.findByToken(token);
                     
                     if(foundUser.isPresent()){
                         User user = foundUser.get();                       
    	   	 			  // delegate operation to service @params (User user String name, String lastName, String location)
    	   	 		      accountManagementServiceImpl.updateProfile(user, name, lastName, location);     	   	 		      
                     }else {          	 
                    	 logger.info("User not found");
     	   	 			return ResponseEntity.status(404).body("User not found");
                     }
                		 
	   	 		}else { 			
	   	 			logger.info("Unauthorized user");
	   	 			return ResponseEntity.status(403).body("Unauthorized user");
	   	 		}   
	   	 		
	   	    } catch (Exception e) {
	   	        logger.error("Error updating user details", e);
	   	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user details: " + e.getMessage());
	   	    }
	   	    
	   	    logger.info("User details successfully updated");
	   	    return ResponseEntity.status(200).body("User details successfully updated");
	    }
	    

	    /***
	     * Endpoint receives a PUT request for user data update
	     * **/
	    @PutMapping("/update_email")
	    public ResponseEntity<?> updateEmailAddress(    
	    	@RequestParam(value= "token", required = false) String token,
            @RequestParam(value= "email", required = false) String email){
	    	

	        // handle null value token
	   	    if (token == null) {
	   	        logger.error("Authorization token is missing");
	   	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing");
	   	    }
	   	    
	   	  // try and catch any errors during application update process
	   	    try {
	   	        // if user is authenticated proceed to update application
	   	 		if (accountManagementServiceImpl.authenticateUserByToken(token)) {
                     Optional<User> foundUser = userRepository.findByToken(token);
                     
                     if(foundUser.isPresent()){
                        User user = foundUser.get();                       
                 	    // delegate operation to service @params (User user, String email)
   	   	 		        accountManagementServiceImpl.updateEmailAddress(user, email);    	 		      
                     }else {          	 
                    	 logger.info("User not found");
     	   	 			return ResponseEntity.status(404).body("User not found");
                     }                                   
	   	 		 
	   	 		}else { 			
	   	 			logger.info("Unauthorized user");
	   	 			return ResponseEntity.status(403).body("Unauthorized user");
	   	 		}   	 		
	   	    } catch (Exception e) {
	   	        logger.error("Error updating email address", e);
	   	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating email address: " + e.getMessage());
	   	    }
	   	    
	   	    logger.info("Email address successfully updated");
	   	    return ResponseEntity.status(200).body("Email address successfully updated");
	   	    
	    }

	    
  
	    /***
	     * Endpoint receives a PUT request for user data update
	     * 
	     * 
	     * **/
	    @DeleteMapping("/delete_user")
	    public ResponseEntity<?> deleteUser(    
	    	@RequestParam(value= "token", required = false) String token) {
	    	
	        // handle null value token
	   	    if (token == null) {
	   	        logger.error("Authorization token is missing");
	   	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing");
	   	    }
	   	    
	   	  // try and catch any errors during application update process
	   	    try {
	   	        // if user is authenticated proceed to update application
	   	 		if (accountManagementServiceImpl.authenticateUserByToken(token)) {
                     Optional<User> foundUser = userRepository.findByToken(token);
                     
                     if(foundUser.isPresent()){
                        User user = foundUser.get();                       
                 	    // delete user @params (User user, String email)
   	   	 		        accountManagementServiceImpl.deleteUser(user);    	 		
   	   	 		        
                     }else {          	 
                    	 logger.info("User not found");
     	   	 			return ResponseEntity.status(404).body("User not found");
                     }                                   
	   	 		 
	   	 		}else { 			
	   	 			logger.info("Unauthorized user");
	   	 			return ResponseEntity.status(403).body("Unauthorized user");
	   	 		}   	 		
	   	    } catch (Exception e) {
	   	        logger.error("Error deleting profile", e);
	   	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating email address: " + e.getMessage());
	   	    }
	   	    
	   	    logger.info("Profile successfully deleted");
	   	    return ResponseEntity.status(200).body("Profile successfully deleted");
	   	    
	    }

	   	 		
}
