/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.repository;


import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.bb_pets_adoption.adoption.model.AdoptionApplication;
import com.example.bb_pets_adoption.pet_listing.model.Dog;
import com.example.bb_pets_adoption.pet_listing.model.PetList;

/**
 * Repository interface for Pet listings database operations
 */
@Repository
public interface PetListRepository extends MongoRepository<PetList, ObjectId>{


	 /**
	 * 
	 * */
	 List<PetList> findAllByUserId(ObjectId userId);
	
	  /**
	   * 
	   * */
	 Optional<PetList> findByPetId(ObjectId petId);
	
}
