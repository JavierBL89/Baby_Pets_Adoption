/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bb_pets_adoption.auth.model.User;
import com.example.bb_pets_adoption.pet_listing.model.Cat;
import com.example.bb_pets_adoption.pet_listing.model.Dog;
import com.example.bb_pets_adoption.pet_listing.model.Pet;
import com.example.bb_pets_adoption.pet_listing.model.PetList;

/**
 * Interface defines common pet operations
 * 
 * - getAll
 * - 
 * NOTE: findAll() methods support pagination for enhancing site's performance reducind loading time
 * , and ensuring a good user experience by rendering objects in a user-friendly manner:
 * - They takes 2 parameters 'pageNo' and 'pageSize, which specofy the page number and the number of records per page
 * - They return a 'Page' object containinh the list of pets for each page
 * 
 */
@Service
public interface PetService<T> {

	
	/**
     * Query to retrieve all cats with pagination.
     * 
     * @param pageNo the number of the page to retrieve
     * @param pageSize the number of records per page
     * @return a `Page` object containing the cats for the requested page
     */
	public Page<Cat> findAllCats(Pageable pageable);
	
	
	/**
	 * Query to retrieve all dogs with pagination 
	 * 
	 * @param pageNo the number of the page to retrieve
     * @param pageSize the number of records per page
     * @return a `Page` object containing the dogs for the requested page
	 * */
	public Page<Dog> findAllDogs(Pageable pageable);
	
	/**
	 * Query to search and retrieve all cats containing attributes that match with a selected pet tag attributes list
	 * 
	 * @param tags - the list of selected tags
     * @param pageNo the number of the page to retrieve
     * @param pageSize the number of records per page
     * @return a `Page` object containing the cats matching the attributes selected
	 * **/
	public Page<Cat> findAllCatsByTags(List<String> tags, Pageable pageable);
	
	/**
	 * Query to search and retrieve all dogs containing attributes that match with a selected pet tag attributes list
	 * 
	 * @param tags - the list of selected tags
     * @param pageNo the number of the page to retrieve
     * @param pageSize the number of records per page
     * @return a `Page` object containing the cats matching the attributes selected
	 * **/
	public Page<Dog> findAllDogsByTags(List<String> tags, Pageable pageable);
	
	
	/**
	 * Query to search a cat by id 
	 * 
	 * @param petType - the unique cat identifier
	 * @return an `Optional` containing the found cat or empty if not found
	 * */
	  Optional<Cat> findCatById(String petId);
	  
	  
	/**
	 * Query to search a dog by id 
	 * 
	 * @param id - the unique dog identifier
	 * @return an `Optional` containing the found cat or empty if not found
	 * */
	  Optional<Dog> findDogById(String petId);
	  
	  
		/**
		 * Query to create a new cat 
		 * 
		 * @param id - the unique cat identifier
		 * @return an `Optional` containing the found cat or empty if not found
		 * */
		public Cat createNewCat(Cat cat);
		  
		  
			/**
			 * Query to create a new dog 
			 * 
			 * @param id - the unique dog identifier
			 * @return an `Optional` containing the found cat or empty if not found
			 * */
			public Dog createNewDog(Dog dog);
			  
			  
			  /***
			   * Method to use Auth Service for user authentication
			   * 
			   * @param token - the current session token
			   * @return a boolean 
			   * **/
			  boolean authenticateUserByToken(String token);
			  
			  
			  /**
			   *Method for retrieving authenticated user from users colection
			   *
			   *@param token - string token of user's current session 
			   **/
			  Optional<User> findUserByToken(String token);
			  
			  
			  
			  /**
			   * Method for removing the current pet list from database
			   * 
			   * @param petList - the pet list id
			   * **/
			  void deletePetList(String petListId) throws Exception;
			  
			  
			  /**
			   * Method for removing a user from database
			   * 
			   * @param petList - the pet list instance
			   * **/
			  void deleteUser(User user);
			  
			  
			  
			  
	
}
