/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.example.bb_pets_adoption.auth.controller.JwtUtil;
import com.example.bb_pets_adoption.auth.model.User;
import com.example.bb_pets_adoption.email.EmailService;
import com.example.bb_pets_adoption.pet_listing.model.Cat;
import com.example.bb_pets_adoption.pet_listing.model.Dog;
import com.example.bb_pets_adoption.pet_listing.model.Pet;
import com.example.bb_pets_adoption.pet_listing.model.PetList;
import com.example.bb_pets_adoption.pet_listing.model.PetUpdateData;
import com.example.bb_pets_adoption.pet_listing.service.PetListDateDescendingComparator;
import com.example.bb_pets_adoption.pet_listing.service.PetListDateAscendingComparator;
import com.example.bb_pets_adoption.pet_listing.service.PetServiceImpl;
import com.example.bb_pets_adoption.pet_listing.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 */
@RestController
@RequestMapping("/pets")
public class PetController {

	// vars
	PetServiceImpl petServiceImpl;
	S3Service s3Service;

	// create an instance of Logger
	private static final Logger logger = LoggerFactory.getLogger(PetController.class);
		
		
	/**
	  * Constructor injection for PetServiceImpl
	  * */
	 @Autowired
	 public PetController( PetServiceImpl petServiceImpl, S3Service s3Service) {
	        this.petServiceImpl = petServiceImpl;
	        this.s3Service = s3Service;
	        
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
	@GetMapping("/kitties")
	public Page<Cat> getAllCats(
			@RequestParam (value = "pageNo", defaultValue ="0", required = false)  int pageNo, 
			@RequestParam (value = "pageSize", defaultValue = "6", required = false) int pageSize){
		
		    Pageable pageable = PageRequest.of(pageNo, pageSize);
		
		return petServiceImpl.findAllCats(pageable);
	}
	
	
	/**
	 * Endpoint to handle GET requests and retrives a paginated lists of cats
	 * 
	 * The parameters passed defined the number of pages and the number of items per page to retreive
	 * E.g   pageNo 1, items 20
	 *       pageNo 2, items 20 + 20
	 * 
	 * GET /pets/puppies/page?page=1&size=5
	 * 
	 * @param pageNo - the page number to retrieve is 0 as default and updated on each request
     * @param pageSize the number of records per page. It is set to 20 as default i not specified in request
     * @return a Page object containing the paginated list of cats
	 *
	 * */
	@GetMapping("/puppies")
	public Page<Dog> getAllDogs(
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
		    @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize
		    ){
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		
		return petServiceImpl.findAllDogs(pageable);
	}
	
	
	/** 
	 *  Endpoint to create a new pet
	 *  
	 *  
	 * It uses:
	 * @RequestParam for query parameters (also used for MultipartFile adn form fields, 
	 * and allows to handle both data and files in a multipart request)
	 * 
	 * @param token     The authentication token to verify the user.
     * @param formData  The data of the pet to be created.
     * @return ResponseEntity - an HTTP JSON object response with useful information about the request output
	 * **/
	@PostMapping(value= "/list_pet", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)		
	public ResponseEntity<?> createPet(
			@RequestParam(value="token") String token,
			 @RequestParam("category") String category,
	            @RequestParam("breed") String breed,
	            @RequestPart(value="petImg", required=false) MultipartFile petImg,
	            @RequestParam(value="location") String location,
	            @RequestParam(value="birthMonth") String birthMonth,
	            @RequestParam(value="birthYear") String birthYear,
	            @RequestParam(value="providerId", required=false) String providerId,
	            @RequestParam(value="motherBreed") String motherBreed,
	            @RequestPart(value="motherImg") MultipartFile motherImg,
	            @RequestParam(value="fatherBreed", required=false) String fatherBreed,
	            @RequestPart(value="fatherImg", required=false) MultipartFile fatherImg,
	            @RequestParam(value="price") String price,
	            @RequestParam(value="comment", required=false) String comment) {
	
		
		// handle null value token
	    if (token == null) {
	        logger.error("Authorization token is missing");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing");
	    }
	    
	    // remove Bearer prefix if present
	    token = token.replace("Bearer ", token);
	    logger.info("Received token: " + token);
		
		// if user is authenticated proceed to create a a new pet and assocciate this with the user pet listings
		if (petServiceImpl.authenticateUserByToken(token)) {
			
			// upload images to S3 and get URLs
		    String petImgUrl = petImg != null ? s3Service.uploadFile(petImg) : null;
		    String motherImgUrl = motherImg != null ? s3Service.uploadFile(motherImg) : null;
		    String fatherImgUrl = fatherImg != null ? s3Service.uploadFile(fatherImg) : null;
			
			Pet newPet;
			// define the type of pet object to be instantiated
			if(category.equals("cat")) {
				newPet = new Cat();
			}else {
				newPet = new Dog();
			}
			
			// try and double check user authentication, 
			// then set values of the newpet Object with the passed parameters
			// then delegate pet storing proccess to petServiceImpl
			try {
				Optional<User> foundUser = petServiceImpl.findUserByToken(token);
				if(foundUser.isPresent()) {  // double check user authentication
					newPet.setCategory(category);
					newPet.setBreed(breed);
					newPet.setLocation(location);
                    newPet.setBirthDate(LocalDate.of(Integer.parseInt(birthYear), Integer.parseInt(birthMonth), 1));
                    newPet.setUserId(foundUser.get().getUserId());
                    newPet.setMotherBreed(motherBreed);
                    newPet.setMotherImg(motherImgUrl);  // Convert to byte array
                    newPet.setFatherBreed(fatherBreed);
                    newPet.setFatherImg(fatherImgUrl);  // Convert to byte array
                    newPet.setPrice(Float.parseFloat(price));
                    newPet.setComment(comment);
                    newPet.setPetImg(petImgUrl != null ? petImgUrl : null);  // Convert to byte array
				}
				
				// pass new pet, and user Optional objects to service 
				petServiceImpl.savePet(foundUser, newPet);
				
				logger.info("New Pet successfully saved");
	            return ResponseEntity.status(200).body("New Pet successfully saved and assocciated to your listings"); 
			
			}catch(Exception e) {
				
                return ResponseEntity.status(500).body("Error saving pet: " + e.getMessage());
			}
						
		}else {
			logger.info("Unauthorized user");
            return ResponseEntity.status(403).body("Unauthorized");

		}
		
			
	}
	
	
	/**
	 * Endpoint to handle GET requests and retrieve paginated lists of pet listings
	 *  associated to the authenticated user
	 * 
	 * The parameters passed defined the number of pages and the number of items per page to retreive
	 * E.g   pageNo 1, items 20
	 *       pageNo 2, items 20 + 20
	 * 
	 * GET /pets/my_listings?token=token&page?page=0&size=10
	 * 
	 * @param token - the current session authentication token
	 * @param pageNo - the page number to retrieve is 0 as default and updated on each request
     * @param pageSize the number of records per page. It is set to 20 as default i not specified in request
     * @return a Page object containing the paginated list of cats
     *  @return ResponseEntity - an HTTP JSON object response with useful information about the request output
	 * */
	@GetMapping("/my_listings")
	public ResponseEntity<?>  getMyListings(
			@RequestParam (value = "token", required = false)  String token,
			@RequestParam (value = "order", required = false)  String order,
			@RequestParam (value = "pageNo", defaultValue ="0", required = false)  int pageNo, 
			@RequestParam (value = "pageSize", defaultValue = "6", required = false) int pageSize){
		
	    
		// check if user is authenticated
		if(petServiceImpl.authenticateUserByToken(token)) {
			
		    		
			// try and double check user authentication,
			// then create Pageable object,
			// then delegate data fetching request proccess to PetServiceImpl
			try {
			
					 List<PetList> petList = petServiceImpl.getUserPetList(token);
					 
					 // check if list is null
					 if(petList == null ) {
						 logger.info("Pet list is null");
					    return ResponseEntity.status(500).body("Error retrieving pet listings. List is null");
					 }
					 
					 // check if param indicates ascending or descendig order
					 if(order.equals("asc")) {
						 
						 // sort the pet list from latest to oldest
				         Collections.sort(petList, new PetListDateAscendingComparator());
				         
						 logger.info("List sorted in ascending order. Sending list...");
						 return ResponseEntity.ok(petList);

					 } else {
						 // sort the pet list from oldest to the latest
				         Collections.sort(petList, new PetListDateDescendingComparator());	
				         
						 logger.info("List sorted in ascending order.  Sending list...");
				         return ResponseEntity.ok(petList);
					 }
					 
					 
			}catch(Exception e) {
				return ResponseEntity.status(500).body("Error retrieving pet listings: " + e.getMessage());
			}
			
		}else {
			logger.info("Unauthorized user");
            return ResponseEntity.status(403).body("Unauthorized");

		}
		   
		
	}
	
	
	/**
	 * Endpoint to receive and manage pet DELETE requests
	 * 
	 * Steps:
	 * 1. Check for token null values
	 * 2. Authenticate user with passed token
	 * 3. Ensure user exists on repository
	 * 4. Call deletePetList() on PetServiceImpl to handle pet delete operation
	 * 
	 * @param token - the current session token
	 * @param petListId - the id of the petList instance to be deleted
	 * @return ResponseEntity - an HTTP JSON object response with useful information about the request output
	 ***/
	@DeleteMapping("/delete_pet")
	public ResponseEntity<String> deletePetList(
			     @RequestParam (value="token", required=false) String token,
			     @RequestParam(value="petListId", required=false)  String petListId ){
		

		// handle null value token
	    if (token == null || token.isEmpty()) {
	        logger.error("Authorization token is missing");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing");
	    }
	    
	    // log passed values
	    logger.info("Received token: " + token);
	    logger.info("Received petListId: " + petListId);
	    
	    
		try {
		    // check if user is authenticated
		    if(petServiceImpl.authenticateUserByToken(token)) { 
			    // ensure user exists in db
			     Optional<User> foundUser = petServiceImpl.findUserByToken(token);
			     if(foundUser.isPresent()) {  
				 
				      petServiceImpl.deletePetList(petListId);   // pass petList id
				
				      logger.info("Pet successfully removed from user listings");
	                  return ResponseEntity.ok("Pet successfully removed from you listings");
	            
			      }else {
	                   logger.info("User not found");
	                   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found or unauthorized");
	               }
			     
		       }else {
		    	   
			        logger.info("Unauthorized user");
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
		        }
			  
		 }catch (Exception e) {
			
              logger.error("Error deleting PetList: " + e.getMessage());
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting PetList: " + e.getMessage());      
	 }
	}
	
	
	/**
	 * Endpoint to update an existing pet.
	 * 
	 * This method handles updating a pet's information based on the provided form data and images.
	 * 
	 * Steps:
	 * 1. Check for null value on the passed token and ensure it's authenticated
	 * 2. Remove any possible token "Barear" 
	 * 3. Authenticate the user using the passed token
	 * 4. Upload images into AWS S3 bucket and retrieve their URLs
	 * 5. Create a PetUpdateData object to store the form data data fileds passsed in a object (PetUpdateData)
	 * 6. Pass the authenticated user and PetUpdateData object to the petServiceImpl for handling repository operations
	 * 
     * @return ResponseEntity - an HTTP JSON object response with useful information about the request output
	 */
	@PostMapping(value= "/update_pet", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)		
	public ResponseEntity<?> updatePet(
			@RequestParam("token") String token,
		    @RequestParam("petListingId") String petListingId,
		    @RequestParam("petId") String petId,
		    @RequestParam(value="category", required=false) String category,
		    @RequestParam(value= "breed") String breed,
		    @RequestParam(value="location") String location,
		    @RequestParam(value="birthMonth") String birthMonth,
		    @RequestParam(value="birthYear") String birthYear,
		    @RequestParam(value="motherBreed") String motherBreed,
		    @RequestPart(value="motherImg", required=false) MultipartFile motherImg,
		    @RequestParam(value="fatherBreed", required=false) String fatherBreed,
		    @RequestPart(value="fatherImg", required=false) MultipartFile fatherImg,
		    @RequestParam(value="price") String price,
		    @RequestParam(value="comment", required=false) String comment){
	
		
	        
	    logger.info(petListingId);
	    
	    if (token == null) {
	        logger.error("Authorization token is missing");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing");
	    }
	    
	    
	    // remove Bearer prefix if present
	    token = token.replace("Bearer ", token);
	    logger.info("Received token: " + token);
		
	    
		// if user is authenticated proceed to create a a new pet and assocciate this with the user pet listings
		if (petServiceImpl.authenticateUserByToken(token)) {
				

		    
		    // instantiate PetUpdateData to capture the form data passed in an object for data managing and methods communication
		    PetUpdateData formData = new PetUpdateData();
		    
		    formData.setPetId(petId);  // object id need to be string to set form
		    formData.setCategory(category);
		    formData.setBreed(breed);
		    formData.setLocation(location);
	        // create a LOcalDate instance and set values with parsed data
		    formData.setBirthMonth(birthMonth);
		    formData.setBirthYear(birthYear);
		    formData.setMotherBreed(motherBreed);
		    formData.setMotherImg(motherImg);
		    formData.setFatherBreed(fatherBreed);
		    formData.setFatherImg(fatherImg);
		    formData.setPrice(price);
		    formData.setComment(comment);
			   
			   
			// try and double check user authentication, 
			// handle foundUser and formData(as PetUpdateData object) to  petServiceImpl.updatePet() for update operation task
			try {
				if (petServiceImpl.authenticateUserByToken(token)) {
					
	                Optional<User> foundUser = petServiceImpl.findUserByToken(token);
	                User user = foundUser.get(); // cast optional into a User
	                
	                // delegate operation with object params (Optima<user>, PetList , PetUpdateData)
	                petServiceImpl.updatePet(user, petListingId, formData);   
	                
	                return ResponseEntity.ok("Pet updated successfully");
	                
				} else {
					
					logger.info("Not found user");
					return ResponseEntity.status(404).body("User could not be found"); 
			     }
				
			}catch(Exception e) {
				logger.info("Error updating pet: " + e.getMessage());
                return ResponseEntity.status(500).body("Error updating pet: " + e.getMessage());
			}
						
		}else {
			logger.info("Unauthorized user");
            return ResponseEntity.status(403).body("Unauthorized");

		}
		
			
	}
	
	
	
}

