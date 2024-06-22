/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.bb_pets_adoption.pet_listing.model.Cat;
import com.example.bb_pets_adoption.pet_listing.model.Dog;
import com.example.bb_pets_adoption.pet_listing.model.Pet;

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
	
	/***
	 * Custom query to finsd all dogs with attributes that match with some or all of the tags encountered within the tags list.
	 * This is possible thanks to the custom query property '$in' in MongoDB
	 * 
	 * It uses Pageable interface and Page class to only load the specified amount of objects in one go enhancing the site's loading time
	 * and overall performance
	 * **/
	@Query("{ 'tags': { $in: ?0 } }")
	Page<Dog> findByTagsIn(List<String> tags, Pageable pageable);
	
	
}
