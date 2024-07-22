/**
 * 
 */
package com.example.bb_pets_adoption.notification_service.Model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * Class to represent a notification object
 */
@Data
@Document(collection = "notifications")
public class Notification {

    @Id
    private ObjectId id;
    private ObjectId senderId;
    private ObjectId receiverId;
    private String message;
    private String type;
    private boolean viewed;
    private LocalDateTime timestamp;

    // Getters and setters
    
    public void setSenderId(ObjectId senderId) {
    	this.senderId = senderId;
    };
    
    public void setReceiverId(ObjectId receiverId) {
    	this.receiverId = receiverId;
    };
}
