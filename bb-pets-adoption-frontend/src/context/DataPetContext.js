import React, { createContext, useState, useEffect, useMemo, useCallback } from 'react';

/**
 * React Context to provide application states to be used across multiple components within the React app.
 * This context manages the state of pet data, the current pet category, and tags used for filtering
 */

//create a context
export const DataPetContext = createContext();

// provider component
export const DataPetProvider = ({ children }) => {

    const [petsData, setPetsData] = useState([]);   // state for collection of pets
    const [currentPetCategory, setCurrentPetCategory] = useState("");   // state for current pet category

    const [tagsList, setTagsList] = useState([]);    // state for pet filtered search using tags

    const [petData, setPetData] = useState(""); // state for pet data of a single pet (retreived by id)
    const [petId, setPetId] = useState("");     // state for the current petId
    const [dataReadyForRedirect, setDataReadyForRedirect] = useState(false);  // state indicates when data is ready and user cn be redirected


    /***
     * Store the currentPetCategory in local storage whenever it changes
     */
    useEffect(() => {
        localStorage.setItem('currentPetCategory', currentPetCategory);
        setCurrentPetCategory(localStorage.getItem('currentPetCategory'))
    }, [currentPetCategory, setCurrentPetCategory]);


    /**
     * Method to reset data when a new category is selected or
     * the filtered pet search changes 
     * 
     * @params petCategory - current pet category
     */
    const resetPetsData = useCallback(() => {
        setPetsData([]);
    }, []);

    /**
     * Method to update the current list of pets.
     * When the 'load more' button is clicked, the current list withs pets is copied 
     * and the new bunch of pets(page) is append to the list
     * 
     * @params newData - the new bunch of pets data 
     */
    const updateData = useCallback((newData) => {
        setPetsData((prevData) => [...prevData, ...newData]);
    }, []);

    /**
     * 
     */
    const contextValue = useMemo(() => ({
        petsData, currentPetCategory, setCurrentPetCategory,
        setPetsData, resetPetsData, updateData, tagsList, setTagsList,
        petData, setPetData, petId, setPetId, dataReadyForRedirect, setDataReadyForRedirect
    }), [petsData, currentPetCategory, tagsList, petData, petId, dataReadyForRedirect, resetPetsData, updateData]);

    return (
        <DataPetContext.Provider value={contextValue}>
            {children}
        </DataPetContext.Provider>
    );
};