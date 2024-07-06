import React, { useContext, useCallback, useEffect } from "react";
import { AuthContext } from '../../../../context/AuthContext';
import { DataPetContext } from '../../../../context/DataPetContext';
import { Container, Row, Col, Spinner } from "react-bootstrap";
import PetCategory from "./PetCategory";
import Heading from "../../../common/Heading";
import PetListingHolder from "../pet_listing/PetListingHolder";
import PetTagsHolder from "../pet_search/PetTagsHolder";

import useFetchPets from "../../../hooks/data/fetchPets";

/**
 * React component that acts as a holder for the different 'PetCategory' components.
 * 
 * It allows users to select a pet category and passed the request up to the useFecthPets hook 
 * which is reponsible for making GET request to retreive pets data
 * select a category based on the category selected.
 * 
 * Once the data is back, it passes it down to 'PetListingHolder' responsible for rendering PetCards with pagination
 *
 */
const PetCategoriesHolder = () => {


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
        <Container className="mt-5 mb-5 pb-5 bg-warning w-60">
            <Heading tagName="h1" id="pet_categories_heading" className="text-center mt-4 mb-5" text="Pet Categories" />
            <Row className="text-center w-50 m-auto">
                <Col>
                    <PetCategory onClick={handleClick} url="" title="Kitties" />
                </Col>
                <Col>
                    <PetCategory onClick={handleClick} url="" title="Puppies" />
                </Col>
                <Col>
                    <PetCategory url="" title="Elephants" />
                </Col>
            </Row>
            <Row>
            </Row>
            <Container>
                {loading && <Spinner animation="border" role="status">
                    <span className="visually-hidden">Loading...</span>
                </Spinner>}
                {error && <p>Error fetching pets. Please try later</p>}
                {error && console.log("Error fetching pets: ", { error })}
                {petsData.length > 0 && (
                    <Container>
                        <PetTagsHolder petCategory={currentPetCategory} />
                        <PetListingHolder />
                        <div>
                            <button onClick={loadMore} disabled={pages[currentPetCategory]?.page >= totalPages - 1}>More Pets</button>
                        </div>
                    </Container>
                )}
            </Container>
        </Container>
    );
};

export default PetCategoriesHolder;