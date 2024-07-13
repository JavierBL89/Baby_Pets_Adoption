/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.model;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.bb_pets_adoption.pet_listing.repository.CatRepository;
import com.example.bb_pets_adoption.pet_listing.repository.DogRepository;
import com.example.bb_pets_adoption.real_time_notifications.Model.Notification;
import com.example.bb_pets_adoption.search.controller.SearchController;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Class to encapsulate information for each new pet listed.
 * 
 * - Date pet is listed
 * - Date of any update on the pet list
 * - User object
 * - Pet object 
 * 
 * It uses MongoDb annotation to map the object and use the appropiate database collection
 * It uses Lombok @Data annotation to generate getters, setters and the useful toString()
 */
@Data                  // This annotation generates getters, setters, toString, equals, and hashCode methods 
@AllArgsConstructor    // This annotation generates an all-argument constructor@Document(collection = "pet_listings")
@Document(collection = "pet_listings") // Marks this class as a MongoDB PetList document
public class PetList{

	
	// create an instance of Logger
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    
    
	@Id
    private ObjectId id;
    
	private ObjectId userId; // reference to the User who created the post
    
    private Pet pet;         // Pet object related to the post
    private ObjectId petId;  // reference to the Pet related to the post
    private List<ObjectId> adoptionApplicationIDs;
    private List<Notification> pendingNotifications;
    
    private LocalDate createdOn;
    private LocalDate updatedOn;
    
    private String status;   // 'available'(default), 'hidden' , 'unavailable'
    
    
    /// consturictor initializes adoptionApplicationIDs list && pendingNotifications list
    public PetList() {
    	
    	this.adoptionApplicationIDs = new ArrayList<>();
    	this.pendingNotifications = new ArrayList<>();
    }
    
    
    /**
     * Method to removed the user and pet associated to the PetList when the user deletes the petList instance 
     * or when the user account is deleted and the deleteOnCasacde is executed
     * 
     * @param catRepository - the Cat repository
     * @param dogRepository - the Dog repository
     * **/
    public void cascadeDelete(CatRepository catRepository, DogRepository dogRepository) {
    	
    	// check the pet category type and remove it from the appropiate collection
    	if(pet != null) { 		
    	    if(pet.getCategory().equals("cat")) {
    	    	catRepository.delete((Cat) pet);
    	    	
    	    }else if (pet.getCategory().equals("dog")) {
                dogRepository.delete((Dog) pet);
            }
    	 }
    }

    /**
     * Method to removed an adoption application ID from adoptionApplicationIDs List 
     * 
     * @param catRepository - the Cat repository
     * @param dogRepository - the Dog repository
     * **/
    public void removeApplicationId(ObjectId applicationID) throws Exception{
    	
    	  if (applicationID == null) {
              throw new Exception("Application ID cannot be null");
          }
    	  
    	  // try removing the element and grab the returned boolean to use it as a flag
    	  boolean applicationRemoved = adoptionApplicationIDs.remove(applicationID);

          if (applicationRemoved) {
              logger.info("Adoption application successfully removed from PetList with id " + this.getId());
          } else {
              throw new Exception("Adoption application ID " + applicationID + " was not found in user adoptionApplicationIDs list");
          }
    	
    }
}
