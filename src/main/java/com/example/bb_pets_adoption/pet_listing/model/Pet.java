/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Field;


import lombok.Data;

/**
 * Abstract class to represent a Pet object from which each pet subtype inherits from.
 * 
 * It uses Lombok annotattion @Date to provide the claass with getters and setter plus a toString()
 * It uses MonogoDb annoattions to map the object, and to map file field such as images fields
 */
@Data
public abstract class Pet {

	
	@Id
	private ObjectId id;
	private ObjectId ownerId;
	private String ownerName;

	private ObjectId petListId;
	private String category;     // Cat or Dog
	private String breed;
	@Field
	private String petImg;
	private String comment;
	private LocalDate birthDate;
	private String location;
	private String motherBreed;
	@Field
    private String motherImg;
    private String fatherBreed;
    @Field
    private String fatherImg;
	private float price;
	private  List<String> tags;
	
    private List<PetList> posts; // list of posts related to the pet
    
    // constructor initialises arrayList
    public Pet() {
    	this.tags = new ArrayList<>();
    }
    
}
