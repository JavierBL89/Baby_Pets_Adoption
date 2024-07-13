/**
 * 
 */
package com.example.bb_pets_adoption.real_time_notifications.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.example.bb_pets_adoption.account_management.Model.User;
import com.example.bb_pets_adoption.adoption.model.AdoptionApplication;
import com.example.bb_pets_adoption.pet_listing.model.PetList;
import com.example.bb_pets_adoption.real_time_notifications.Model.Notification;

/**
 * Service class to define the operations for real time notifications
 */
@Service
public interface NotificationService {

	/**
	 * Method responsible for creating a new Notification instance
	 *  when a new adoption is been requested
	 * 
	 * @param {ObjectId} userId - 
	 * @param {String} message - 
	 * **/
	public void createAdoptionApplicationNotification(PetList petList,  AdoptionApplication application, User user, String message) throws Exception;
	
	

	/***
	 * Method creates anew notification for the user applicant when the status 
	 * of the their application has been updated
	 * 
	 * @param {String} applicationId - the id of the adoption application
	 * @param {String} status - the new application status
	 */
	public void createUpdateStatusNotification(AdoptionApplication application,  User user, String message) throws Exception;
	
	
	/**
	 * Method to mark a 'Notification' with type "drop" as viewed when the user reviews it
	 * 
	 * @param {String} notificationIdString - the notification ID to be udpdated
	 * **/
	public void markAsViewedAndDelete(String notificationIdString) throws Exception;
	
	
	/***
	 * Method to remove a pending notification from a petListing
	 * @param {ObjectId} notificationId -the  notification ID to be removed
	 * */
	 public boolean removeNotificationFromPetListing(ObjectId notificationId);
	 
	 
	 /***
	 * Method to remove a pending notification from an adoption application
	 * 
	 * @param {ObjectId} notificationId -the  notification ID to be removed
	 * */
	 public boolean removeNotificationFromAdoptionApplication(ObjectId notificationId);
	 
	 
	/**
	 * Method to find unread notifications related to a user
	 * 
	 * @param {ObjectId} userId - the id of the recipient user
	 * **/
	public List<Notification> getPendingListingsNotifications(ObjectId userId) throws Exception;
	
	

	/**
	 * Method to find unread notifications related to adoption applications which status have been updated 
	 * 
	 * @param{ObjectId} userIdString - the id of the recipient user
	 * **/
	public List<AdoptionApplication> getPendingStatusNotifications(ObjectId userId) throws Exception;
	
	
	/**
	 * 
	 * */
	public boolean authenticateUserByToken(String token);


	/**
	 * Method to delete a notificaion from db by ID
	 * 
	 *@param {String} notificationIdString - the tring representaion of the notification ID
	 * */
	void deleteNotificationById(String notificationIdString) throws Exception;



	/**
	 * Method to delete a notificaion from the pendingNotifications list of the AdoptionApplication
	 * 
	 * It performs a cascade delete when the notification 
	 * indicating the update of the application status has been viewed by the recipient
	 * 
	 *@param {String} notificationIdString - the string representaion of the notification ID
	 **@param {String} applicationIdString - the string representaion of the adoption application ID
	 * */
	void notificationDeleteCascade(String notificationIdString, String applicationIdString) throws Exception;
	
	
}

