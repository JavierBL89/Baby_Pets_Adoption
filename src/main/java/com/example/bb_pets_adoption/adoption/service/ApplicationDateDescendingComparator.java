/**
 * 
 */
package com.example.bb_pets_adoption.adoption.service;

import java.util.Comparator;

import com.example.bb_pets_adoption.adoption.model.AdoptionApplication;

/**
 * Class responsible for comparing PetList objects in descending order (latest to older )based on thir createdOn date
 * 
 * it is put aside from the PetList object model to follow the single responsability priciple
 */
public class ApplicationDateDescendingComparator implements Comparator<AdoptionApplication> {
	
    @Override
    public int compare(AdoptionApplication o1, AdoptionApplication o2) {
        return o2.getApplicationDate().compareTo(o1.getApplicationDate());
    }
}