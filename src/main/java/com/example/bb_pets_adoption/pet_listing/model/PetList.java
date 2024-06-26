/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.model;


import java.time.LocalDate;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.bb_pets_adoption.auth.model.User;

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
public class PetList {

	@Id
    private ObjectId id;
    
    @DBRef
    private User user; // Reference to the User who created the post
    
    @DBRef
    private Pet pet; // Reference to the Pet related to the post
    
    private LocalDate createdOn;
    private LocalDate updatedOn;
}
