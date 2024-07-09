/**
 * 
 */
package com.example.bb_pets_adoption.account_management.Model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import com.example.bb_pets_adoption.account_management.Model.User;
import com.example.bb_pets_adoption.pet_listing.model.PetList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class represents a template for user object data fields 
 * to allow updating pets based on different pet categories
 */
@Data                  // This annotation generates getters, setters, toString, equals, and hashCode methods 
@NoArgsConstructor     // This annotation generates a no-argument constructor
@AllArgsConstructor    // This annotation generates an all-argument constructor
public class UpdateProfileData {


	private String name;
	private String lastName;
	private String email;
	private String password;
	private List<String> roles;  // list of roles ["Adopter", "Pet Provider"]
	

}
