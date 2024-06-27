/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.bb_pets_adoption.auth.model.User;
import com.example.bb_pets_adoption.auth.repository.UserRepository;
import com.example.bb_pets_adoption.auth.service.AuthenticationServiceImpl;
import com.example.bb_pets_adoption.pet_listing.controller.PetController;
import com.example.bb_pets_adoption.pet_listing.model.Cat;
import com.example.bb_pets_adoption.pet_listing.model.Dog;
import com.example.bb_pets_adoption.pet_listing.model.Pet;
import com.example.bb_pets_adoption.pet_listing.model.PetList;
import com.example.bb_pets_adoption.pet_listing.repository.CatRepository;
import com.example.bb_pets_adoption.pet_listing.repository.DogRepository;
import com.example.bb_pets_adoption.pet_listing.repository.PetListRepository;

/**
 * Class to host business logic around pet operations
 */
@Service
public class PetServiceImpl implements PetService{

	// vars
	private CatRepository catRepository;
	private DogRepository dogRepository;
	private PetListRepository petListRepository;
	private AuthenticationServiceImpl authenticationServiceImpl;


	// create an instance of Logger
	private static final Logger logger = LoggerFactory.getLogger(PetController.class);
	
	@Autowired
	UserRepository userRepository;

	/**
	 * Constructor to inject dependencies
	 * 
	 * @param catRepository
	 * @param dogRepository
	 * */
	@Autowired
    public PetServiceImpl(CatRepository catRepository, DogRepository dogRepository, 
    		AuthenticationServiceImpl authenticationServiceImpl, PetListRepository petListRepository) {
        this.catRepository = catRepository;
        this.dogRepository = dogRepository;
        this.authenticationServiceImpl = authenticationServiceImpl;
        this.petListRepository = petListRepository;
    }

	
	/**
	 * Method to retrieve all cats from dogs repository
	 * 
	 * */
	@Override
	public Page<Cat> findAllCats(Pageable pageable) {
		return catRepository.findAll(pageable);
	}

	
	/**
	 * Method to retrieve all dogs from dogs repository
	 * 
	 * */
	@Override
	public Page<Dog> findAllDogs(Pageable pageable) {
		// TODO Auto-generated method stub
		return dogRepository.findAll(pageable);
	}

	
	/**
	 * Method to retreive a cat by ID
	 * 
	 * */
	@Override
	public Optional<Cat> findCatById(String petId) {
		
		 ObjectId objectId;
	        try {
	            objectId = new ObjectId(petId);
	            System.out.println(objectId);
	        } catch (IllegalArgumentException e) {
	            return Optional.empty();
	        }
	        return catRepository.findCatById(objectId);
	}

	/**
	 * Method to retreive dog by ID
	 * 
	 * */
	@Override
	public Optional<Dog> findDogById(String petId) {
		ObjectId objectId;
        try {
            objectId = new ObjectId(petId);
            System.out.println(objectId);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
		return dogRepository.findDogById(objectId);
	}
	
	
	/**
	 * Method to retrieve all cats by tags from cats repository
	 * */
	@Override
	public Page findAllCatsByTags(List tags, Pageable pageable) {
		return catRepository.findByTagsIn(tags, pageable);

	}


	/**
	 * Method to retrieve all dogs by tags from dogs repository
	 * */
	@Override
	public Page findAllDogsByTags(List tags, Pageable pageable) {
		return dogRepository.findByTagsIn(tags, pageable);
	}


	/*
	 *  Method to save a new Cat instance into cats repository
	 * */
	@Override
	public Cat createNewCat(Cat newCat){
		
         return catRepository.save(newCat);
	}
	
	/*
	 * Method to save a new Dog into dogs repository
	 * */
	public Dog createNewDog(Dog newDog){
		
         return dogRepository.save(newDog);
	}

    /**
    * 
    * */
	@Override
	public boolean authenticateUserByToken(String token) {

		return authenticationServiceImpl.authenticate(token);
	}


    /**
     * 
     * **/
	@Override
	public Optional<User> findUserByToken(String token) {

		Optional<User> user = userRepository.findByToken(token);
        
		return user;
	}

	 /**
     * Method responsible for storing objects into their corresponding repositories and 
     * managing associations user-pet  pet-user  petList-user-pet
     *
     *NOTE: The order of events are crucial for proper associations
     *
     * Steps:
     * 1. Initialize a PetList object
     * 2. Ensure the pet category and save in appropiate repository
     * 3. Asscociate instances of User and Pet to the new petList instance, then save
     * 4. Add the new petList instance into user's petListings list
     *
     *  
     * @param foundUser - the Optional user found by the token
     * @param newPet  -  the new pet to be saved
     * @throws Exception if there is an error while saving the pet
     */
	public void savePet(Optional<User> foundUser, Pet newPet) throws Exception {

		
		User user = foundUser.get();        // cast Optional Object into a User instance
		PetList newPetList = new PetList(); // instantiate a PetList object
		
	
		// try and set pet providerId to the user's id, 
		// then check pet's category to save in the appropiate collection
		try {
			 // associate user with pet listed
			 newPet.setProviderId(user.getUserId());

			 // check if new pet is ca or dog to store it in cats collection
			 if(newPet.getCategory().equals("cat")) {
				 catRepository.save((Cat) newPet);  // cast Pet into a Cat object
				 logger.info("" +catRepository.findById(newPet.getId()));
			 }// check if new pet is cat or dog to store it in dogs collection
			 else if(newPet.getCategory().equals("dog")) {
				 dogRepository.save((Dog) newPet);  // cast Pet into a Dog object
				 
			 }else {
		            throw new Exception("Unknown pet category: " + newPet.getCategory());
		        }
			 
			}catch(Exception error) {
				logger.error("Something went wrong and pet culd not be saved into user pets list. " + error.getMessage());
				throw new Exception("Pet could not be associated with user");
			}
		
		
		
		// try and set the values for the new petList instance, then save in database
		try {
			
			newPetList.setCreatedOn(LocalDate.now());
			newPetList.setUserId(user.getUserId());
			newPetList.setPet(newPet);
			petListRepository.save(newPetList);
			
		}catch(Exception error) {
					
			logger.error("Something went wrong and new pet list could not be saved into database. " + error.getMessage());
			throw new Exception("Error while storing the new PetList object. " + error.getMessage());
			}
					
		
		   // try and save new PetList object into user's pet listings 
		try {
					
			if(user.getPetList() != null) {
			user.getPetList().add(newPetList);
			userRepository.save(user);
			};
				 
		 }catch(Exception error) {
					
			logger.error("Something went wrong and pet could not be saved into user pets list. " + error.getMessage());
			throw new Exception("Pet could not be associated with user");
		}
				
		
	 }
	
	
	/**
	 * Method retrieves the list of pet listings associated with a user
	 *
	 * It uses the current session token to find the user,
	 * 
	 * Steps:
	 * 1. Uses the token to find the user by calling the 'findUserByToken' class method
	 * 2. If user is found, it retrieves and returns the list of pet listings 'petList' associated to the user
	 * 3. If user is found, it throws an exception indicating that the user was not found
	 *
	 * @param token - the authentication token to identify the user
	 * @return a list of 'PetList' objects associated to the user
	 * @throws Exception if the user is not found
	 */
	public List<PetList> getUserPetList(String token) throws Exception{
		
			Optional<User> foundUser = this.findUserByToken(token);
			
		    // double check user authentication
			if(foundUser.isPresent()) {
							
			    User user = foundUser.get();  // cast into user instance
			    
				logger.info("User found.");

			    return user.getPetList();		
							 
			}else {
                throw new Exception("User not found");
            }
	    }

	
	/**
	* Method responsible for removing a PetList instance from database
	*  and initiate a cascading deletion for all items related to the PetList instance such as the Pet object.
	*  
	* Steps:
	* 1. Cast string id into Object id fro database operations
	* 2. Check if item is in database
	* 3. Initiate cascading deletion for the Pet object linked
	* 4. Remove PetList from database
	* 
	* @param petList - the pet list instance id as string
	* @throws Exception - if an error occurs during proccess
	* **/
	@Override
	public void deletePetList(String petListId) throws Exception{
		
		// handle null value petListId
	    if (petListId == null || petListId.isEmpty()) {
	    	
	        throw new IllegalArgumentException("PetListId cannot be null or empty");
	    }
	    
	    // convert petListId string into an ObjectId instance for database operations
	    ObjectId objectId;
	    try {
	        objectId = new ObjectId(petListId);
	    } catch (IllegalArgumentException e) {
	        throw new IllegalArgumentException("Invalid PetListId format: " + petListId, e);
	    }
	    
	    
		// try and proceed with items cascading deletion, then remove the item from database
		try {		
			// find item in database by its id
			Optional<PetList> foundPetList = petListRepository.findById(objectId);
			
			// if item is present...
			if(foundPetList.isPresent()) {
				
				// initiate for cascading deletion passing the repository references neede for peration
				foundPetList.get().cascadeDelete(catRepository, dogRepository); 
				
				// remove the the current petlist instance
				petListRepository.deleteById(objectId);  
	            logger.info("PetList with id " + petListId + " successfully deleted");

			}else {		
				
				logger.info("PetList object could not be found in database." );      
			}
			
			
		}catch(Exception e) {
			
			throw new Exception("An error occured and the PetList object could not be removed from database. " + e.getMessage());
			
		}
		
	}

	/**
	* Method for removing a user from database
	* 
	* @param petList - the pet list instance
	 * **/
	@Override
	public void deleteUser(User user) {

		user.cascadeDelete(petListRepository, catRepository, dogRepository);  // remove exiting items from both collections
        userRepository.delete(user);          // remove user
 
	}
	
}
