/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.bb_pets_adoption.pet_listing.model.Pet;

/**
 * Repository class is for polymorphic operations when updating pets based on their category.
 * 
 * This will allow for maintainable and cleaner code on the PetServiceImple update() 
 * avoiding if/else statements to check for pet categories
 * 
 */
@Repository
public interface PetRepository <T extends Pet> extends MongoRepository<T, ObjectId> {
}
