/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.Multipart;
import lombok.Data;

/**
 * Class represents a template for pet object data fields 
 * that will e used as part of the 'Startegy' Design Pattern 
 * to allow updating pets based on different pet categories
 */
@Data
public class PetUpdateData {

	@Id
	private String petId;
	private String category;
    private String breed;
    private String location;
    private String birthMonth;
    private String birthYear;
    private String motherBreed;
    private MultipartFile motherImg;
    private String fatherBreed;
    private MultipartFile fatherImg;
    private String price;
    private String comment;
    
    

}
