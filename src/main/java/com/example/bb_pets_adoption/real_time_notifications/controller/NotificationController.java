/**
 * 
 */
package com.example.bb_pets_adoption.real_time_notifications.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bb_pets_adoption.account_management.Model.User;
import com.example.bb_pets_adoption.account_management.Repository.UserRepository;
import com.example.bb_pets_adoption.adoption.model.AdoptionApplication;
import com.example.bb_pets_adoption.pet_listing.controller.PetController;
import com.example.bb_pets_adoption.pet_listing.model.PetList;
import com.example.bb_pets_adoption.real_time_notifications.Model.Notification;
import com.example.bb_pets_adoption.real_time_notifications.service.NotificationServiceImpl;

/**
 * 
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {

	
	// vars
	private SimpMessagingTemplate messageTemplate;
	NotificationServiceImpl notificationServiceImpl;
	private UserRepository userRepository;
	
	// create an instance of Logger
	private static final Logger logger = LoggerFactory.getLogger(PetController.class);
	
	
	// Constructor for dependency injection
	@Autowired
	public NotificationController(SimpMessagingTemplate messageTemplate, NotificationServiceImpl notificationServiceImpl,
			UserRepository userRepository) {
		
		this.messageTemplate = messageTemplate;
		this.notificationServiceImpl = notificationServiceImpl;
		this.userRepository = userRepository;
	}
	
	
	/***
	 * Endppoint to recieves a PUT request
	 * 
	 * It is to handle notifications when they are marked as viewed by the user
	 * 
	 *@param {String} token - the authentication token for the current session
	 *@param {String} notificationId - the string representation of the notification to be handled
	 *@return ResponseEntity - the http response with he appropiate status and message
	 * **/
	 @PutMapping("/markAsViewed")
	 public ResponseEntity<?> markDroppedApplicationAsViewed(
			 @RequestParam(value="token" ,required=false) String token,
			 @RequestParam(value="notificationId" ,required=false) String notificationIdString
			 ) {
		 
		 // handle null value token
		 if (token == null) {
		      logger.error("Authorization token is missing");
		      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing");
		 }
		        
		// if user is authenticated proceed to create a a new pet and assocciate this with the user pet listings
	    if (notificationServiceImpl.authenticateUserByToken(token)) {				   
		     try {
		    	 // mark as viewed and delete it
	            notificationServiceImpl.markAsViewedAndDelete(notificationIdString);
			    return ResponseEntity.status(200).body("Notification marked as viewed.");
		     }catch(Exception e) {		 
			      new Exception(e.getMessage());	
			      return ResponseEntity.status(500).body("A Server error occured." + e.getMessage());		      
		     }	     
		     
	    }else{
			logger.info("Unauthorized user");
           return ResponseEntity.status(403).body("Unauthorized");
	    }
	}
	 
	 

	 /**
	 * Endpoint handles a GET request to fetch all the notifications with "pending" status realated to a user
	 * 
	 * It ensures user authentication before performing any databases operation
	 * 
     *@param {String} token - the authentication token of the current session
     *@return ResponseEntity - the http response with he appropiate status and message
	 * **/
	@GetMapping("/pending")
	public ResponseEntity<?> getUnreadNotifications(
			@RequestParam(value="token" ,required=false) String token){
		
		 // handle null value token
		 if (token == null) {
		      logger.error("Authorization token is missing");
		      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing");
		 }
		 		 
		// instantiate a hashmap to store and send data as http response
		Map<String, Object> response = new HashMap<>();
		
		// if user is authenticated proceed to create a a new pet and assocciate this with the user pet listings
	    if (notificationServiceImpl.authenticateUserByToken(token)) {
				   
		     try {
		    	 Optional<User> foundUser = userRepository.findByToken(token);
		    	 if(foundUser.isPresent()) {
		    		 
		    		 List<Notification> unviewedListingspNotifications = notificationServiceImpl.getPendingListingsNotifications(foundUser.get().getUserId());
		    		 List<AdoptionApplication> unviewedAppStatusNotifications = notificationServiceImpl.getPendingStatusNotifications(foundUser.get().getUserId());
		    		 
		    		 response.put("unviewedListingspNotifications", unviewedListingspNotifications);
		    		 response.put("unviewedAppStatusNotifications", unviewedAppStatusNotifications);
					 return ResponseEntity.status(200).body(response);		
		    		 
		    	 }else {	    		 
		    		 return ResponseEntity.status(404).body("User not found");		     
		    	 }
		    	      
		      }catch(Exception e) {		 
			      new Exception(e.getMessage());	
			      return ResponseEntity.status(500).body("A Server error occured." + e.getMessage());		      
		     }	     
		     
	    }else{
			logger.info("Unauthorized user");
           return ResponseEntity.status(403).body("Unauthorized");
	    }
	 }
	 
	 
	/**
	 * 
	 * **/
	@MessageMapping("/notify")
	@SendTo("/topic/notifications")
	public String sendNotification(String message) {
		return message;
	}
	
	
	/***
	 * 
	 * **/
	public void sendApplicationStatusUpdate(String message) {
		messageTemplate.convertAndSend("/topic/notifications", message);	
	}
	
	/***
	 * 
	 * **/
	public void newApplication(String message) {
		messageTemplate.convertAndSend("/topic/notifications", message);	
	}
}
