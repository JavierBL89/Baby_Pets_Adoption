import { useState, useEffect } from "react";
import useFetchPets from "./fetchPets.jsx"


/**
 * Custom React hook that manages pagination for different categories
 * of pets dinamically
 * 
 * The useEffect listens to changes on the param passed 'categoryType' from any of the methods where the variable is passed
 * 
 * @returns The function 'usePagination' returns an object
 *  with the following properties:
 **  { pages, goToNextPage, goToPrevPage };
 */
const usePagination = (categoryType, num_of_columns) => {


    const [pages, setPages] = useState({ [categoryType]: { page: 0, columns_per_page: num_of_columns } });


    // useEfeect is triggered when 'categoryType' or 'num_of_columns' change. 
    // ...Basically when the user selects a pet category
    useEffect(() => {

        // update the pagination state when categoryType or num_of_columns change
        setPages({ [categoryType]: { page: 0, columns_per_page: num_of_columns } });

    }, [categoryType, num_of_columns]);


    /**
     * The function 'goToNextPage' updates the page number 
     * for a given category in a state variable called 'pages'
     */
    const goToNextPage = (categoryName, cols) => {

        setPages((prevState) => ({
            ...prevState,    // copy whatever is in the current state
            [categoryName]: {   // access the field in the object with string passed 'Kitties', 'Puppies', etc..
                page: prevState[categoryName].page + 1,   // access the current value of 'page' thte belong to the passed pet category and increase by 1
                columns_per_page: cols

            }
        }));
    }

    const resetPagination = () => {
        setPages({ [categoryType]: { page: 0, columns_per_page: num_of_columns } });
    };


    return { pages, goToNextPage, resetPagination }  // return consts to acces them from other parts in the app
};


export default usePagination;