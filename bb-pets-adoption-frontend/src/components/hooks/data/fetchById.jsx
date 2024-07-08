import React, { useContext, useState, useEffect, useCallback } from "react";
import instance from "../../../scripts/axiosConfig";
import { DataPetContext } from "../../../context/DataPetContext";
import { useParams } from "react-router-dom";



/**
 * Custom Hook makes API GET request to retrieve a pet by its ID.
 *
 * This hook listens to changes on the 'petId' parameter to make a GET request based on the pet ID.
 *
 * /pets/{id};  (GET request with a single ID)
 *
 * This hook returns the data retrieved along with other useful states to be used across other modules that depend on this Hook
 * to perform operations.
 *
 * @returns loading   - state to inform while the GET request is in process
 * @returns error     - the error message
 * @returns petData   - the data retrieved from the API
 */
const useFetchById = (petId) => {

    // use DataPetContext to access states for pet search and management 
    const { setDataReadyForRedirect } = useContext(DataPetContext);
    const [petData, setPetData] = useState("");       // state for error messages

    const [loading, setLoading] = useState(false);  // state used for feedback or UX purposes
    const [error, setError] = useState("");       // state for error messages

    const { currentPetCategory } = useParams();

    //const currentPetCategory = localStorage.getItem('currentPetCategory');
    const fetchPet = useCallback(async () => {


        // check if id is null or empty and exit function
        if (!petId) {
            console.error("'petId' is empty or null");

            return;
        }

        if (!currentPetCategory) {
            console.error("'currentPetCategory' is empty or null");
            return;
        }

        setLoading(true);  // set state to true
        setDataReadyForRedirect(false);  // set the redirection flag
        console.log(currentPetCategory);
        try {
            // url appropiate thr appropiate target endpoint
            const url = `/pets/${currentPetCategory}/view/${petId}`;

            // GET request
            const response = await instance.get(url);

            // check if status ok or report error
            if (response && response.status === 200) {

                const result = response.data;    // grab response
                if (result) {
                    setPetData(result);
                    setDataReadyForRedirect(true); // data is ready for url redirection
                } else {
                    setError("No data returned");
                }
            } else {
                throw new Error(`HTTP error status: ${response.status}`);
            }

        } catch (err) {    // catch any error during process
            setError(err.message);
            throw new Error("Error: " + err.message);
        } finally {
            setLoading(false);   // always et back to false
        }

    }, [petId, currentPetCategory, setPetData, setDataReadyForRedirect]);

    /***
     * useEffect gets triggered when petId state is changed
     */
    useEffect(() => {
        if (petId) {

            fetchPet();
        }
    }, [petId, fetchPet]);

    return { loading, error, petData };
};



export default useFetchById;