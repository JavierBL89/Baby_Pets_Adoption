/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.model;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* Class represents a Cat object that inherits from Pet calss. 
* It defines the attributes of a Cat objectt
* 
* The fields are mapped and converted into a Pet document using MongoDb @Document 
* to then be stored into Mongo database
* 
*/
@Document(collection = "dogs") // Marks this class as a MongoDB Pet document
public class Dog extends Pet {

	
}
