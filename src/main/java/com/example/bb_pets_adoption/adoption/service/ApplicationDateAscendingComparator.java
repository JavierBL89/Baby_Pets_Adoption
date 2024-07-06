package com.example.bb_pets_adoption.adoption.service;

import java.util.Comparator;

import com.example.bb_pets_adoption.adoption.model.AdoptionApplication;

/**
 * Class responsible for comparing PetList objects in ascending order (older to latest )based on thir createdOn date
 * 
 * it is put aside from the PetList object model to follow the single responsability priciple
 */
public class ApplicationDateAscendingComparator implements Comparator<AdoptionApplication> {
	
    @Override
    public int compare(AdoptionApplication a1, AdoptionApplication a2) {
        return a1.getApplicationDate().compareTo(a2.getApplicationDate());
    }
}