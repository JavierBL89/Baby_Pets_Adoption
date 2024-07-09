/**
 * 
 */
package com.example.bb_pets_adoption.account_management.Repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.bb_pets_adoption.account_management.Model.User;
import com.example.bb_pets_adoption.pet_listing.model.PetList;

/**
 * UserRepository is the interface to storage, retreive, and search operations on User objects.
 * It extends MongoRepository which provides CRUD operations and some other helful methods  .
 */
@Repository
public interface UserRepository extends MongoRepository<User, ObjectId>{
	
	 /**
     * Custom query method to find a user by email
     * 
     * @param email the users' email address to search for
     * @return an Optional that will contain the found User or empty if no User is found
     */
	Optional<User> findByEmail(String email);

	 /**
     * Custom query method to find a user by their token
     * This token can be used for multiple purposes to find a user without knwoing or having to update their email
     * their email (e.g. a password reset)
     * 
     * @param token the users' token  to search for
     * @return an Optional that will contain the found User or empty if no User is found
     */
	Optional<User> findByToken(String token);
	
	

}
