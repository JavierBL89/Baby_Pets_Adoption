/**
 * 
 */
package com.example.bb_pets_adoption.real_time_notifications.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.bb_pets_adoption.real_time_notifications.Model.Notification;

/**
 * Repsository interface for database operations related to 'notifications' database collection
 */
public interface NotificationRepository extends MongoRepository<Notification, ObjectId> {
	
	/*
	 *
	 **/
    List<Notification> findBySenderId(ObjectId senderId);
    
    /*
	 *
	 **/
   List<Notification> findByReceiverId(ObjectId userId);
}
