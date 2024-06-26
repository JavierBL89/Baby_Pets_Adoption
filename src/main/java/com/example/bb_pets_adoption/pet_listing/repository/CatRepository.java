/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.repository;

import org.springframework.stereotype.Repository;

import com.example.bb_pets_adoption.pet_listing.model.Cat;
import com.example.bb_pets_adoption.pet_listing.model.Dog;
import com.example.bb_pets_adoption.pet_listing.model.Pet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


/***
 * Repository for Cat model
 * 
 * Interface to storage, retreive, and search operations on Cat objects.
 * It extends MongoRepository which provides CRUD operations and some other helful methods  .
 */
@Repository
public interface CatRepository extends MongoRepository<Cat, ObjectId> {

	/*
	 * Custom query to  find all cats using Pageable interface and Page class
	 * to only load the specified amount of objects in one go enhancing the site's loading time
	 * and overall performance
	 * 
	 * @params pageable - the pageable object the determines the number of documents to be retrieved
	 * **/
	Page<Cat> findAll(Pageable pageable);
	
	
	/***
	 * Custom query to finsd all cats with attributes that match with some or all of the tags encountered within the tags list.
	 * This is possible thanks to the custom query property '$in' in MongoDB
	 * 
	 * It uses Pageable interface and Page class to only load the specified amount of objects in one go enhancing the site's loading time
	 * and overall performance
	 * 
	 * @param tags - the list containing keywords to filter out the pet search
	 * @params pageable - the pageable object the determines the number of documents to be retrieved
	 * **/
	@Query("{ 'tags': { $in: ?0 } }")
	Page<Cat> findByTagsIn(List<String> tags, Pageable pageable);
	
	
	/***
	 * Custom query to find a cat by its id
	 * 
	 * @param petId - the pet id
	 * **/
	Optional<Cat> findCatById(ObjectId petId);

}
