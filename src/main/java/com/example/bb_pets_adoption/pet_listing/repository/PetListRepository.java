/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.repository;


import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


import com.example.bb_pets_adoption.pet_listing.model.Dog;
import com.example.bb_pets_adoption.pet_listing.model.PetList;

/**
 * Repository interface for Pet listings database operations
 */
public interface PetListRepository extends MongoRepository<PetList, ObjectId>{


	Optional<PetList> findByUserId(ObjectId userId);
	
}
