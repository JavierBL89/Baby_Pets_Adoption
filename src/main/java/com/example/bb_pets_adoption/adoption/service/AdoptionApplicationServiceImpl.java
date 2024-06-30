/**
 * 
 */
package com.example.bb_pets_adoption.adoption.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bb_pets_adoption.adoption.model.AdoptionApplication;
import com.example.bb_pets_adoption.adoption.repository.AdoptionApplicationRepository;
import com.example.bb_pets_adoption.auth.model.User;
import com.example.bb_pets_adoption.auth.repository.UserRepository;
import com.example.bb_pets_adoption.auth.service.AuthenticationServiceImpl;
import com.example.bb_pets_adoption.pet_listing.model.PetList;
import com.example.bb_pets_adoption.pet_listing.repository.PetListRepository;
import com.example.bb_pets_adoption.search.controller.SearchController;


/**
 * Contains business logic for creating, updating, retrieving, and deleting adoption applications.
 */
@Service
public class AdoptionApplicationServiceImpl implements AdoptionApplicationService{

	
    AdoptionApplicationRepository adoptionApplicationRepository;
	AuthenticationServiceImpl authenticationServiceImpl;
	UserRepository userRepository;
	PetListRepository petListRepository;
	
	

	// create an instance of Logger
     private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
     
    /***
     * Constructor for dependencies injection
     * 
     * @param adoptionApplicationRepository - 
     * **/
	@Autowired
	public AdoptionApplicationServiceImpl (AdoptionApplicationRepository adoptionApplicationRepository, AuthenticationServiceImpl authenticationServiceImpl,
			UserRepository userRepository, PetListRepository petListRepository) {
		
		this.adoptionApplicationRepository = adoptionApplicationRepository;
		this.authenticationServiceImpl  = authenticationServiceImpl;
		this.userRepository = userRepository;
		this.petListRepository = petListRepository;
	}
	

	/**
	 * 
	 * **/
	@Override
	public Optional<AdoptionApplication> getApplicationById(ObjectId id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	/**
	 * 
	 * **/
	@Override
	public List<AdoptionApplication> getApplicationsByUserId(ObjectId userId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * 
	 * **/
	@Override
	public List<AdoptionApplication> getApplicationsByPetId(ObjectId petId) {
		// TODO Auto-generated method stub
		return null;
	}


    /***
     * 
     * **/
	@Override
	public boolean authenticateUserByToken(String token) {
          
		return authenticationServiceImpl.authenticate(token);
	}
	
	

    /***
     * 
     * **/
	@Override
	public Optional<User> findUserByToken(String token) {
          
	          Optional<User> user = userRepository.findByToken(token);
        
		return user;
	}



    /***
     * Method is responsible for the business logic of creating an adoption application, 
     * and associates it to the pet.
     * The association is done through the PetList instance which contains information about the the pet, 
     * the user, along with a list of all adoption application IDs related to the pet.
     * 
     * Steps:
     * 1. Check the PetList object can be found before proceeding with operation, or exit it.
     * 2. Instantiate a new AdoptionApplication object
     * 3. Set values of this with the current application data (pet, user, comments, etc)
     * 4. Add aplication id into PetList getAdoptionApplication list
     * 5. Save changes
     * 4. Save adoption application 
     * @throws Exception - if something goes wrong
     **/
	@Override
	public AdoptionApplication createApplication(String petIdString, User user, String comments) throws Exception {
		
		ObjectId petId;
		// check if object petId is null or not a valid hexstring       
		if(petIdString != null || !petIdString.isEmpty()) {
			
			 petId = new ObjectId(petIdString); // convert string IDs into an ObjectId
			 
		}else {
			throw new Exception ("Invalid pet ID provided. Pet ID is null or empty");
		}
		
		 
				
		try {
        // find and update Petlist instance with the new application id added into adoptionApplicationIDs list.
         Optional<PetList> foundPetList = petListRepository.findByPetId(petId);
        
         if(foundPetList.isPresent()) {
        	 
        	    PetList petList = foundPetList.get();  // cast optional into PetList instance 	 
        	    AdoptionApplication application = new AdoptionApplication();  // instantiate an AdoptionApplication object
                application.setPetId(petId);
                application.setUserId(user.getUserId());
                application.setComments(comments);
                application.setStatus("Pending");
                application.setApplicationDate(LocalDate.now());
                
                // save adoption application to generate ID
        	    adoptionApplicationRepository.save(application);
        	    
        	    // add the application id to the adoptionApplicationIDs list 
                petList.getAdoptionApplicationIDs().add(application.getId());
        	    petListRepository.save(petList);  // save changes 	    
        	    logger.info("Adoption application ID was added to adoptionApplicationsIDs list in PetList object with id " + petList.getId());
        	       	    
        	    adoptionApplicationRepository.save(application);  // save adoption application
        	    logger.info("Adoption application successfully saved");

                return application;
        	 
         }else {
        	 
        	 throw new Exception("PetList asssociated to pet id " + petId + " was not found.");
         }
         
		}catch(Exception e) {
			
			throw new Exception("Error creating adoption aplication. " + e.getMessage());
		}
      
	}

	
	/**
	 * 
	 * **/
	@Override
	public AdoptionApplication updateApplicationStatus(ObjectId id, String status) {
		 Optional<AdoptionApplication> applicationOpt = adoptionApplicationRepository.findById(id);
	        if (applicationOpt.isPresent()) {
	            AdoptionApplication application = applicationOpt.get();
	            application.setStatus(status);
	            return adoptionApplicationRepository.save(application);
	        }
	        return null;
	}

	
	/**
	 * 
	 * **/
	@Override
	public void deleteApplication(User user ,String petIdString, String applicationIdString) throws Exception{
		
		// check if object petId is null or not a valid hexstring       
		if(petIdString == null || petIdString.isEmpty()) {
			  throw new Exception ("Invalid pet ID provided. Pet ID is null or empty");
		}
		
		// check if object petId is null or not a valid hexstring       
		if(applicationIdString == null || applicationIdString.isEmpty()) {
			   throw new Exception ("Invalid applicationId provided. Application ID is null or empty");
		}
	    
		
		// convert string IDs into an ObjectId
		ObjectId petId; 
		ObjectId applicationId;
		
		try {
	        petId = new ObjectId(petIdString);
	    } catch (IllegalArgumentException e) {
	        throw new Exception("Invalid pet ID. Pet ID is not a valid ObjectId: " + petIdString, e);
	    }
		
		try {
	        applicationId = new ObjectId(applicationIdString);
	    } catch (IllegalArgumentException e) {
	        throw new Exception("Invalid application ID. Application ID is not a valid ObjectId: " + applicationIdString, e);
	    }
		
		
		// try and find the Petlist object related to the current pet by the petId
		PetList petList;
		try {
			
            Optional<PetList> foundPetList = petListRepository.findByPetId(petId);
            if(foundPetList.isPresent()) {
            	petList = foundPetList.get();
            }else {
           	 
           	 throw new Exception("PetList asssociated to pet id " + petId + " was not found.");
            }               
		}catch(Exception e) {
			
			throw new Exception("Error retrieving PetList related to pet ID " + petId + " " + e.getMessage());
		}
		
		
		
		 //try and find and delete the AdoptionApplication instance
        try {
             Optional<AdoptionApplication> foundApplication = adoptionApplicationRepository.findById(applicationId);
    
             if(foundApplication.isPresent()) {

            	   // remove application id from PetList adoptionApplicationIds list
            	    petList.removeApplicationId(applicationId);
            	    petListRepository.save(petList); // save changes to PetList
            	    
            	    // delete adoption application
                    adoptionApplicationRepository.deleteById(applicationId);
            	    logger.info("Adoption application successfully deleted");

             }else {
            	 
            	 throw new Exception("PetList asssociated to pet id " + petId + " was not found.");
             }
             
    		}catch(Exception e) {
    			
    			throw new Exception("Error deleting adoption aplication." + e.getMessage(), e);
    	}
	}
	
	
	
	/**
	 * Method handles the deletion of all applications related to a pet in database
	 * 
	 * When a user deletes a PetList from clien-side, all applications related to that pet are deleted in cascade
	 * 
	 * Steps:
	 * 1. Find and store all applications matching the petId
	 * 2. Remove all aplication objets from repository
	 * @param petId - the petId related to applications to be deleted
	 * @exception message - any errors during operation
	 ***/
	 public void deleteAllApplicationsByPetId(ObjectId petId) throws Exception{
		 
		 try {
		     // store in a list all applications related to the pet
		     List<AdoptionApplication> list =  adoptionApplicationRepository.findByPetId(petId);
		 
		     // iterate over list and remove each object from repository
		     for(AdoptionApplication app : list) {
			 
			     adoptionApplicationRepository.delete(app);
		     }
		 
	   }catch (Exception e) {
           throw new Exception("Error deleting adoption applications related to pet ID " + petId + ". " + e.getMessage(), e);
       }
	 }
}
