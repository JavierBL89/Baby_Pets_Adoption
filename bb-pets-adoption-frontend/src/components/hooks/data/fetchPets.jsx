import { useState, useEffect, useCallback } from "react";
import axios from "../../../scripts/axiosConfig";
import usePagination from './pagination';


/***
 * Custom Hook makes API GET request to retreive pets data using pagination params as url queries.
 * 
 * This useFecthPets Hook depends on usePagination Hook which is responsible for managin the pagination behavoir 
 * by updating the page number and the columns (or number of pet objects) to be retrieved per page.
 * Only when the 'pages' state is updated within 'usePagination', the 'useEffect' on this Hook is triggered 
 * to use the new 'pages' state to make the GET request with the new pagination params
 * 
 * `/pets/${categoryType}?pageNo=${current_page}&pageSize=${columns_per_page}`;
 * 
 * It handles potential errors before to prevent bad request is the 'pages' state do not contain the correct data
 * and after the GET request by cheking  the response status
 * 
 * The hook returnes the data retreive along with other useful states to be used accross the other modules that depend on this Hook
 * to perform operations
 * 
 * @returns petsData  - the pets data retreived
 * @returns pages  - the objetc with info of the current page state and amount of coulumns received from usePagination hook
 *  @returns loading   - state to inform while the GET request is in process
 *  @returns error     - the error message
 *  @returns totalPages - the current page number  '2', '3'..
 *  @returns goToNextPage  - method from usePagination that updates the 'pages' to retreive the next chunk of data 
 *  @returns goToPreviousPage - method from usePagination that updates the 'pages' to retreive the next chunk of data 
 * 
 */
const useFetchPets = (categoryType) => {

    const [petsData, setPetsData] = useState([]);   // pets data fetched from API
    const [loading, setLoading] = useState(false);  // state used for feedfack or UX purposes
    const [error, setError] = useState(null);       // state for error messages
    const num_of_columns = 6;                      // Number of items per page


    // call usePagination and access the returned values of the new upadated pagination state
    const { pages, goToNextPage, resetPagination } = usePagination(categoryType, num_of_columns);
    // this keeps track of the page number. this state is used to enable and desable buttons 'next', 'prev'
    const [totalPages, setTotalPages] = useState(0);




    const fetchData = useCallback(async (append = false) => {

        console.log('Pages state:', pages);
        if (categoryType) {
            setLoading(true);  // set stateto true

            /********
             * *******
             * 
             * This block of code has the purpose of a  defensive programming
             * to ensure pages and the category exist before making API request.
             * 
             * But because the way I have coded pagination adn hooks, the 'fetchData' method is always triggered twice on each render, 
             * and always throws the error within since the condition is true on the very first render of the double render..
             * and if remove the check, the list of pets is copied rather than replaced on every pet category change, so I've left it just like it is now
             * 
             *********
             ********/
            if (!pages || !pages[categoryType]) {
                // setError(`Data for pagination is not available for category: ${categoryType}`);
                // setLoading(false)
                return;
            }

            try {
                /* The first code line is assigning the value of
                  'pages[category]?.page' to the variable 'current_page'. If 'pages[category]?.page' is defined
                   and not null, then 'current_page' will be assigned that value. Otherwise, it will be assigned
                  the value of 0. */
                let current_page = pages[categoryType]?.page ?? 0;
                let columns_per_page = num_of_columns;

                // GET request to target URL and pass the params expected in API for the paginaion feature
                let url = `/pets/${categoryType}?pageNo=${current_page}&pageSize=${columns_per_page}`;

                const response = await axios.get(url
                );

                // check if status ok or report error
                if (response.status === 200) {
                    const result = response.data;  // grab response

                    // check if result has data or set error message
                    if (result) {
                        const newPets = result.content;
                        append = true;
                        // assuming the result is the array of pets
                        setPetsData(prevPets => append && [...prevPets, ...newPets]);
                        setTotalPages(result.totalPages);  // this data variable comes on JSON object response provided from Page class in API
                    } else {
                        setError("No data returned");
                    }

                } else {
                    throw new Error(`HTTP error! status: ${response.status}`)
                }
            } catch (err) {    // catch any error during process
                setError(err.message);
            };

            setLoading(false);   // set back to falase
        }
    });

    ;

    // usesEffect is only triggered when the 'pages' state is changed from usePagination Hook to make API request
    useEffect(() => {
        fetchData(true);
    }, [pages, categoryType]);  // if url or pages changes, the useEffect is re-triggered

    const loadMore = () => {
        goToNextPage(categoryType, num_of_columns);
    };


    return { petsData, pages, setPetsData, loadMore, loading, error, totalPages, goToNextPage } // return constants to be accessed from other parts in application
}

export default useFetchPets;