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
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bb_pets_adoption.adoption.service.AdoptionApplicationService;
import com.example.bb_pets_adoption.adoption.service.AdoptionApplicationServiceImpl;
import com.example.bb_pets_adoption.auth.model.User;
import com.example.bb_pets_adoption.auth.repository.UserRepository;
import com.example.bb_pets_adoption.auth.service.AuthenticationServiceImpl;
import com.example.bb_pets_adoption.pet_listing.controller.PetController;
import com.example.bb_pets_adoption.pet_listing.model.Cat;
import com.example.bb_pets_adoption.pet_listing.model.Dog;
import com.example.bb_pets_adoption.pet_listing.model.Pet;
import com.example.bb_pets_adoption.pet_listing.model.PetList;
import com.example.bb_pets_adoption.pet_listing.model.PetUpdateData;
import com.example.bb_pets_adoption.pet_listing.repository.CatRepository;
import com.example.bb_pets_adoption.pet_listing.repository.DogRepository;
import com.example.bb_pets_adoption.pet_listing.repository.PetListRepository;
import com.example.bb_pets_adoption.pet_listing.service.S3Service;
import jakarta.servlet.http.HttpServletRequest;

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
	private AdoptionApplicationServiceImpl adoptionApplicationServiceImpl;
	UserRepository userRepository;
	S3Service s3Service;

	// create an instance of Logger
	private static final Logger logger = LoggerFactory.getLogger(PetController.class);

	
	
	/**
	 * Constructor for dependancies injection
	 * 
	 * @param catRepository
	 * @param dogRepository
	 * @param userRepository
	 * @param authenticationServiceImpl
	 * @param petListRepository
	 *  @param s3Service
	 * */     
	@Autowired
    public PetServiceImpl(CatRepository catRepository, DogRepository dogRepository, UserRepository userRepository,
    		AuthenticationServiceImpl authenticationServiceImpl, PetListRepository petListRepository,
    		 S3Service s3Service, AdoptionApplicationServiceImpl adoptionApplicationServiceImpl) {
        this.catRepository = catRepository;
        this.dogRepository = dogRepository;
        this.authenticationServiceImpl = authenticationServiceImpl;
        this.petListRepository = petListRepository;
        this.s3Service = s3Service;
        this.userRepository = userRepository;
        this.adoptionApplicationServiceImpl = adoptionApplicationServiceImpl;
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
	  @param petId - string representation of the petId
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
	 * @param petId - string representation of the petId
	 * */
	@Override
	public Optional<Dog> findDogById(String petId) {
		
		// convert string id into an ObjectId to be accepted by repository operation by id
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
	 *  
	 * @param newCat - the cat object to be saved into repository
	 * */
	@Override
	public Cat createNewCat(Cat newCat){
		
         return catRepository.save(newCat);
	}
	
	
	/*
	 * Method to save a new Dog into dogs repository
	 * 
	 * @param newDog - the dog object to be saved into repository
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
			newPetList.setPetId(newPet.getId());   // pets need to be saved before this step to ensure the ID is generated
			newPetList.setStatus("available");
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
	* Method for removing all related adoption applications
	* Uses AdoptionApplicationServiceImpl service for delete operation
	* Traces any exceptions during process
	* 
	* @param petId - the petId to search for
	 * **/
	@Override
	public void deleteAdoptionApplications(ObjectId petId) {

		try {		
			adoptionApplicationServiceImpl.deleteAllApplicationsByPetId(petId);
		
		} catch (Exception e) {
			e.printStackTrace();
		}  
 
	}
	
	/**
	* Method for removing a user from database
	* 
	* @param user - the user instance
	 * **/
	@Override
	public void deleteUser(User user) {

		user.cascadeDelete(petListRepository, catRepository, dogRepository);  // remove exiting items from both collections
        userRepository.delete(user);          // remove user
 
	}
	
	
	/**
     * Method responsible for updating existing pet objects in their corresponding repositories and
     * managing associations between user, pet, and petList.
 
     * Steps:
     * 1. Ensure the pet ID is valid and convert it into an ObjectId instance
     * 2. Check the pet category and find it in the appropriate repository
     * 3. Update the pet fields with the provided form data
     * 4. Save the updated pet object to the repository
     * 
     *
     * @param foundUser - the Optional user found by the token
     * @param formData  - the form data containing updated pet information
     * @throws Exception if there is an error while updating the pet
     */
	public void updatePet(User user, String petListingId, PetUpdateData  formData ) throws Exception {


	        		
	    // check if pet is a cat, find it in repository, update object, and save
	    if(formData.getCategory().equals("cat")) {
				 
			Optional<Cat> foundCat = catRepository.findById(new ObjectId(formData.getPetId())); 
		    logger.info("Cat found in repository");
			
		    if(foundCat.isPresent()) {
					 
				Cat cat = foundCat.get();    // cast optional into a Cat
				updatePetFields(cat, formData); // use helper method to set pet fields with new data
				catRepository.save(cat);        // save cat
				logger.info("Pet successfully updated");
				 
				updatePetListObject(petListingId, cat); // update PetList object and save it ensuring the pet field is also updated
                logger.info("PetList instance successfuly updated");


			}else {
		        throw new Exception("Cat not found");
		    }
				 
		}// check if if pet is a dog, find it in repository, update object, and save
		else if(formData.getCategory().equals("dog")) {
				 
			Optional<Dog> foundDog = dogRepository.findById(new ObjectId(formData.getPetId()));  
			logger.info("Dog found in repository");

			if (foundDog.isPresent()) {
				
		          Dog dog = foundDog.get();  // cast optional into a Dog
		          updatePetFields(dog, formData);  // use helper method to set pet fields with new data
		          dogRepository.save(dog);
		          
		          updatePetListObject(petListingId, dog); // update PetList object and save it ensuring the pet field is also updated
	              logger.info("PetList instance successfuly updated");
	              
		      } else {
		           throw new Exception("Dog not found");
		      }
				 
		}else {
		     throw new Exception("Unknown pet category: " + formData.getCategory());
		  }
	    

	 }
	
	
	/***
	 * Helper method responsible for updating the exiting pet fields with new data passed on PetUpdateData intance
	 * 
	 * Method also checks if motherImg and fatherImg fields passed on formData are empty.
	 * If empty or null, pet related fields are reset with the previous image uploaded.
	 * This ensures that image fields are correctly handled and updated.
	 * 
	 * @param pet - the pet object to be updated
	 * @param updateData - the new data for the existing pet
	 * **/
	private void updatePetFields(Pet pet, PetUpdateData formData) {
		
		
		// to upload images to S3 and get URLs
		// fisrt check if formData image fields are null or empty, and if so, set the image field with previous img uploaded
	    String motherImgUrl;
	    String fatherImgUrl;
	    
	    // check for mother image
		if (formData.getMotherImg() != null && !formData.getMotherImg().isEmpty()) {
            motherImgUrl = s3Service.uploadFile(formData.getMotherImg());
        } else {
            motherImgUrl = pet.getMotherImg(); // Retain existing image URL
        }
		
		 // check for father image
		 if (formData.getFatherImg()!= null && !formData.getFatherImg().isEmpty()) {
              fatherImgUrl = s3Service.uploadFile(formData.getFatherImg());
          } else {
              fatherImgUrl = pet.getFatherImg(); // Retain existing image URL
          }
		   
		   
		pet.setCategory(formData.getCategory());
        pet.setBreed(formData.getBreed());
        pet.setLocation(formData.getLocation());
        // create a LOcalDate instance and set values with parsed data
        pet.setBirthDate(LocalDate.of(Integer.parseInt(formData.getBirthYear()), Integer.parseInt(formData.getBirthMonth()), 1));
        pet.setMotherBreed(formData.getMotherBreed());
        pet.setMotherImg(motherImgUrl);
        pet.setFatherImg(fatherImgUrl);
        pet.setMotherBreed(formData.getMotherBreed());
        pet.setPrice(Float.parseFloat(formData.getPrice()));
        pet.setComment(formData.getComment());
    }
	
	
	/***
	 * Helper method responsible for updating the exiting PetList instance with the updated pet data
	 * 
	 * Steps:
	 * 
	 * 1. Convert string is into a an ObjectId
	 * 2. Find object in db
	 * 3. Set new values for object fields ( pet, updatedOn)
	 * 4. Save changes
	 * 
	 * @param pet - the pet object to be updated
	 * @param updateData - the new data for the existing pet
	 * @throws Exception if there is an error while updating the PetList object
	 * **/
	private void updatePetListObject(String petListingId, Pet pet) throws Exception {
		
		// convert petListIdString and into an ObjectId
		ObjectId petListId;
		if(petListingId != null || !petListingId.isEmpty()){
		    petListId = new ObjectId(petListingId);
		}else {	
				throw new IllegalArgumentException("PetListId cannot be null or empty");	
		}
		
		
		// try and find the current PetList instance in db, update updateOn field, then save changes
		// catch any errors during process
		try {
			Optional<PetList> foundPetList = petListRepository.findById(petListId);
			if(foundPetList.isPresent()) {
				
				PetList petList = foundPetList.get();   // cast optional into PetList instance	
				petList.setPet(pet);                    // set pet field with pet changes
				petList.setUpdatedOn(LocalDate.now());  // set updatedOn field with current date
                petListRepository.save(petList);     // save changes
			}
		
		}catch(Exception e) {
			
			throw new Exception("An error occured and the PetList instance for the current updated operation could not be updated." + e.getMessage());

		}
 
    }
	
	
}
