/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.service;

import com.example.bb_pets_adoption.pet_listing.repository.CatRepository;
import com.example.bb_pets_adoption.pet_listing.repository.DogRepository;
import com.example.bb_pets_adoption.pet_listing.repository.PetListRepository;
import com.example.bb_pets_adoption.pet_listing.repository.PetRepository;
import com.example.bb_pets_adoption.pet_listing.model.Cat;
import com.example.bb_pets_adoption.pet_listing.model.Dog;
import com.example.bb_pets_adoption.pet_listing.model.Pet;
import com.example.bb_pets_adoption.pet_listing.model.PetList;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/**
 * SQUEDULED TASK
 * 
 * 
 * Service class is reposible for finding and removing all pets which birth date is gone past 40 days
 * to ensure application requirement
 * 
 * It uses Spring Boot Scheduling framework that allows to schedule tasks.
 * 
 * This scheduled task gets triggered every day at midnight.
 * 
 * **/
@Service
public class PetCleanupService {

	
	    private CatRepository catRepository;
	    private DogRepository dogRepository;
	    private PetListRepository petListRepository;
	    
	    // Contructor for dependency injection
	    @Autowired
	    public PetCleanupService(CatRepository catRepository, DogRepository dogRepository, PetListRepository petListRepository) {
	    	
	    	this.catRepository = catRepository;
	    	this.dogRepository= dogRepository;
	    	this.petListRepository = petListRepository;
	    }

	    
    /**
     * Method runs every day at midnight
     * ***/
    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void cleanupOldPets() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(40);

     // Clean up cats
        List<Pet> oldCats = catRepository.findAllByBirthDateBefore(thirtyDaysAgo);
        for (Pet cat : oldCats) {
            deletePetAndPetList(cat.getId());
        }


        // Clean up dogs
        List<Pet> oldDogs = dogRepository.findAllByBirthDateBefore(thirtyDaysAgo);
        for (Pet dog : oldDogs) {
            deletePetAndPetList(dog.getId());
        }
    }
    
    
    /***
     * Method to remove pets from their respective collections and the pet listing associated to them
     * 
     * @param {ObjectId} - the id of the pet to be removed from db mn
     * */
    private void deletePetAndPetList(ObjectId petId) {
    	
        // firstly, delete pet listing associated with the pet
        Optional<PetList> petListOptional = petListRepository.findByPetId(petId);
        petListOptional.ifPresent(petListRepository::delete);

        // secondly, delete the pet
        if (catRepository.existsById(petId)) {
            catRepository.deleteById(petId);
        } else if (dogRepository.existsById(petId)) {
            dogRepository.deleteById(petId);
        }
    }
}
