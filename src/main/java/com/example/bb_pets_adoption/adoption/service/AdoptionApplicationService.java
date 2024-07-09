/**
 * 
 */
package com.example.bb_pets_adoption.adoption.service;

import com.example.bb_pets_adoption.adoption.model.AdoptionApplication;
import com.example.bb_pets_adoption.account_management.Model.User;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
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
    public AdoptionApplication createApplication(String petIdString, String petCategory, User user, String comments)  throws Exception;

    
    /***
     * 
     ***/
    public Optional<AdoptionApplication> getApplicationById(ObjectId id);

    
    /***
     * 
     ***/
    public List<AdoptionApplication> getApplicationsByApplicantId(ObjectId userId);

    
    /***
     * 
     ***/
    Page<AdoptionApplication> getAllApplicationsByPetId(String petId, Pageable pageable) throws Exception;
    
    
    /**
     * 
     * */
    Page<AdoptionApplication> getAllApplicationsByStatus(String petIdString, Pageable pageable, String status) throws Exception;
    
    
    /***
     * 
     ***/
    public AdoptionApplication updateApplicationStatus(String applicationIdString, String status) throws Exception;

    
    /***
     * 
     ***/
    public void deleteApplication(User user, String applicationIdString) throws Exception;
    
    
    /***
     * 
     ***/
    public boolean authenticateUserByToken(String token);
    

    /***
     * 
     * **/
	public Optional<User> findUserByToken(String token);
	
	
	/***
	 * 
	 * **/
	boolean isDuplicate(String petIdString, User user) throws Exception;
	
	/**
	 * 
	 * **/
	Page<AdoptionApplication> getAllApplicationsByApplicantId(User user, Pageable pageable) throws Exception;
	
	
	/*
	 * 
	 ***/
	List<AdoptionApplication>  sortList(List<AdoptionApplication> list, String order) throws Exception;
   
}
