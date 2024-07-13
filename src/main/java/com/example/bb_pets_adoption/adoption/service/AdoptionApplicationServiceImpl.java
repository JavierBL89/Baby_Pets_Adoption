/**
 * 
 */
package com.example.bb_pets_adoption.adoption.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bb_pets_adoption.adoption.model.AdoptionApplication;
import com.example.bb_pets_adoption.adoption.repository.AdoptionApplicationRepository;
import com.example.bb_pets_adoption.account_management.Model.User;
import com.example.bb_pets_adoption.account_management.Repository.UserRepository;
import com.example.bb_pets_adoption.auth.service.AuthenticationServiceImpl;
import com.example.bb_pets_adoption.pet_listing.model.Cat;
import com.example.bb_pets_adoption.pet_listing.model.Dog;
import com.example.bb_pets_adoption.pet_listing.model.Pet;
import com.example.bb_pets_adoption.pet_listing.model.PetList;
import com.example.bb_pets_adoption.pet_listing.repository.CatRepository;
import com.example.bb_pets_adoption.pet_listing.repository.DogRepository;
import com.example.bb_pets_adoption.pet_listing.repository.PetListRepository;
import com.example.bb_pets_adoption.real_time_notifications.Model.Notification;
import com.example.bb_pets_adoption.real_time_notifications.Repository.NotificationRepository;
import com.example.bb_pets_adoption.real_time_notifications.service.NotificationServiceImpl;
import com.example.bb_pets_adoption.search.controller.SearchController;



/**
 * Contains business logic for creating, updating, retrieving, and deleting adoption applications.
 */
@Service
public class AdoptionApplicationServiceImpl implements AdoptionApplicationService{

	// vars
    AdoptionApplicationRepository adoptionApplicationRepository;
	AuthenticationServiceImpl authenticationServiceImpl;
	UserRepository userRepository;
	PetListRepository petListRepository;
	CatRepository catRepository;
	DogRepository dogRepository;
	NotificationServiceImpl notificationServiceImpl;
	NotificationRepository notificationRepository;
	

	// create an instance of Logger
     private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
     
    /***
     * Constructor for dependencies injection
     * 
     * @param adoptionApplicationRepository - 
     * **/
	@Autowired
	public AdoptionApplicationServiceImpl (AdoptionApplicationRepository adoptionApplicationRepository, AuthenticationServiceImpl authenticationServiceImpl,
			UserRepository userRepository, PetListRepository petListRepository, CatRepository catRepository, DogRepository dogRepository,
			NotificationServiceImpl notificationServiceImpl, NotificationRepository notificationRepository) {
		
		this.adoptionApplicationRepository = adoptionApplicationRepository;
		this.authenticationServiceImpl  = authenticationServiceImpl;
		this.userRepository = userRepository;
		this.petListRepository = petListRepository;
		this.dogRepository = dogRepository;
		this.catRepository = catRepository;
		this.notificationServiceImpl = notificationServiceImpl;
		this.notificationRepository = notificationRepository;
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
	public List<AdoptionApplication> getApplicationsByApplicantId(ObjectId userId) {
		// TODO Auto-generated method stub
		return null;
	}

	


	/**
	 * Method responsible for retrieving all applications related to a single pet by its ID
	 * using pagination.
	 * 
     * @param {String} petIdString - pet ID string of the pet to search for
	 * @param {Pageable} pageable -  the object with info about the pageNo, and pageSize
	 * @
	 * */
     @Override
     public Page<AdoptionApplication> getAllApplicationsByPetId(String petIdString, Pageable pageable) throws Exception {

    	// check if object petIdString is null or empty and convert into n ObjectId    
 		ObjectId petId;
 		if(petIdString != null) {		
 			 petId = new ObjectId(petIdString); // convert string IDs into an ObjectId
 			 
 		}else {
 			throw new Exception ("Invalid pet ID provided. Pet ID is null or empty");
 		}		
 		logger.info(adoptionApplicationRepository.findAllByPetId(petId, pageable).toString());
	       return adoptionApplicationRepository.findAllByPetId(petId, pageable);
     }

     
     
 	/**
 	 * Method responsible for retrieving all applications related to a single pet by its ID
 	 * using pagination.
 	 * 
      * @param {String} petIdString - pet ID string of the pet to search for
 	 * @param {Pageable} pageable -  the object with info about the pageNo, and pageSize
 	 * @
 	 * */
      @Override
      public Page<AdoptionApplication> getAllApplicationsByApplicantId(User user, Pageable pageable) throws Exception {
		
 	       return adoptionApplicationRepository.findAllByApplicantId(user.getUserId(), pageable);
      }
      
      
      
      /**
  	 * Method responsible for retrieving all applications related to a single pet by its ID
  	 * using pagination.
  	 * 
       * @param {String} petIdString - pet ID string of the pet to search for
  	 * @param {Pageable} pageable -  the object with info about the pageNo, and pageSize
  	 * @
  	 * */
       @Override
       public Page<AdoptionApplication> getAllApplicationsByStatus(String petIdString, Pageable pageable, String status) throws Exception {
        
    	   // ensure status is not null
    	   if(status == null || status.isEmpty()) {		   
    		   throw new Exception ("Status cannot be null or empty");
      		}		
    	   
    	   
      	    // check if object petIdString is null or empty and convert into n ObjectId    
   		    ObjectId petId;
   		    if(petIdString != null) {		
   		    	 petId = new ObjectId(petIdString); // convert string IDs into an ObjectId
   			 
   		    }else {
   			    throw new Exception ("Invalid pet ID provided. Pet ID is null or empty");
   		    } 		
  	           return adoptionApplicationRepository.findAllByStatus(petId, status, pageable);
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
     * It also checks for duplicate applications done by a user to a pet.
	 * If another application matching the user ID and the Pet ID, user must either cancel the new application or override the previous one
	 * 
     * Steps:
     * 1. Check the PetList object can be found before proceeding with operation, or exit it.
     * 2. Instantiate a new AdoptionApplication object
     * 3. Set values of this with the current application data (pet, user, comments, etc)
     * 4. Add aplication id into PetList getAdoptionApplication list
     * 5. Save changes
     * 4. Create a new 'adoption application notification'
     * 
     * @param {String} petIdString -  the string represenation of the pet ID
     * @param {String} petCateory - the pet category (i.e Cat, Dog...)
     * @param {User} user - the curent user
     * @param {String} comments - any comments on the application
     * @throws Exception - if something goes wrong
     **/
	@Override
	public AdoptionApplication createApplication(String petIdString, String petCategory, User user, String comments) throws Exception {
		
		// check if object petIdString is null or empty and convert into n ObjectId    
		ObjectId petId;
		if(petIdString != null) {			
			 petId = new ObjectId(petIdString); // convert string IDs into an ObjectId			 
		}else {
			throw new Exception ("Invalid pet ID provided. Pet ID is null or empty");
		}
			
		// retreive the pet from db based on its category to the save it within the adoption application
		Pet pet = this.getPetByCategory(petCategory, petId);

		try {
         // find and update Petlist instance with the new application id added into adoptionApplicationIDs list.
         Optional<PetList> foundPetList = petListRepository.findByPetId(petId);
 	     PetList petList = foundPetList.get();  // cast optional into PetList instance 	 

         if(foundPetList.isPresent()) {
        	 
        	    AdoptionApplication application = new AdoptionApplication();  // instantiate an AdoptionApplication object
                application.setPetId(petId);
                application.setPet(pet);
                application.setApplicantId(user.getUserId());
                application.setApplicant(user);
                application.setComments(comments);
                application.setStatus("Pending");
                application.setApplicationDate(LocalDate.now());
                
                // save adoption application to generate ID
        	    adoptionApplicationRepository.save(application);
        	    logger.info("Adoption application successfully saved with applicant ID: " + application.getApplicantId());
        	    
        	    // add the application id to the adoptionApplicationIDs list 
                petList.getAdoptionApplicationIDs().add(application.getId());
        	    petListRepository.save(petList);  // save changes 	    
        	    logger.info("Adoption application ID was added to adoptionApplicationsIDs list in PetList object with id " + petList.getId());
        	    
        	    // create a new notification
        	    notificationServiceImpl.createAdoptionApplicationNotification(petList, application, user, "New adoption application");       	                   
                
        	    return application;       	 
         }else {       	 
        	 throw new Exception("PetList asssociated to pet id " + petId + " was not found.");
         } 
         
		}catch(Exception e) {			
			throw new Exception("Error creating adoption aplication. " + e.getMessage());
		}    
	}

	
	
	/**
	 * Method responsible for handling application status update
	 * 
	 * Steps:
	 * 1. Check applicationIdString for null value and convert into OjectId
     * 2. Find the AdoptionApplication by ID in repository
     * 3. Set with statues and save changes
     * 
	 * @param {String} applicationIdString - the applicationId to work on
	 * @param {String} status - the new application status to be set
	 * **/
	@Override
	public void updateApplicationStatus(String applicationIdString, User user, String status) throws Exception{
		
		// check if object applicationIdString is null or empty and convert into an ObjectId   
		ObjectId applicationId;  
		if(applicationIdString != null) {				
			applicationId = new ObjectId(applicationIdString); // convert string IDs into an ObjectId		 
		}else {
			throw new Exception ("Invalid pet ID provided. Pet ID is null or empty");
		}
		
		
		// find the AdoptionApplication, set new status and save
		AdoptionApplication application;
		try {
		    Optional<AdoptionApplication> foundApplication = adoptionApplicationRepository.findById(applicationId);

		    if (foundApplication.isPresent()) {
	            application = foundApplication.get();  // cast optional into AdoptionApplication
	            application.setStatus(status);   // set with new status
	            // remove the the the pending adoption application notification
	             application.getPendingNotifications().removeIf(notification -> notification.getReceiverId().equals(user.getUserId()) && !notification.isViewed());
	             adoptionApplicationRepository.save(application);  // save changes
	             
	         	// create a new notification. Pass the the application object, and a message
		        notificationServiceImpl.createUpdateStatusNotification(application, user, "New status for application with reference " + application.getAppTracker());        
		    }else {        	
	        	throw new Exception("Application was not found.");
	        }
	        
		}catch(Exception e) {
			
			throw new Exception("Error updating adoption aplication. " + e.getMessage());
		}
	}

	
	/**
	 * Method manages and handle the deletion of a specific adoption application selected by the user
	 * 
	 * Steps:
	 * 1. Check for petIdString null or empty value
     * 2. Check for applicationIdString null or empty value
     * 3. Convert petIdString into an ObjectId for repository operations
     * 4. Convert applicationIdString into an ObjectId for repository operations
     * 5. Find a store the PetList instance associated to the pet
     * 6. Find the AdoptionApplication in database
     * 7. Delete the AdoptionApplication from the Petlist adoptionApplicationsIDs list
     * 8. Delete the AdoptionApplication instance from database
     * 
     * @param {User} user - the user applicant who application belongs to
     * @param {String} applicationIdString - the string representation of the application ID
     * @param
	 * **/
	@Override
	public void deleteApplication(User user , String applicationIdString) throws Exception{
		
		// check if object petId is null or not a valid hexstring       
		if(applicationIdString == null || applicationIdString.isEmpty()) {
			   throw new Exception ("Invalid applicationId provided. Application ID is null or empty");
		}	
		
		// convert string IDs into an ObjectId
		ObjectId applicationId;				
		try {
	        applicationId = new ObjectId(applicationIdString);
	    } catch (IllegalArgumentException e) {
	        throw new Exception("Invalid application ID. Application ID is not a valid ObjectId: " + applicationIdString, e);
	    }
		
		
		/**
		 * Find the AdoptionApplication object related to the current pet by the petId
		 * */
		AdoptionApplication adoptionApplication;	
        Optional<AdoptionApplication> foundApplication = adoptionApplicationRepository.findById(applicationId);  
         if(foundApplication.isPresent()) {
              adoptionApplication = foundApplication.get();
          }else {       	 
   	          throw new Exception("Adoption application with id " + applicationId + " was not found.");
          }           	        
		
		
		/**
		 *1. Find the Petlist object related to the current pet by the petId
		 *2. Remove any pending notifications related to the applicant who is dropping the application
		 *3. Notify the user that application "ID" is been dropped by athe applicant
		 * */
        PetList petList;
        Optional<PetList> foundPetList = petListRepository.findByPetId(adoptionApplication.getPetId());
            if(foundPetList.isPresent()) {
            	petList = foundPetList.get();  // cast optional into Petlist
            	//remove any pending notifications
            	petList.getPendingNotifications().removeIf(notification -> notification.getSenderId().equals(user.getUserId()));
            	
            	Notification notification = new Notification();
            	notification.setSenderId(user.getUserId());
            	notification.setReceiverId(petList.getUserId());
            	notification.setType("drop");
            	notification.setMessage("User " + user.getName() + " has dropped its application");
            	notificationRepository.save(notification);
            	// petList.getPendingNotifications().add(notification);  NOTE:we leave this out for now
            	
            }else {          	 
           	 throw new Exception("PetList asssociated to pet id " + adoptionApplication.getPetId() + " was not found.");
            }          
		
		
		
		/**
		 * - Remove applicationId from PetList instance and save changes
		 * - Lastly remove the adoption application
		 * */
	    try {
            // remove application id from PetList adoptionApplicationIds list
            petList.removeApplicationId(applicationId);
            petListRepository.save(petList); // save changes to PetList
            	    
            // delete adoption application
            adoptionApplicationRepository.deleteById(applicationId);
            logger.info("Adoption application successfully deleted");
			
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
		     List<AdoptionApplication> list =  adoptionApplicationRepository.findAllByPetId(petId);
		 
		     // iterate over list and remove each object from repository
		     for(AdoptionApplication app : list) {
			 
			     adoptionApplicationRepository.delete(app);
		     }
		 
	   }catch (Exception e) {
           throw new Exception("Error deleting adoption applications related to pet ID " + petId + ". " + e.getMessage(), e);
       }
	 }

	 
	 
	 /**
	  * Method responsible for finding any application duplicat
	  * 
	  * If a user tries to apply for a pet which has previously applied for, 
	  * this method will notify them.
	  * 
	  * Steps:
	  * 1. Check petString is not null
	  * 2. Convert petString into an ObjectId instance
	  * 3. Find all applications linked to the current user
	  * 4. Check if any of those applications also has the petID field matching with the pet ID
	  *    of the the user is trying to apply for
	  * 
	  *@param {String} petIdString - the string id type of the pet
	  *@param {User} user - the current user
	  *@throws Exception - any exception occured i the proccess
	  * */
	 public boolean isDuplicate(String petIdString, User user) throws Exception{
		 	
		    // check if object petIdString is null or empty and convert into n ObjectId    
			ObjectId petId;
			if(petIdString != null) {				
				 petId = new ObjectId(petIdString); // convert string IDs into an ObjectId				 
			}else {
				throw new Exception ("Invalid pet ID provided. Pet ID is null or empty");
			}
		 
			
			// flag to exit loop
	 		boolean applicationFound = false;
	 		// find and store all aplications found by userId
	 		List<AdoptionApplication> applications = adoptionApplicationRepository.findByApplicantId(user.getUserId());
	 		
	 		// iterate over the list and check if any has the same petId as the pet being requested
	 		for(AdoptionApplication app : applications) {	 			
	 			if(app.getPetId().equals(petId)) {
	 				return applicationFound = true;
	 			}		
	 		}	 		
	 		
	 	return applicationFound;	 	
	 }


	 /**
	  * 
	  * ***/
	@Override
	public List<AdoptionApplication> sortList(List<AdoptionApplication> list, String order) throws Exception{

	    // check if list is null or empty
		if (list == null || list.isEmpty()) {
	        logger.warn("List is null or empty. Nothing to sort.");
	        return list;
	    }
		
		// Check if param indicates ascending or descending order
	    if (order == null || order.equalsIgnoreCase("asc")) {
	        // Sort the applications list from latest to oldest
	        logger.debug("Before sorting ascending: " + list);
	        Collections.sort(list, new ApplicationDateAscendingComparator());
	        logger.info("List sorted in ascending order. Sorted list: " + list);
	        
	    } else if ("desc".equalsIgnoreCase(order)) {
	        // Sort the applications list from oldest(default) to the latest
	        logger.debug("Before sorting descending: " + list);
	        Collections.sort(list, new ApplicationDateDescendingComparator());
	        logger.info("List sorted in descending order. Sorted list: " + list);
	    } else {
	        logger.error("Invalid order parameter: {}", order);
	        throw new Exception("Invalid order parameter");
	    }
	    
	    // Log the final sorted list before returning
	    logger.debug("Final sorted list: " + list);
		return list;
	}
	
	
	
	
	/***
	 * Method to identify the pet category of the current pet which adoption application is meant to be.
	 * 
	 * This method dynamically creates the concrete pet class type based on the pet category, 
	 * and returns a Pet object with the concrete pet reference.
	 * 
	 * @params {String} category - the current pet category
	 * @param {String}  - the current pet id
	 * @throws Exception - with a descriptive message of a potential error
	 * **/
	public Pet getPetByCategory(String category, ObjectId petId) throws Exception{
		
		 Pet pet = null;
		 
		    switch (category) {
		    
		        case "kitties":
		            Optional<Cat> cat = catRepository.findById(petId);
		            if (cat.isPresent()) {
		                pet = cat.get();
		            } else {
		                throw new Exception("Cat not found with ID: " + petId);
		            }
		            break;

		            
		        case "puppies":
		            Optional<Dog> dog = dogRepository.findById(petId);
		            if (dog.isPresent()) {
		                pet = dog.get();
		            } else {
		                throw new Exception("Dog not found with ID: " + petId);
		            }	
		            break;
		        default:
		            throw new Exception("Invalid pet category: " + category);
		    }
		    
		    return pet;
	    }
	
	   
	/***
	 * Method to identify the pet category of the current pet which adoption application is meant to be.
	 * 
	 * This method dynamically creates the concrete pet class type based on the pet category, 
	 * and returns a Pet object with the concrete pet reference.
	 * 
	 * @params {String} category - the current pet category
	 * @param {String}  - the current pet id
	 * @throws Exception - with a descriptive message of a potential error
	 * **/
	   public void receiveNewApplication(String petId) {
	        // New application logic...
	        // Assume userId is available after receiving the new application
	     //   ObjectId userId = getUserIdFromPet(petId);
	       // notificationService.createNotification(userId, "New application received for pet ID: " + petId);
	    }
	
}
