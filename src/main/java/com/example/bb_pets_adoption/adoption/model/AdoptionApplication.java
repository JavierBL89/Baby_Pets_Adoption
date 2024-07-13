package com.example.bb_pets_adoption.adoption.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.bb_pets_adoption.account_management.Model.User;
import com.example.bb_pets_adoption.pet_listing.model.Pet;
import com.example.bb_pets_adoption.real_time_notifications.Model.Notification;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Represents the adoption application details.
 * */
@Data
@Document(collection = "adoption_applications")
public class AdoptionApplication {

    @Id
    private ObjectId id;
    private ObjectId petId;
    private String appTracker;
    private Pet pet;
    private User applicant;
    private ObjectId applicantId;
    private String status;
    private LocalDate applicationDate;
    private String comments;
    
    // list of notifications related to updates on application statuses
    private List<Notification> pendingNotifications;
   
    /// consturictor initializes pendingNotifications list
    public AdoptionApplication() {
    	    	this.pendingNotifications = new ArrayList<>();
    	    	this.appTracker = UUID.randomUUID().toString().substring(0,4);
    }
    
}
