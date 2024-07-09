/**
 * 
 */
package com.example.bb_pets_adoption.account_management.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bb_pets_adoption.account_management.Model.User;
import com.example.bb_pets_adoption.account_management.Repository.UserRepository;
import com.example.bb_pets_adoption.auth.service.AuthenticationServiceImpl;
import com.example.bb_pets_adoption.pet_listing.controller.PetController;

/**
 * 
 */
@Service
public class AccountManagementServiceImpl implements AccountManagementService{
	
	
    // vars
	AuthenticationServiceImpl authenticationServiceImpl;
	UserRepository userRepository;
	
	// create an instance of Logger
	private static final Logger logger = LoggerFactory.getLogger(PetController.class);
		
	
	/**
	 * Constructor for dependencies injection
	 * 
	 * **/
	@Autowired
	public AccountManagementServiceImpl(AuthenticationServiceImpl authenticationServiceImpl, UserRepository userRepository) {
		this.authenticationServiceImpl = authenticationServiceImpl;
		this.userRepository = userRepository;
	}
	
	
	
	@Override
	public void updateProfile(User user, String name, String lastName, String location) throws Exception {

       try {
    	   user.setName(name);
    	   user.setLastName(lastName);
    	   user.setLocation(location);
    	   userRepository.save(user);
    	   
    	   logger.info("User data successfully updated. User id " + user.getUserId());
       }catch(Exception e) {
    	   
    	   logger.error("An error ocured and user data could not be updated" + e.getMessage());
    	   throw new Exception("An error ocured and user data could not be updated. " + e.getMessage());
    	   
       }
		
	}

	
	/**
	 * Method responsible for updating the user email address
	 * 
	 * @param {User} user - the user object to be updated
	 * @param {String} email - the new email address 
	 * @throws Exception - message with any error occured during process
	 * **/
	@Override
	public void updateEmailAddress(User user, String email) throws Exception {
		
		 try {
	    	   user.setEmail(email);
	    	   userRepository.save(user);
	    	   
	    	   logger.info("User email address successfully updated. User id " + user.getUserId());
	       }catch(Exception e) {
	    	   
	    	   logger.error("An error ocured and user email address could not be updated" + e.getMessage());
	    	   throw new Exception("An error ocured and user email address could not be updated. " + e.getMessage());
	    	   
	       }
		
	}

	/**
	 * Authenticate user by current session token
	 * Utilizes Authentication Service
	 * 
	 * @param {String} token
	 * @return boolean
	 * */
	@Override
	public boolean authenticateUserByToken(String token) {

		return authenticationServiceImpl.authenticate(token);
	}
	

}
