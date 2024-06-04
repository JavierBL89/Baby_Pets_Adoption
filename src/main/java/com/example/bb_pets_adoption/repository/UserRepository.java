/**
 * 
 */
package com.example.bb_pets_adoption.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.bb_pets_adoption.model.User;

/**
 * 
 */

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId>{
	
	// Custom query method to find a user by email
	User findByEmail(String email);

}
