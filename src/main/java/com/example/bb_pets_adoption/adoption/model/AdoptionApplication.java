package com.example.bb_pets_adoption.adoption.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private ObjectId userId;
    private String status;
    private LocalDate applicationDate;
    private String comments;

    
}
