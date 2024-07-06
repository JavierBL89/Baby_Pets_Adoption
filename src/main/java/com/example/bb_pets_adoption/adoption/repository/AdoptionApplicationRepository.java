/**
 * 
 */
package com.example.bb_pets_adoption.adoption.repository;


import com.example.bb_pets_adoption.adoption.model.AdoptionApplication;
import com.example.bb_pets_adoption.auth.model.User;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;


/***
 *Manages database operations for adoption applications.
 * 
 **/
public interface AdoptionApplicationRepository extends MongoRepository<AdoptionApplication, ObjectId> {
	
	/**
	 * 
	 * */
    List<AdoptionApplication> findByApplicantId(ObjectId userId);
    
    
    /**
	 * Query to fingd all applications related to a single pet
	 * using pagination based on predefined parameters
	 * 
	 * @param petId - pet ID lo search for
	 * @param pageable -  the obejct with info about the pageNo, and pageSize
	 * */
    Page<AdoptionApplication> findAllByPetId(ObjectId petId, Pageable pageable) throws Exception;
    
    
    
    /**
   	 * Query to fingd all applications related to a single pet
   	 * using pagination based on predefined parameters
   	 * 
   	 * @param petId - pet ID lo search for
   	 * @param pageable -  the obejct with info about the pageNo, and pageSize
   	 * */
      Page<AdoptionApplication> findAllByApplicantId(ObjectId applicantId, Pageable pageable);
       
       
    /**
	 * Query to find all applications related to a single pet
	 * without pagination
	 * 
	 * @param petId - pet ID lo search for
	 * */
    List<AdoptionApplication> findAllByPetId(ObjectId petId);
    
    /**
	 * Query to find a single application
	 * 
	 * @param petId - pet ID lo search for
	 * */
    AdoptionApplication findByPetId(ObjectId petId);
} 

