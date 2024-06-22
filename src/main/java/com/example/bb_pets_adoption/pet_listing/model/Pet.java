/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.Data;

/**
 * Abstract class to represent a Pet object from which each pet subtype inherits from
 */
@Data
public abstract class Pet {

	
	@Id
	private ObjectId petId;
	private ObjectId providerId;
	private String type;     // Cat or Dog
	private String breed;
	private String fur;
	private List<String> color;
	private String description;
	private LocalDate birthDate;
	private String location;
	private List<Pet> parents;
	private List<String> images;
	private List<String> tags;
	
	@DBRef
    private List<Post> posts; // list of posts related to the pet
}
