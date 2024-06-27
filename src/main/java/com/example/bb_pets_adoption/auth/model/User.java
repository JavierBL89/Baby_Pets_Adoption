/**
 * 
 */
package com.example.bb_pets_adoption.auth.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.bb_pets_adoption.pet_listing.model.Pet;
import com.example.bb_pets_adoption.pet_listing.model.PetList;
import com.example.bb_pets_adoption.pet_listing.repository.CatRepository;
import com.example.bb_pets_adoption.pet_listing.repository.DogRepository;
import com.example.bb_pets_adoption.pet_listing.repository.PetListRepository;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* Class represents a User. It defines the attributes of a User object.
* A User can be an "Adopter", "Pet Provider", or both.
* The fields are mapped and converted into a User document using MongoDb @Document 
* to then be stored into Mongo database
* 
* Class usess Lombok @nnotations for reducing boiler plate and enhance readiness
*/

@Data                  // This annotation generates getters, setters, toString, equals, and hashCode methods 
@NoArgsConstructor     // This annotation generates a no-argument constructor
@AllArgsConstructor    // This annotation generates an all-argument constructor
@Document(collection = "users") // Marks this class as a MongoDB User document
public class User{

	
	@Id
	private ObjectId userId;
	private String name;
	private String lastName;
	private String email;
	private String password;
	private String token;
	private String registeredBy;
	private List<String> roles;  // list of roles ["Adopter", "Pet Provider"]
	
    @DBRef
	private List<PetList> petList = new ArrayList<>();   // list of posts created by the user


    /**
     * Method to removed all objects associated to the user when the user deletes thir account
     * 
     * @param catRepository - the Cat repository
     * @param dogRepository - the Dog repository
     * **/
    public void cascadeDelete(PetListRepository petListRepository, CatRepository catRepository, DogRepository dogRepository) {
    	
    	if(petList != null) {
    		
    		/** 
    		 * 1. iterate over the list of pets listed 
    		 * 2. call cascadeDelete for each item and pass repositories references needed
    		 * 3. remove all items in the petList list
    		 */
    		// use helper method from 
    		for (PetList listItem : petList) {
    			
    			listItem.cascadeDelete(catRepository, dogRepository); // remove from pet collections
    			petListRepository.delete(listItem);       // remove from petListings collection
    		}
    	}
    }
}