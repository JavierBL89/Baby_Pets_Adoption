import { useState, useEffect, useCallback, useMemo } from "react";


/**
 * Custom React hook that manages pagination for different categories
 * of pets dinamically
 * 
 * The useEffect listens to changes on the param passed 'categoryType' from any of the methods where the variable is passed
 * 
 * @returns The function 'usePagination' returns an object memoized with the hook 'useMemo'
 *  with the following properties:
 **  { pages, goToNextPage, goToPrevPage };
 * 
 * 'useMemo' hook helps to store the values of the dependancies till the next update, 
 * which helps to mantain a more consistant applicattion state
 */
const usePagination = (categoryType) => {

    const num_of_columns = 6;                      // Number of items per page
    const [pages, setPages] = useState({});


    // useEfeect is triggered when 'categoryType' or 'num_of_columns' change. 
    // ...Basically when the user selects a pet category
    useEffect(() => {

        // update the pagination state when categoryType or num_of_columns change
        setPages({ [categoryType]: { page: 0, columns_per_page: num_of_columns } });

    }, [categoryType]);


    /**
     * The function 'goToNextPage' updates the page number 
     * for a given category in a state variable called 'pages'
     */
    const goToNextPage = useCallback((categoryName, cols) => {

        setPages((prevState) => ({
            ...prevState,    // copy whatever is in the current state
            [categoryName]: {   // access the field in the object with string passed 'Kitties', 'Puppies', etc..
                page: prevState[categoryName].page + 1,   // access the current value of 'page' thte belong to the passed pet category and increase by 1
                columns_per_page: cols

            }
        }));
    }, []);


    const resetPagination = useCallback(() => {
        setPages({ [categoryType]: { page: 0, columns_per_page: num_of_columns } });
    }, [categoryType]);


    return useMemo(() => ({ pages, goToNextPage, resetPagination }), [pages, goToNextPage, resetPagination]);
    // return consts to acces them from other parts in the app
};


export default usePagination;