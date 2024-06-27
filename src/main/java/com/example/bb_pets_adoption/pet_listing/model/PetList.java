/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.model;


import java.time.LocalDate;
import java.util.Comparator;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.bb_pets_adoption.auth.model.User;
import com.example.bb_pets_adoption.pet_listing.repository.CatRepository;
import com.example.bb_pets_adoption.pet_listing.repository.DogRepository;
import com.example.bb_pets_adoption.pet_listing.repository.PetListRepository;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Data
@Document(collection = "pet_listings")
public class PetList{

	@Id
    private ObjectId id;
    
	private ObjectId userId; // reference to the User who created the post
    
    private Pet pet; // reference to the Pet related to the post
    
    private LocalDate createdOn;
    private LocalDate updatedOn;
    
    
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

}
