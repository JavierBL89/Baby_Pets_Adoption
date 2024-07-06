package com.example.bb_pets_adoption.adoption.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.bb_pets_adoption.auth.model.User;
import com.example.bb_pets_adoption.pet_listing.model.Pet;

import lombok.Data;

import java.time.LocalDate;


/**
 * Represents the adoption application details.
 * */
@Data
@Document(collection = "adoption_applications")
public class AdoptionApplication {

    @Id
    private ObjectId id;
    private ObjectId petId;
    private Pet pet;
    private User applicant;
    private ObjectId applicantId;
    private String status;
    private LocalDate applicationDate;
    private String comments;

    
}
