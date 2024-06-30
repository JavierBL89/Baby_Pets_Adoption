/**
 * 
 */
package com.example.bb_pets_adoption.adoption.service;

import com.example.bb_pets_adoption.adoption.model.AdoptionApplication;
import com.example.bb_pets_adoption.adoption.repository.AdoptionApplicationRepository;
import com.example.bb_pets_adoption.auth.model.User;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;



/***
 * 
 ****/
@Service
public interface AdoptionApplicationService {


    /***
     * 
     * ***/
    public AdoptionApplication createApplication(String petIdString, User user, String comments)  throws Exception;

    
    /***
     * 
     ***/
    public Optional<AdoptionApplication> getApplicationById(ObjectId id);

    
    /***
     * 
     ***/
    public List<AdoptionApplication> getApplicationsByUserId(ObjectId userId);

    
    /***
     * 
     ***/
    public List<AdoptionApplication> getApplicationsByPetId(ObjectId petId);

    
    /***
     * 
     ***/
    public AdoptionApplication updateApplicationStatus(String applicationIdString, String status) throws Exception;

    
    /***
     * 
     ***/
    public void deleteApplication(User user, String petIdString, String applicationIdString) throws Exception;
    
    
    /***
     * 
     ***/
    public boolean authenticateUserByToken(String token);
    

    /***
     * 
     * **/
	public Optional<User> findUserByToken(String token);
   
}
