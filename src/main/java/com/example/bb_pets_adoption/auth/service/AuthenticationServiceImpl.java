/**
 * 
 */
package com.example.bb_pets_adoption.auth.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.bb_pets_adoption.auth.model.User;
import com.example.bb_pets_adoption.auth.repository.UserRepository;
import com.example.bb_pets_adoption.search.controller.SearchController;


/**
 * 
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService{

	@Autowired
	UserRepository userRepository;
	
	
	// create an instance of Logger
     private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
			
     
     /**
      * 
      * 
      * **/
	@Override
	public boolean authenticate(String token) {
		
		
		if(token  == null || token.isEmpty()) {
			logger.info("Token passed for authentication is null or empty.");
			return false;
		}
		
		Optional<User> foundUser = this.userRepository.findByToken(token);
		
		if(foundUser.isPresent()) {	
			logger.info("User was authenticated");
			return true;
			
		} else {	
            logger.error("User could not be authenticated");
            return false;
		}
	}

}
