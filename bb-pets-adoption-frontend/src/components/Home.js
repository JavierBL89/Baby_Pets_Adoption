import React, { useContext, useEffect, useCallback } from "react";
import LogoutButton from "./auth/components/LogoutButton";
import { AuthContext } from "../context/AuthContext";
import { useLocation, useNavigate } from 'react-router-dom'
import { Container, Row, Col, Stack, Spinner } from "react-bootstrap";
import PetCategoriesHolder from "./pet/components/pet_categories/PetCategoriesHolder";
import AdoptionInfoComponent from "./AdoptionInfoComponent";
import WelcomeComponent from "./pet/components/WelcomeComponent";
import useFetchPets from "./hooks/data/fetchPets";
import { DataPetContext } from "../context/DataPetContext";
import PetTagsHolder from "./pet/components/pet_search/PetTagsHolder";
import PetListingHolder from "./pet/components/pet_listing/PetListingHolder";


/***
 * Component is the core part of the application, the landing page.
 * 
 * It handles user authentication state and is built up with other components that bring key functionalities.
 * 
 * It displays the pet categories and handles fucntionalit to allow users to select a category and fetch pets available using 
 * the  'useFetch' hook which in addition is listention to 'loadMore' action to perform another fecth using pagination
 * 
 */
const Home = () => {

    // state for displaying certain info and elements based on authentication conditions
    const { isAuthenticated, login } = useContext(AuthContext);
    const { currentPetCategory, petsData, setCurrentPetCategory, resetPetsData } = useContext(DataPetContext);
    const { loading, error, totalPages, loadMore, pages } = useFetchPets();

    /***
       * useEffect resets data to empty on every render
       */
    useEffect(() => {
        resetPetsData();
    }, [resetPetsData]);

    /**
       * Sets category state based on user selection
       * 
       * @params title - the title of the pet category
       */
    const handleClick = useCallback((title) => {

        resetPetsData();      // reset pets data state from 'useFetchPets' hook when a new category is selected
        setCurrentPetCategory(title.toLowerCase());  // update category state when user selects a pet category to be passed to PetDataContext

    }, [resetPetsData, setCurrentPetCategory]);



    return (

        <Stack>
            <WelcomeComponent />
            <PetCategoriesHolder handleClick={handleClick} />
            <Container>
                {loading &&
                    <Spinner animation="border" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </Spinner>}
                {error && <p>Error fetching pets. Please try later</p>}
                {error && console.log("Error fetching pets: ", { error })}
                {petsData.length > 0 && (
                    <Container>
                        <PetTagsHolder petCategory={currentPetCategory} />
                        <PetListingHolder />
                        <Row >
                            <button id="load_more_button" onClick={loadMore} disabled={pages[currentPetCategory]?.page >= totalPages - 1}>More Pets</button>
                        </Row>
                    </Container>
                )}
            </Container>
            <AdoptionInfoComponent />
        </Stack>


    )
}

export default Home;