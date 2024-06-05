/**
 * 
 */
package com.example.bb_pets_adoption.auth.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.bb_pets_adoption.auth.model.User;
import com.example.bb_pets_adoption.auth.repository.UserRepository;

/**
 * CustomOAuth2UserService extends DefaultOAuth2UserService to load the user
 * during OAuth2 authentication. 
 * 
 * It handles multiple providers like Google, Facebook, and Auth0 making use of the registrationId 
 * to distinguish between different providers and process the user information accordingly.
 * 
 * It checks if the user exists in the database and creates a new user
 * entry if the user does not exist.
 * 
 * - DefaultOAuth2User object, we are effectively converting the OAuth2 user information 
 *   into a format that Spring Security can understand and use for authentication purposes.
 */
@Service
public class CustomOAuth2UserService  extends DefaultOAuth2UserService{

	// vars
	 private final UserRepository userRepository;
	 private final PasswordEncoder passwordEncoder;
	 
	 
	 // Constructor
	 public CustomOAuth2UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
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
		 
		 OAuth2User oAuth2User = super.loadUser(userRequest);
		 
		 // Get the registration ID to determine the provider to determine which OAuth2 provider (Google, Facebook, Auth0) is being used for the current authentication request
		 String registrationId = userRequest.getClientRegistration().getRegistrationId();
	     String email = null;
	     String name = null;

	     // Extract user information based on provider
	     if ("facebook".equals(registrationId)) {
	          email = oAuth2User.getAttribute("email");
	          name = oAuth2User.getAttribute("name");
	     } else if ("google".equals(registrationId)) {
	          email = oAuth2User.getAttribute("email");
	          name = oAuth2User.getAttribute("name");
	      } else if ("auth0".equals(registrationId)) {
	          email = oAuth2User.getAttribute("email");
	          name = oAuth2User.getAttribute("name");
	      } else {
	          throw new OAuth2AuthenticationException("Unsupported registration provider: " + registrationId);
	      }
		 
	     // Check if user exists in the repository
	     Optional<User> optionalUser = userRepository.findByEmail(email);
	     User user;
	     if (optionalUser.isPresent()) {
	         user = optionalUser.get();
	     } else {
	         // Create a new user if not found
	         user = new User();
	         user.setEmail(email);
	         user.setName(name);
	         user.setLastName(oAuth2User.getAttribute("last_name"));
	         user.setPassword(passwordEncoder.encode("default_password")); // Set a default password
	         user.setRoles(List.of("ROLE_ADOPTER"));
	         userRepository.save(user); // Save the user
	        }
	     
	     //return an OAuth2User object that Spring Security can use for authentication and authorization within the application
	     // Authority is used to define the user's rights within the app.
	     return new DefaultOAuth2User(
	                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADOPTER")), // Grant the user a initial single role
	                oAuth2User.getAttributes(),  // Set the user's attributes(all the attributes)
	                "email"); // Specify the key used tas user's unique identifier in the set attributes
	    }
        
}
