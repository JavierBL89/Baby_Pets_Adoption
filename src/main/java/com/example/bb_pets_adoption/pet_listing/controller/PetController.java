/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.controller;

import java.time.LocalDate;
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
import com.example.bb_pets_adoption.pet_listing.service.PetServiceImpl;

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
	

	// create an instance of Logger
	private static final Logger logger = LoggerFactory.getLogger(PetController.class);
		
		
	/**
	  * Constructor injection for PetServiceImpl
	  * */
	 @Autowired
	 public PetController( PetServiceImpl petServiceImpl) {
	        this.petServiceImpl = petServiceImpl;
	        
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
     * @return ResponseEntity with a message indicating the result of the operation.
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
                    newPet.setProviderId(foundUser.get().getUserId());
                    newPet.setMotherBreed(motherBreed);
                    newPet.setMotherImg(motherImg != null ? motherImg.getBytes() : null);  // Convert to byte array
                    newPet.setFatherBreed(fatherBreed);
                    newPet.setFatherImg(fatherImg != null ? fatherImg.getBytes() :null);  // Convert to byte array
                    newPet.setPrice(Float.parseFloat(price));
                    newPet.setComment(comment);
                    newPet.setPetImg(petImg != null ? petImg.getBytes() : null);  // Convert to byte array
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
	
}

