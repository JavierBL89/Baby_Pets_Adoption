/**
 * 
 */
package com.example.bb_pets_adoption.search.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bb_pets_adoption.pet_listing.model.Cat;
import com.example.bb_pets_adoption.pet_listing.model.Dog;
import com.example.bb_pets_adoption.pet_listing.service.PetServiceImpl;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 */
@RestController
@RequestMapping("/pets")
public class SearchController {

	// vars
	PetServiceImpl petServiceImpl;
	

	// create an instance of Logger
	private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
		
		
	/**
	  * Constructor injection for PetServiceImpl
	  * */
	 @Autowired
	 public SearchController( PetServiceImpl petServiceImpl) {
	        this.petServiceImpl = petServiceImpl;
	        
	    }
	
	// endpoints
	
	
	/**
	 * Endpoint to handle GET requests and retrieves a cat by its ID
	 * The cat's ID is specified as a path variable in the URL
	 * 
	 * @param id the unique identifier of the cat to be retrieved
	 * @return an Optional containing the cat if found, or an empty Optional if not found
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/kitties/view/{id}")
	public @ResponseBody Optional<Cat> findCatById(@PathVariable String id){
		
		return petServiceImpl.findCatById(id);
	}
	
	
	
	/**
	 * Endpoint to handle GET requests and retrieves a dog by its ID
	 * The cat's ID is specified as a path variable in the URL
	 * 
	 * @param id the unique identifier of the dog to be retrieved
	 * @return an Optional containing the dog if found, or an empty Optional if not found
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/puppies/view/{id}")
	public @ResponseBody Optional<Dog> findDogById(@PathVariable String id){

		return petServiceImpl.findDogById(id);
	}
	
	
	// endpoints
	
		/**
		 * Endpoint to handle GET requests and retrives a paginated lists of cats
		 * 
		 * The parameters passed defined the number of pages and the number of items per page to retreive
		 * E.g   pageNo 1, items 20
		 *       pageNo 2, items 20 + 20
		 * 
		 * GET /pets/cats/page?page=0&size=10
		 * 
		 * @param pageNo - the page number to retrieve is 0 as default and updated on each request
	     * @param pageSize the number of records per page. It is set to 20 as default i not specified in request
	     * @return a Page object containing the paginated list of cats
		 * */
		@GetMapping("/kitties/filter_by")
		public Page<Cat> getAllCatsByTags(
				@RequestParam (value = "tags", required = false)  List<String> tags,
				@RequestParam (value = "pageNo", defaultValue ="0", required = false)  int pageNo,
				@RequestParam (value = "pageSize", defaultValue = "6", required = false) int pageSize){
			
			    Pageable pageable = PageRequest.of(pageNo, pageSize);// initialize Pageable object with values
		
				return petServiceImpl.findAllCatsByTags(tags, pageable);
			
	
		}
		
		
		/**
		 * Endpoint to handle GET requests and retrives a paginated lists of cats
		 * 
		 * The parameters passed defined the number of pages and the number of items per page to retreive
		 * E.g   pageNo 1, items 20
		 *       pageNo 2, items 20 + 20
		 * 
		 * GET /pets/cats/page?page=0&size=10
		 * 
		 * @param pageNo - the page number to retrieve is 0 as default and updated on each request
	     * @param pageSize the number of records per page. It is set to 20 as default i not specified in request
	     * @return a Page object containing the paginated list of cats
		 * */
		@GetMapping("/puppies/filter_by")
		public Page<Dog> getAllDogsByTags(
				@RequestParam (value = "tags", required = false)  List<String> tags,
				@RequestParam (value = "pageNo", defaultValue ="0", required = false)  int pageNo,
				@RequestParam (value = "pageSize", defaultValue = "6", required = false) int pageSize){
			 logger.info("REQUEST TO HERE PUTAAAA");

			    Pageable pageable = PageRequest.of(pageNo, pageSize);	// initialize Pageable object with values
			    
				return petServiceImpl.findAllDogsByTags(tags, pageable);
	
		}
}

