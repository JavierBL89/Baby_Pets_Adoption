package com.example.bb_pets_adoption.adoption.controller;

import com.example.bb_pets_adoption.adoption.model.AdoptionApplication;
import com.example.bb_pets_adoption.adoption.service.AdoptionApplicationService;
import com.example.bb_pets_adoption.adoption.service.AdoptionApplicationServiceImpl;
import com.example.bb_pets_adoption.auth.model.User;
import com.example.bb_pets_adoption.pet_listing.controller.PetController;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
     * 4. Delegate application process to AdoptionApplicationServiceImpl
     * 
     * @RequestParam {ObjectId} petId - the petId for adoption request
     * @RequestParam {String} token  - the current session auhentication token
     * @RequestParam {String} comments) - the user application  comments
     * @return @ResponseEntity - Http response with process output
     * **/
    @PostMapping("/apply")
    public ResponseEntity<?> applyForAdoption(
            @RequestParam("petId") String petIdString,
            @RequestParam("token") String token,
            @RequestParam("comments") String comments) {
    	

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
	 				
	 			    // delegate opeation to service. @params ( String petId, User user, String comments
	 				adoptionApplicationServiceImpl.createApplication(petIdString, user, comments);
	              				
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
        return adoptionApplicationServiceImpl.getApplicationsByUserId(userId);
    }

    
    /***
     * 
     * 
     * **/
    @GetMapping("/pet/{petId}")
    public List<AdoptionApplication> getApplicationsByPetId(@PathVariable ObjectId petId) {
        return adoptionApplicationServiceImpl.getApplicationsByPetId(petId);
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
    @PutMapping("/updateStatus")
    public ResponseEntity<?> updateApplicationStatus(
    		@RequestParam String applicationIdString,
            @RequestParam("token") String token,
            @RequestParam("status") String status)  {
    	
    	
        // handle null value token
   	    if (token == null) {
   	        logger.error("Authorization token is missing");
   	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing");
   	    }
   	    
   	    
   	    // try and catch any errors during application update process
   	    try {
   	        // if user is authenticated proceed to update application
   	 		if (adoptionApplicationServiceImpl.authenticateUserByToken(token)) {
   	 		       	 			
   	 			    // delegate operation to service @params ( String applicationIdString, String status)
   	 				adoptionApplicationServiceImpl.updateApplicationStatus(applicationIdString, status);
   	              					
   	 		}else {
   	 			
   	 			logger.info("Unauthorized user");
   	 			return ResponseEntity.status(403).body("Unauthorized user");
   	 		}
   	 		
   	    } catch (Exception e) {
   	        logger.error("Error creating updating application", e);
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
    		@RequestParam("petId") String petIdString,
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
	 				adoptionApplicationServiceImpl.deleteApplication(user, petIdString, applicationIdString);
	 			  
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
