package com.example.bb_pets_adoption.pet_listing.service;

import java.util.Comparator;

import com.example.bb_pets_adoption.pet_listing.model.PetList;

/**
 * Class responsible for comparing PetList objects in ascending order (older to latest )based on thir createdOn date
 * 
 * it is put aside from the PetList object model to follow the single responsability priciple
 */
public class PetListDateAscendingComparator implements Comparator<PetList> {
	
    @Override
    public int compare(PetList o1, PetList o2) {
        return o1.getCreatedOn().compareTo(o2.getCreatedOn());
    }
}