/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.bb_pets_adoption.auth.model.User;


/**
 * 
 */
@Document(collection = "posts")
public class Post {

	@Id
    private String id;
    private String comments;
    
    @DBRef
    private User user; // Reference to the User who created the post
    
    @DBRef
    private Pet pet; // Reference to the Pet related to the post
    
    private Date createdOn;
    private Date updatedOn;
}
