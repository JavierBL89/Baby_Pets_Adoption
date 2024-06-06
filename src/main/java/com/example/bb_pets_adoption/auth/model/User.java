/**
 * 
 */
package com.example.bb_pets_adoption.auth.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* Class represents an a User. User can ben an "Adopter", "Pet Provider", or both.
* The fields are mapped and converted into a User document using MongoDb @Document 
* and then stored into Mongo database.
* 
* Class usess Lombok @nnotations for reducing boiler plate and enhance readiness
*/

@Data                  // This annotation generates getters, setters, toString, equals, and hashCode methods 
@NoArgsConstructor     // This annotation generates a no-argument constructor
@AllArgsConstructor    // This annotation generates an all-argument constructor
@Document(collection = "users") // Marks this class as a MongoDB User document
public class User {

	
	@Id
	private ObjectId userId;
	private String name;
	private String lastName;
	private String email;
	private String password;
	private String token;
	private List<String> roles;  // list of roles ["Adopter", "Pet Provider"]



}