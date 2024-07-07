/**
 * 
 */
package com.example.bb_pets_adoption.adoption.repository;


import com.example.bb_pets_adoption.adoption.model.AdoptionApplication;
import com.example.bb_pets_adoption.auth.model.User;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Pageable;
import java.util.List;


/***
 *Manages database operations for adoption applications.
 * 
 **/
public interface AdoptionApplicationRepository extends MongoRepository<AdoptionApplication, ObjectId> {
	
	
	/**
	 * Query to find all applciations related to a specific applicantId
	 * This is used to check for duplicate applications on a single pet
	 * 
	 * @param userId - the objectId of the applicant
	 * */
    List<AdoptionApplication> findByApplicantId(ObjectId userId);
    
    
    /**
	 * Query to fingd all applications related to a single pet
	 * using pagination based on predefined parameters
	 * 
	 * @param petId - pet ID to search for
	 * @param pageable -  the object with info about the pageNo, and pageSize
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
     * Custom Query to find all applications related to a single pet filtering the search by 
     * the application status using pagination based on predefined parameters
     * 
     * - '_id': This specifies that the query will match documents based on the _id field
     * - '?0': This is a placeholder for the first method parameter
     * - 'status': This specifies that the query will match documents based on the status field
     * - '?1' : This is a placeholder for the second method parameter
     * 
     * @param petId - pet ID lo search for
     * @param status - the application status to filter seacrh
     * @param pageable -  the obejct with info about the pageNo, and pageSize
     * */
      @Query("{ 'petId': ?0, 'status': ?1 }")
       Page<AdoptionApplication> findAllByStatus(ObjectId petId, String status, Pageable pageable);
         
         
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

