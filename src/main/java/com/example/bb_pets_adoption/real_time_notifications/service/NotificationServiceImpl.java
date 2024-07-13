/**
 * 
 */
package com.example.bb_pets_adoption.real_time_notifications.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bb_pets_adoption.account_management.Model.User;
import com.example.bb_pets_adoption.adoption.model.AdoptionApplication;
import com.example.bb_pets_adoption.adoption.repository.AdoptionApplicationRepository;
import com.example.bb_pets_adoption.auth.service.AuthenticationServiceImpl;
import com.example.bb_pets_adoption.pet_listing.controller.PetController;
import com.example.bb_pets_adoption.pet_listing.model.PetList;
import com.example.bb_pets_adoption.pet_listing.repository.PetListRepository;
import com.example.bb_pets_adoption.real_time_notifications.Model.Notification;
import com.example.bb_pets_adoption.real_time_notifications.Repository.NotificationRepository;

/**
 * 
 */
@Service
public class NotificationServiceImpl implements NotificationService{

	// vars
	private AdoptionApplicationRepository adoptionApplicationRepository;
	private PetListRepository petListRepository;
	private NotificationRepository notificationRepository;
	private AuthenticationServiceImpl authenticationServiceImpl;
	
	// create an instance of Logger
	private static final Logger logger = LoggerFactory.getLogger(PetController.class);
				
	
	// constructor for dependency injection
	@Autowired	
	public NotificationServiceImpl(AdoptionApplicationRepository adoptionApplicationRepository,
			PetListRepository petListRepository, NotificationRepository notificationRepository, 
			AuthenticationServiceImpl authenticationServiceImpl) {
		this.adoptionApplicationRepository =  adoptionApplicationRepository;
		this.petListRepository =  petListRepository;
		this.notificationRepository =  notificationRepository;
		this.authenticationServiceImpl =  authenticationServiceImpl;
	}
	
	
	/**
	 * Method responsible for creating a new Notification instance 
	 * 
	 * Steps:
	 * 1. Check for null/empty values
	 * 2. Covert userStringId into an ObjectId
	 * 3. Instantiate Notification object
	 * 4. Set values
	 * 5. Save into repository
	 * 
	 * @param {ObjectId} userId - id of the user recipient
	 * @param {String} message - the notification message
	 * @throws Exception - an error during Notification instantiation
	 * **/
	@Override
	public void createAdoptionApplicationNotification(PetList petList, AdoptionApplication application, User user, String message) throws Exception {
		
		
		// try create and save a new notification instance
		try {
		    Notification notification = new Notification(); // instantiate a Notification object
            notification.setSenderId(user.getUserId());
            notification.setReceiverId(petList.getUserId());
            notification.setMessage(message);
            notification.setType("application");
            notification.setViewed(false);     //  set to false
            notification.setTimestamp(LocalDateTime.now());
            notificationRepository.save(notification);  // save in repository          
            logger.info("New Notifciations created for user ID:" + user.getUserId());
            
            // add the new notification into the pending notifications list related to the petLiting
            // (this will be used for petListing pending notifications)
            petList.getPendingNotifications().add(notification);
            petListRepository.save(petList);
            
             // add the new notification into the pending notifications list related to the adoption application
            // (this will be used for application pending notifications)
            application.getPendingNotifications().add(notification);
            adoptionApplicationRepository.save(application);
            
		}catch(Exception e) {		
			throw new Exception("Error occured and 'Notification' object could not be created." + e.getMessage());
		}
        
	}
	
	
	
	/***
	 * Method for sending notifications to the applicant when the status 
	 * of the their application has been updated by the pet owner.
	 * 
	 * Status options ("Pending", "Viewed","Accepted","Selected"
	 * 
	 * @param {String} applicationId - the id of the adoption application
	 * @param {String} status - the new application status
	 * */
	public void createUpdateStatusNotification(AdoptionApplication application, User user, String message) throws Exception{
		
		// try create and save a new notification instance
				try {
				    Notification notification = new Notification(); // instantiate a Notification object
				    notification.setSenderId(user.getUserId());
		            notification.setReceiverId(application.getApplicantId());
		            notification.setMessage(message);
		            notification.setType("status");
		            notification.setViewed(false);     //  set to false
		            notification.setTimestamp(LocalDateTime.now());
		            
		            notificationRepository.save(notification);  // save in repository          
		            logger.info("New Notification created for user ID:" + application.getApplicantId());
		            
		            // add the new notification to the list of pending notifications related to ststus updates
		            application.getPendingNotifications().add(notification);
		            adoptionApplicationRepository.save(application);
		            		            
				}catch(Exception e) {		
					throw new Exception("Error occured and 'Notification' object could not be created." + e.getMessage());
				}
	}

	
	/***
	 * Method responsible for finding all unread notifications
	 * belonging to a specific user related to a sppecific petListing
	 *
	 * Steps:
	 * 1. Find all pet listings related to the user identified by the given userId
	 * 2. Filters out the pet listings that have pending notifications
	 * 3. Collect all pending notifications from the filtered pet listings into a single list
	 * 
	 * @param {ObjectId} userId - The ID of the user recipient
	 * @return {List<Notification>} - A list of all unread notifications belonging to the user
	 */
	 @Override
	 public List<Notification> getPendingListingsNotifications(ObjectId userId){
	
			// find all adoption applcations related to the user
			List<PetList> list = petListRepository.findAllByUserId(userId);
			
			// ilter out pet lists that have pending notifications and collect the notifications
	        List<Notification> pendingNotifications = list.stream()
	                .flatMap(petList -> petList.getPendingNotifications().stream())
	                .collect(Collectors.toList());
	        return pendingNotifications;
    }
	
	
		/**
		 * Method to find unread notifications related to adoption applications which status have been updated 
		 * 
		 * @param {String} applicationId - the id string of the adoption application to search for
		 * **/
		public List<AdoptionApplication> getPendingStatusNotifications(ObjectId userId) throws Exception{
	
			// find all adoption applcations related to the user
			List<AdoptionApplication> list = adoptionApplicationRepository.findByApplicantId(userId);
			// map the list and store the applictions which pendingNotificationsList has any notification
			list.stream().map(app -> app.getPendingNotifications().size() >=1);
			// return the filtered list
	        return list;
	    }
	
		
	
	/***
	 * Method to set a specific notification with type "drop" as viewed
	 * 
	 * Steps:
	 * 1 Check null value
	 * 2. Find the notification in db by its ID
	 * 3. Mark notification as viewed
	 * 4. Save into repository
	 * 5. Find the petListing where the notifictaion lives in
	 * 6. Iterate over the list of pending notifictions and remove it using the notificationId
	 * 7. Save changes
	 * 8. Remove the the Notification itself from repository
	 * 
	 * @param {String} notificationIdString - the string representation of the notificationId
	 * @throws Exception - an error while occured during operation 
	 * **/
	@Override
	public void markAsViewedAndDelete(String notificationIdString) throws Exception {
		
		// check for null or empty value, and convert string into an objectId
		ObjectId notificationId;
		if(!notificationIdString.isEmpty() && notificationIdString != null) {
			notificationId = new ObjectId(notificationIdString);
		}else {
			throw new Exception("Notification ID cannot be emoty or null");
		}
        
		// remove notification from petListing pending notifications if exists
		boolean removed = removeNotificationFromPetListing(notificationId);
		// if not
		if(!removed) {
			// remove notification from adoption application pending notifications if exists
            removed = removeNotificationFromAdoptionApplication(notificationId);
		}
		
		if (!removed) {
            throw new Exception("Notification not found in any entity");
        }else {
        	
        	// find and set the notification as viewed
    		try { 
                Notification notification = notificationRepository.findById(notificationId).orElse(null);
                if (notification != null) {
                    notification.setViewed(true);
                    notificationRepository.save(notification);
                    
                    // remove from db
                    notificationRepository.deleteById(notificationId);
               }else {
       			throw new Exception("Notification object was found as null");
               }
    		}catch(Exception e) {		
    			throw new Exception("Error occured while trying to mark the notification as viewed." + e.getMessage());
    		}
        }
    }

	
	/***
	 * Method to remove a pending notification from a petListing
	 * 
	 * Steps:
	 * 1. Find and store all the PetList obejcts from db
	 * 2. Iterate over the list of pending notifications
	 * 3. Find and remove the notification from the list
	 * 4. Save changes
	 * 
	 * @param {ObjectId} notificationId -the  notification ID to be removed
	 * */
	 public boolean removeNotificationFromPetListing(ObjectId notificationId) {
		 
	        List<PetList> petLists = petListRepository.findAll();
	        for (PetList petList : petLists) {
	            boolean removed = petList.getPendingNotifications().removeIf(notification -> notification.getId().equals(notificationId));
	            if (removed) {
	                petListRepository.save(petList);
	                return true;
	            }
	        }
	        return false;
	    }
	 
	 
	/***
	* Method to remove a pending notification from an adoption application
	* 
	 * Steps:
	 * 1. Find store all the adoption applications from db
	 * 2. Iterate over the list of pending notifications
	 * 3. Find and remove the notification from the list
	 * 4. Save changes
	 * 
    * @param {ObjectId} notificationId -the  notification ID to be removed
	* */
	 public boolean removeNotificationFromAdoptionApplication(ObjectId notificationId) {
		 
	        List<AdoptionApplication> applications = adoptionApplicationRepository.findAll();
	        for (AdoptionApplication application : applications) {
	            boolean removed = application.getPendingNotifications().removeIf(notification -> notification.getId().equals(notificationId));
	            if (removed) {
	                adoptionApplicationRepository.save(application);
	                return true;
	            }
	        }
	        return false;
	    }
	 
	
    /*
     * 
     * */
	@Override
    public boolean authenticateUserByToken(String token) {
		
		return authenticationServiceImpl.authenticate(token);
	}


	 /*
     * Method to delete a notification from the db
     * 
     * Steps:
     * 1.Check for null or empty values
     * 2. Find the notification in db by its ID
     * 3. Delete it using its Id
     * */
	@Override
    public void deleteNotificationById(String notificationIdString) throws Exception{
				
		// check for null or empty value, and convert string into an objectId
		ObjectId notificationId;
		if(!notificationIdString.isEmpty() && notificationIdString != null) {
			notificationId = new ObjectId(notificationIdString);
		}else {
			throw new Exception("Notification ID cannot be emoty or null");
		}
				
		try {			
			 Optional<Notification> foundNotification = notificationRepository.findById(notificationId);
			 if(foundNotification.isPresent()) {
				 notificationRepository.deleteById(foundNotification.get().getId());
				 logger.info("Notification successfully removed from db");
			 }
		}catch(Exception e) {
			throw new Exception("Something went wrong and the notification could ot be deleted:" + e.getMessage());
		}		
	}


	/*
    * Method performs a cascade delete when the notification indicating 
    * the update of the application status has been viewed by the recipient 
    * the notification needs to be deleted from db and from the list of pending notifications.
	* 
	* Steps:
	* 1. Check for null or empty values
	* 2. Find the adoption application in db
	* 3. Get the list of pending notifications and remove the one which has been viewed
	* 4. Find the notification in db and remove it
	* 
	 *@param {String} notificationIdString - the string representaion of the notification ID
	 *@param {String} applicationIdString - the string representaion of the adoption application ID
	 * */
	@Override
   public void notificationDeleteCascade(String notificationIdString, String applicationIdString) throws Exception{
		

        // check for null or empty value, and convert string into an ObjectId
        ObjectId notificationId;
        if (notificationIdString != null && !notificationIdString.isEmpty()) {
            notificationId = new ObjectId(notificationIdString);
        } else {
            throw new Exception("Notification ID cannot be empty or null");
        }

        // check for null or empty value, and convert string into an ObjectId
        ObjectId applicationId;
        if (applicationIdString != null && !applicationIdString.isEmpty()) {
            applicationId = new ObjectId(applicationIdString);
        } else {
            throw new Exception("Application ID cannot be empty or null");
        }
        
		
     // Remove notification from the adoptionApplication pendingNotifications list
        try {
            Optional<AdoptionApplication> foundApplication = adoptionApplicationRepository.findById(applicationId);
            if (foundApplication.isPresent()) {
                AdoptionApplication adoptionApplication = foundApplication.get();
                adoptionApplication.getPendingNotifications().removeIf(notification -> notification.getId().equals(notificationId));
                logger.info("Notification successfully removed from the list of pending notifications related to the application");
                // save changes
                adoptionApplicationRepository.save(adoptionApplication);
            } else {
                throw new Exception("Adoption Application was not found");
            }
        } catch (Exception e) {
            throw new Exception("Something went wrong and the notification could not be deleted from the application: " + e.getMessage());
        }

        
        // Remove notification from the notifications repository
        deleteNotificationById(notificationIdString);   	
   
    }
	
}
