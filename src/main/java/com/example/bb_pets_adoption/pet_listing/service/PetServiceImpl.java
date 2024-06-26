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
     * Method manages pet-user association and saves the pet to the appropriate repositor
     *
     * Steps:
     * 1. Initialize a PetList object
     * 2. Set object attributes values 
     * 3. Check the pet category and use the appropiate repository to save the new pet
     * 4. Save the new PetList into user's petListings list
     *
     *  
     * @param foundUser - the Optional user found by the token
     * @param newPet  -  the new pet to be saved
     * @throws Exception if there is an error while saving the pet
     */
	public void savePet(Optional<User> foundUser, Pet newPet) throws Exception {

		
		User user = foundUser.get();        // cast Optional Object into a User instance
		PetList newPetList = new PetList(); // instantiate a PetList object
		
		// try and set values for the new PetList instance
		try {
					
			newPetList.setCreatedOn(LocalDate.now());
			newPetList.setUser(user);
			petListRepository.save(newPetList);
			
		}catch(Exception error) {
					
			logger.error("Something went wrong and new pet list could not be saved into database. " + error.getMessage());
			throw new Exception("Error while storing the new PetList object. " + error.getMessage());
			}
				

		
		// try and set pet providerId to the user's id, 
		// then check pet's category to save in the appropiate collection
		try {
			 // associate user with pet listed
			 newPet.setProviderId(user.getUserId());

			 // check if new pet is ca or dog to store it in cats collection
			 if(newPet.getCategory().equals("cat")) {
				 catRepository.save((Cat) newPet);  // cast Pet into a Cat object
				 
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

}
