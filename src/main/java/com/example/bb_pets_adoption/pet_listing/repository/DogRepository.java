/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.bb_pets_adoption.pet_listing.model.Dog;

/***
 * Repository for Dog model
 * 
 * Interface to storage, retreive, and search operations on Dog objects.
 * It extends MongoRepository which provides CRUD operations and some other helful methods  .
 */
public interface DogRepository extends MongoRepository<Dog, ObjectId>{

	/*
	 * Custom query to  find all dogs using Pageable interface and Page class
	 * to only load the specified amount of objects in one go enhancing the site's loading time
	 * and overall performance
	 * 
	 * @params pageable - the pageable object the determines the number of documents to be retrieved
	 * **/
	Page<Dog> findAll(Pageable pageable);
	
}
