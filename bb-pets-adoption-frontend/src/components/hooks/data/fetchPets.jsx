import { useState, useEffect, useContext, useCallback } from "react";
import instance from "../../../scripts/axiosConfig";

import usePagination from './pagination';
import { DataPetContext } from '../../../context/DataPetContext';
/***
 * Custom Hook makes API GET request to retreive pets data using pagination params in url queries.
 * 
 * The 'useFecthPets' hook depends on 'usePagination' hook which is responsible for managin the pagination behavoir 
 * by updating the page number and the columns (or number of pet objects) to be retrieved per page.
 * When the 'pages' state is updated within 'usePagination'a the 'useEffect' on this hook is triggered 
 * to uses the new 'pages' state to make the GET request with the new pagination params in the string query
 * 
 * This 'useFetchPets' hook listens to changes on 'tagsList' state from 'DataPetsContext' to make a GET request based on pets attributes (tags)
 * selected by the user.
 * An if/else statement is used to build the appropiate URL fo GET request based on whether 'tagsList' is empty (not tags) or not.
 * 
 * /pets/${categoryType}?pageNo=${current_page}&pageSize=${columns_per_page};   (no tags GET request)
 * /pets/${categoryType}/filter_by?tags=<tag>&<tag>&pageNo=${current_page}&pageSize=${columns_per_page};  (GET rquest with tags)
 * 
 * It handles potential errors before to prevent bad request is the 'pages' state do not contain the correct data
 * and after the GET request by cheking  the response status
 * 
 * This 'useFecthPets' hook also listens when 'currentPetCategory' state is changed from the 'DataPetsContext',
 *  and to uses 'setPetsData' to set the state with new pets data list fetched.
 * 
 * This hook returnes the data retreive along with other useful states to be used accross the other modules that depend on this Hook
 * to perform operations
 * 
 * @returns pages  - the objetc with info of the current page state and amount of coulumns received from usePagination hook
 *  @returns loading   - state to inform while the GET request is in process
 *  @returns error     - the error message
 *  @returns totalPages - the current page number  '2', '3'..
 *  @returns goToNextPage  - method from usePagination that updates the 'pages' to retreive the next chunk of data 
 * @returns loadMore -  method variable to load next page with pets
 */
const useFetchPets = () => {

    const [loading, setLoading] = useState(false);  // state used for feedback or UX purposes
    const [error, setError] = useState(null);       // state for error messages
    const num_of_columns = 6;                      // Number of items per page

    // use DataPetContext to access 'dataPets' & current pet category states
    const { currentPetCategory, setPetsData, tagsList } = useContext(DataPetContext);

    // call usePagination and access the returned values of the new updated pagination state
    const { pages, goToNextPage } = usePagination(currentPetCategory);

    // this keeps track of the page number. this state is used to enable and disable buttons 'next', 'prev'
    const [totalPages, setTotalPages] = useState(0);



    // method responsible for handling GET request
    const fetchData = useCallback(async (append = false) => {

        if (currentPetCategory) {
            setLoading(true);  // set state to true

            /********
             * This block of code has the purpose of defensive programming
             * to ensure pages and the category exist before making API request.
             ********/
            if (!pages || !pages[currentPetCategory]) {
                return;
            }

            try {
                /* The first code line is assigning the value of
                  'pages[category]?.page' to the variable 'current_page'. If 'pages[category]?.page' is defined
                   and not null, then 'current_page' will be assigned that value. Otherwise, it will be assigned
                  the value of 0. */
                let current_page = pages[currentPetCategory]?.page ?? 0;
                let url = "";

                // build the appropriate URL based on condition
                if (tagsList && tagsList.length > 0) {
                    // build query string with each tag in the tags list by joining with '&'
                    const tagsQuery = tagsList.map(tag => `tags=${tag}`).join('&');
                    // GET request to target URL and pass the params expected in API for the pagination feature
                    url = `/pets/${currentPetCategory}/filter_by?${tagsQuery}&pageNo=${current_page}&pageSize=${num_of_columns}`;
                } else {
                    url = `/pets/${currentPetCategory}?pageNo=${current_page}&pageSize=${num_of_columns}`;
                }

                // GET request
                const response = await instance.get(url);

                // check if status ok or report error
                if (response.status === 200) {
                    const result = response.data;  // grab response

                    // check if result has data or set error message
                    if (result) {
                        const newPets = result.content;
                        setPetsData(prevPets => append ? [...prevPets, ...newPets] : newPets);
                        setTotalPages(result.totalPages);  // this data variable comes on JSON object response provided from Page class in API
                    } else {
                        setError("No data returned");
                    }
                } else {
                    throw new Error(`HTTP error status: ${response.status}`);
                }
            } catch (err) {    // catch any error during process
                setError(err.message);
            } finally {
                setLoading(false);   // set back to false
            }
        }
    }, [currentPetCategory, pages, setPetsData, tagsList]);

    /**
    *  useEffect is only triggered when the 'pages' state is changed from usePagination Hook to make API request
    */
    useEffect(() => {
        if (pages[currentPetCategory]) {
            fetchData(true);  // 'true' set boolean to allow and append new data to the existing list
        }
    }, [fetchData, pages]);

    /**
    * useEffect is only triggered when the 'tagsList' state is changed from 'DataPetsContext' Hook to make API request
    */
    useEffect(() => {
        fetchData(true);  // 'true' set boolean to allow and append new data to the existing list
    }, [tagsList]);

    /**
     * Helper method to initiate 'load more' pets functionality
     * */
    const loadMore = () => {
        goToNextPage(currentPetCategory, num_of_columns);
    };


    return { pages, loadMore, loading, error, totalPages }; // return constants to be accessed from other parts in application
};

export default useFetchPets;