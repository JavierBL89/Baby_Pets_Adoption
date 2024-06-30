/**
 * 
 */
package com.example.bb_pets_adoption.adoption.repository;


import com.example.bb_pets_adoption.adoption.model.AdoptionApplication;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


/***
 *Manages database operations for adoption applications.
 * 
 **/
public interface AdoptionApplicationRepository extends MongoRepository<AdoptionApplication, ObjectId> {
	
	/**
	 * 
	 * */
    List<AdoptionApplication> findByUserId(ObjectId userId);
    
    
    /**
	 * 
	 * */
    List<AdoptionApplication> findByPetId(ObjectId petId) throws Exception;
} 

