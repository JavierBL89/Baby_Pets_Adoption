/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.bb_pets_adoption.pet_listing.model.Cat;
import com.example.bb_pets_adoption.pet_listing.model.Dog;
import com.example.bb_pets_adoption.pet_listing.model.Pet;
import com.example.bb_pets_adoption.pet_listing.repository.CatRepository;
import com.example.bb_pets_adoption.pet_listing.repository.DogRepository;

/**
 * Class to host business logic around pet operations
 */
@Service
public class PetServiceImpl implements PetService{

	// vars
	CatRepository catRepository;
	DogRepository dogRepository;
	
	
	/**
	 * Constructor to inject dependencies
	 * 
	 * @param catRepository
	 * @param dogRepository
	 * */
	@Autowired
    public PetServiceImpl(CatRepository catRepository, DogRepository dogRepository) {
        this.catRepository = catRepository;
        this.dogRepository = dogRepository;
    }

	
   /**
   * 
   **/
	@Override
	public Page<Cat> findAllCats(Pageable pageable) {
		return catRepository.findAll(pageable);
	}

	/**
	  * 
	  **/
	@Override
	public Page<Dog> findAllDogs(Pageable pageable) {
		// TODO Auto-generated method stub
		return dogRepository.findAll(pageable);
	}

	/**
	  * 
	  **/
	@Override
	public Optional findCatById(String id) {
		
		return catRepository.findById(new ObjectId(id));
	}

	/**
	  * 
	  **/
	@Override
	public Optional findDogById(String id) {
		
		return dogRepository.findById(new ObjectId(id));
	}
	
	/*
	 * */
	@Override
	public Page<Cat> findAllCatsByTags(List<String> tags, Pageable pageable){
		
		return catRepository.findByTagsIn(tags, pageable);
	}
	
	
	/*
	 * */
	@Override
	public Page<Dog> findAllDogsByTags(List<String> tags, Pageable pageable){
		
		return dogRepository.findByTagsIn(tags, pageable);
	}

}
