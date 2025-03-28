/**
 * 
 */
package com.example.bb_pets_adoption.auth.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.bb_pets_adoption.account_management.Model.User;
import com.example.bb_pets_adoption.account_management.Repository.UserRepository;

/**
 * CustomOAuth2UserService extends DefaultOAuth2UserService to load the user
 * during OAuth2 authentication
 * 
 * It checks if the user exists in the database and creates a new user
 * entry if the user does not exist
 * 
 * - DefaultOAuth2User object, we are effectively converting the OAuth2 user information 
 *   into a format that Spring Security can understand and use for authentication purposes
 */
@Service
public class CustomOAuth2UserService  extends DefaultOAuth2UserService{

	// vars
	 
	 private final UserRepository userRepository;
	 
	 
	 // Constructor
	 @Autowired
	    public CustomOAuth2UserService(UserRepository userRepository) {
	        this.userRepository = userRepository;
	        System.out.println("Custom User Service Initiated");
	    }
	 
	 /**
	 * Loads the user from the OAuth2UserRequest and uses the user's information.
	 * If the user does not exist in the database, it creates a new user.
	 * 
	 * @param userRequest the OAuth2UserRequest containing the user details
	 * @return the OAuth2User containing the user's information
	 * @throws OAuth2AuthenticationException handles errors that may occur during authentication
	 */
	 @Override
	 public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		 
		 
		 System.out.println("Custom User service initiating");
		 
		 OAuth2User oAuth2User = super.loadUser(userRequest);
		 
		 // Get the registration ID to determine the provider to determine which OAuth2 provider (Google, Facebook, Auth0) is being used for the current authentication request
		 String registrationId = userRequest.getClientRegistration().getRegistrationId();
	     String email = oAuth2User.getAttribute("email");   // extract email

	 
	     System.out.println("User registering with Goole");
		 
	     // Check if user exists in the repository
	     Optional<User> foundUser = userRepository.findByEmail(email);
	     
	     User user;
	     if (foundUser.isPresent()) {
	         user = foundUser.get();
	          System.out.println("User found in database regitered with " + registrationId);

	     } else {
	         // Create a new user if not found
	         user = new User();
	         user.setEmail(email);
	         user.setName(oAuth2User.getAttribute("name"));
	         user.setLastName(oAuth2User.getAttribute("family_name"));   // family_name is typically used for last name in OAuth2 responses
	         user.setRoles(List.of("ROLE_ADOPTER"));
	         userRepository.save(user); // Save the user
	          System.out.println("User regitered with " + registrationId);

	        }
	     
	     //return an OAuth2User object that Spring Security can use for authentication and authorization within the application
	     // Authority is used to define the user's rights within the app.
	     return new DefaultOAuth2User(
	                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADOPTER")), // Grant the user a initial single role
	                oAuth2User.getAttributes(),  // Set the user's attributes(all the attributes)
	                "email"); // Specify the key used tas user's unique identifier in the set attributes
	    }
        
}
