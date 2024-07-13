package com.example.bb_pets_adoption.adoption.controller;

import com.example.bb_pets_adoption.adoption.model.AdoptionApplication;
import com.example.bb_pets_adoption.adoption.service.AdoptionApplicationService;
import com.example.bb_pets_adoption.adoption.service.AdoptionApplicationServiceImpl;
import com.example.bb_pets_adoption.account_management.Model.User;
import com.example.bb_pets_adoption.pet_listing.controller.PetController;
import com.example.bb_pets_adoption.adoption.service.ApplicationDateDescendingComparator;
import com.example.bb_pets_adoption.adoption.service.ApplicationDateAscendingComparator;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/***
 * Exposes REST endpoints for managing adoption applications.
 * 
 * **/
@RestController
@RequestMapping("/adoption")
public class AdoptionApplicationController {

    
    private AdoptionApplicationService adoptionApplicationServiceImpl;

	// create an instance of Logger
	private static final Logger logger = LoggerFactory.getLogger(PetController.class);
		
	
    // Constructor for dependancies injection
    @Autowired
    public AdoptionApplicationController(AdoptionApplicationServiceImpl adoptionApplicationServiceImpl) {
    	this.adoptionApplicationServiceImpl =  adoptionApplicationServiceImpl;
    }
    
    
    /***
     * Endpoint to receive and handle adoption application POST requests
     * 
     * Steps:
     * 1.Check for token null value
     * 2. Authenticate user by the token passed
     * 3. Find user in database
     * 4. Check for duplicate applications. 
     * 5. Delegate application process to AdoptionApplicationServiceImpl
     * 
     * @RequestParam {ObjectId} petId - the petId for adoption request
     * @RequestParam {String} token  - the current session auhentication token
     * @RequestParam {String} comments) - the user application  comments
     * @return @ResponseEntity - Http response with process output
     * **/
    @PostMapping("/apply")
    public ResponseEntity<?> applyForAdoption(
            @RequestParam(value="petId", required=false) String petIdString,
            @RequestParam(value="petCategory", required=false) String petCategory,
            @RequestParam(value="token", required=false) String token,
            @RequestParam(value="comments", required=false) String comments,
            @RequestParam(value="override", required=false) boolean override) {
    	
    logger.info(petCategory);
    
        // handle null value token
	    if (token == null) {
	        logger.error("Authorization token is missing");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing");
	    }
	    
	    
	    // try and catch any errors during adoption application creation process
	    try {
	        // if user is authenticated proceed to create a a new pet and assocciate this with the user pet listings
	 		if (adoptionApplicationServiceImpl.authenticateUserByToken(token)) {

	 			// find user
	 			Optional<User> foundUser = adoptionApplicationServiceImpl.findUserByToken(token);
	 			if(foundUser.isPresent()) {

	 				User user = foundUser.get(); // cast optimal to a User instance
	 				
	 			     // check for duplicate applications
	     	    	 boolean foundDuplicate = adoptionApplicationServiceImpl.isDuplicate(petIdString, user);
	     	    	if (foundDuplicate) {
                        logger.info("Duplicate adoption application");
                        return ResponseEntity.status(409).body("Duplicate application");
                    }
		 			   
	 			    // delegate opeation to service. @params ( String petId, User user, String comments
	 				adoptionApplicationServiceImpl.createApplication(petIdString, petCategory, user, comments);
	              				
	 			}else {
	 				
	 			   logger.info("User not found");
                   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found or unauthorized");           
	 			} 
	 			
	 		}else {
	 			
	 			logger.info("Unauthorized user");
	 			return ResponseEntity.status(403).body("Unauthorized user");
	 		}
	 		
	    } catch (Exception e) {
	        logger.error("Error creating adoption application", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating adoption application: " + e.getMessage());
	    }
	    
	    logger.info("Application successfully submitted");
	    return ResponseEntity.status(200).body("Application successfully submitted");
    }

    
    /***
     * 
     * 
     * **/
    @GetMapping("/{id}")
    public ResponseEntity<AdoptionApplication> getApplicationById(@PathVariable ObjectId id) {
        return adoptionApplicationServiceImpl.getApplicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
    /***
     * 
     * 
     * **/
    @GetMapping("/user/{userId}")
    public List<AdoptionApplication> getApplicationsByUserId(@PathVariable ObjectId userId) {
        return adoptionApplicationServiceImpl.getApplicationsByApplicantId(userId);
    }

    
    /***
     * Endpoint
     * It also handles list sorting (asc, des). Check the value of 'order' passed and acts accordingly.

     * **/
    @GetMapping("/pet/applications")
    public ResponseEntity<Map<String, Object>> getAllApplicationsByPetId(
    		@RequestParam(value="petId", required=false) String petIdString,
			@RequestParam(value= "order", required = false, defaultValue="asc")  String order,
			@RequestParam(value= "status", required = false, defaultValue="asc")  String status,
    		@RequestParam(value="token",  required=false) String token,
    		@RequestParam(value="pageNo",  defaultValue= "0", required=false) int pageNo,
    		@RequestParam(value="pageSize",  defaultValue="6", required=false)int pageSize) {
    	
    	Map <String, Object> response = new HashMap<>(); // instantiate a Hashmap to send response message
    		
    	 // handle null value token
   	    if (token == null) {
   	        logger.error("Authorization token is missing");    // set response message
   	        response.put("message", "Authorization token is missing");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
   	    }
   	    
   	    
   	    
   	    // authenticate by using token
   	    if(adoptionApplicationServiceImpl.authenticateUserByToken(token)) {
   	    	
   	    	// find user in database
   	    	try {
   	    		Optional<User> foundUser = adoptionApplicationServiceImpl.findUserByToken(token);
   	    		if(foundUser.isPresent()) { 
					 logger.info(foundUser.get().toString());

   	    		   // create Pageable instance	
   	    	   	   Pageable pageable = PageRequest.of(pageNo, pageSize);  
 	   	   
   	    	   	   // grab the page of items returned
                   Page<AdoptionApplication> applicationsPage = adoptionApplicationServiceImpl.getAllApplicationsByStatus(petIdString, pageable, status);
          	       logger.info("Applications list succesfully retrieved with status '" + status + "'");    // set response message

                   // pass the content to List to be able to sort it by using Coolections.sort()
                   List<AdoptionApplication> applications = applicationsPage.getContent();

                   
                   response.put("applications", applications);      	 
                   return ResponseEntity.status(200).body(response);

                   // Check if param indicates ascending or descending order
                /*   if (order == null || "asc".equalsIgnoreCase(order)) {
                       // Sort the applications list from latest to oldest
                       Collections.sort(applications, new ApplicationDateAscendingComparator());
                       logger.info("List sorted in ascending order. Sending list...");
                   } else if ("desc".equalsIgnoreCase(order)) {
                       // Sort the applications list from oldest(default) to the latest
                       Collections.sort(applications, new ApplicationDateDescendingComparator());
                       logger.info("List sorted in descending order. Sending list...");
                   } else {
                       response.put("message", "Invalid order parameter");
                       logger.error("Invalid order parameter: {}", order);
                       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                   }

          */
           

   	    		}else {  	    			
   	    			response.put("message", "User could not be found");  // set response message 	    			
   	    			logger.error("User could not be found");
   	    	    	return ResponseEntity.status(404).body(response);
   	    		}   	    		
   	    	}catch(Exception e) {
   	    		
	    	 logger.error("Error while retrieving user data: " + e.getMessage());  
             response.put("message", "Error while retrieving user data: " + e.getMessage());    // set response message
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);       
   	    	}
   	    	
   	    }else {   	    	
   	    	logger.error("User could not be authenticated");   	    	
            response.put("message", "User could not be authenticated");    // set response message
   	    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
   	    }  	
    }

    
    
    /***
     * Endpoint
     * It also handles list sorting (asc, des). Check the value of 'order' passed and acts accordingly.

     * **/
    @GetMapping("/my_applications")
    public ResponseEntity<Map<String, Object>> getAllApplicationsByUserId(
			@RequestParam(value= "order", required = false, defaultValue="asc")  String order,
    		@RequestParam(value="token",  required=false) String token,
    		@RequestParam(value="pageNo",  defaultValue= "0", required=false) int pageNo,
    		@RequestParam(value="pageSize",  defaultValue="6", required=false)int pageSize) {
    	
    	Map <String, Object> response = new HashMap<>(); // instantiate a Hashmap to send response message
    	    	
    	 // handle null value token
   	    if (token == null) {
   	        logger.error("Authorization token is missing");    // set response message
   	        response.put("message", "Authorization token is missing");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
   	    }
   	      
   	    
   	    // authenticate by using token
   	    if(adoptionApplicationServiceImpl.authenticateUserByToken(token)) {
   	    	
   	    	// find user in database
   	    	try {
   	    		Optional<User> foundUser = adoptionApplicationServiceImpl.findUserByToken(token);
   	    		if(foundUser.isPresent()) { 
   	    		   
   	    			User user = foundUser.get();
   	    		   // create Pageable instance	
   	    	   	   Pageable pageable = PageRequest.of(pageNo, pageSize);  
   	    	   	    	    	   	   
   	    	   	   // grab the page of items returned
                   Page<AdoptionApplication> applicationsPage = adoptionApplicationServiceImpl.getAllApplicationsByApplicantId(user, pageable);
                   // pass the content to List to be able to sort it using Coolections.sort()
                   List<AdoptionApplication> applications = applicationsPage.getContent();
                   
              	  response.put("applications", applications);      	 
                  return ResponseEntity.status(200).body(response);  
                  
                    // sort list based on user preference
                 /*   try {                  	
                    	adoptionApplicationServiceImpl.sortList(applications, order);
                    	  response.put("applications", applications);      	 
                          return ResponseEntity.status(200).body(response);                          
                    }catch(Exception e) {                  	
                        response.put("message", e.getMessage());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                */
                   
                   
   	    		}else {  	   	    			
   	    			response.put("message", "User could not be found");  // set response message 	    			
   	    			logger.error("User could not be found");
   	    	    	return ResponseEntity.status(404).body(response);
   	    		}
   	    		
   	    	}catch(Exception e) { 	    		
	    	     logger.error("Error while retrieving user data: " + e.getMessage());  
                 response.put("message", "Error while retrieving user data: " + e.getMessage());    // set response message
                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);       
   	    	}
   	    	
   	    }else {   	    	
   	    	logger.error("User could not be authenticated"); 	    	
            response.put("message", "User could not be authenticated");    // set response message
   	    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
   	    }
    	
    }
    /***
     * Endpoint to receive and manage PUT request to update an adoption application
     * 
     * 1. Check for token null value
     * 2. Authenticate user by the token passed
     * 3. Delegate object update process to AdoptionApplicationServiceImpl
     * 
     * @RequestParam {String} applicationIdString - the applicationId to work on
     * @RequestParam {String} token - current session token for user authentication
     * @RequestParam {String} status) - the new application status to be set
     * **/
    @PutMapping("/pet/applications/updateStatus")
    public ResponseEntity<?> updateApplicationStatus(
            @RequestParam(value= "token", required = false) String token,
            @RequestParam(value= "status", required = false) String status,
            @RequestParam(value= "applicationId", required = false) String applicationIdString)  {
    	
    	
        // handle null value token
   	    if (token == null) {
   	        logger.error("Authorization token is missing");
   	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing");
   	    }
   	    
   	    // try and catch any errors during application update process
   	    try {
   	    	
   	        // if user is authenticated proceed to update application
   	 		if (adoptionApplicationServiceImpl.authenticateUserByToken(token)) {
   	 		    Optional<User> foundUser = adoptionApplicationServiceImpl.findUserByToken(token);
	    		if(foundUser.isPresent()) { 	 
	    			
	    			User user = foundUser.get();  	 			
   	 			    // delegate operation to service @params ( String applicationIdString, String status)
   	 				adoptionApplicationServiceImpl.updateApplicationStatus(applicationIdString, user, status);
   	 				
	    		}else {  	   	    			
   	    			logger.error("User could not be found");
   	    	    	return ResponseEntity.status(404).body("User could not be found");
   	    		}
   	    		
   	 		}else { 			
   	 			logger.info("Unauthorized user");
   	 			return ResponseEntity.status(403).body("Unauthorized user");
   	 		}   	 		
   	    } catch (Exception e) {
   	        logger.error("Error updating application", e);
   	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating adoption application: " + e.getMessage());
   	    }
   	    
   	    logger.info("Application successfully updated");
   	    return ResponseEntity.status(200).body("Application successfully updated"); 
    }

    
    
    /***
     * Endpoint receives and manages DELETE requests for deleting adoption applications
     * 
     * Steps:
     * 1. Check for token null value
     * 2. Authenticate user by the token passed
     * 3. Find user in database
     * 4. Delegate application process to AdoptionApplicationServiceImpl
     *
     * @RequestParam("petId") String petIdString,
     * @RequestParam("token") String token,
     * @RequestParam("applicationId") String applicationIdString
     * @return @ResponseEntity - Http response with process output
     * **/
    @DeleteMapping("/delete_application")
    public ResponseEntity<?> deleteApplication(
            @RequestParam("token") String token,
            @RequestParam("applicationId") String applicationIdString
            ) {
    	
    	
        /// handle null value token
	    if (token == null) {
	        logger.error("Authorization token is missing");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing");
	    }
	    
	    
	    // try and catch any errors during adoption application creation process
	    try {
	        // if user is authenticated proceed to create a a new pet and assocciate this with the user pet listings
	 		if (adoptionApplicationServiceImpl.authenticateUserByToken(token)) {
	 		    
	 			// find user
	 			Optional<User> foundUser = adoptionApplicationServiceImpl.findUserByToken(token);
	 			if(foundUser.isPresent()) {
	 				
	 				User user = foundUser.get(); // cast optimal to a User instance
	 				
	 			    // delegate opeation to service. @params ( User user, String petIdString, String applicationIdString )
	 				adoptionApplicationServiceImpl.deleteApplication(user, applicationIdString);
	 			  
	 			    logger.info("Application successfully deleted");
	 			    return ResponseEntity.status(200).body("Application succesfully deleted"); 
	 			}else {
	 				
	 			   logger.info("User not found");
                   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found or unauthorized");           
	 			} 
	 			
	 		}else {
	 			
	 			logger.info("Unauthorized user");
	 			return ResponseEntity.status(403).body("Unauthorized user");
	 		}
	 		
	    } catch (Exception e) {
	        logger.error("Error deleting adoption application", e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting adoption application: " + e.getMessage());
	    }
	    
	    
    }
}
