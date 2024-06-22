import React, { createContext, useState } from 'react';

/**
 * React Context to provide application states to be used across multiple components within the React app.
 * This context manages the state of pet data, the current pet category, and tags used for filtering
 */

//create a context
export const DataPetContext = createContext();

// provider component
export const DataPetProvider = ({ children }) => {

    const [petsData, setPetsData] = useState([]);
    const [currentPetCategory, setCurrentPetCategory] = useState("");
    const [tagsList, setTagsList] = useState([]);


    /**
     * Method to reset data when a new category is selected or
     * the filtered pet search changes 
     * 
     * @params petCategory - current pet category
     */
    const resetPetsData = () => {
        setPetsData([]);
    }

    /**
     * Method to update the current list of pets.
     * When the 'load more' button is clicked, the current list withs pets is copied 
     * and the new bunch of pets(page) is append to the list
     * 
     * @params newData - the new bunch of pets data 
     */
    const updateData = (newData) => {
        setPetsData((prevData) => [...prevData, ...newData]);
    };

    return (
        <DataPetContext.Provider value={{
            petsData, currentPetCategory,
            setCurrentPetCategory, setPetsData,
            resetPetsData, updateData, tagsList, setTagsList
        }}>
            {children}
        </DataPetContext.Provider>
    );
};