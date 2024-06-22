import React, { useContext, useState, useEffect, useRef } from "react";
import { DataPetContext } from '../../../../context/DataPetContext';
import { AuthContext } from '../../../../context/AuthContext';
import { useNavigate } from 'react-router-dom'
import { Container, Row, Col, Spinner } from "react-bootstrap";
import PetCategory from "./PetCategory";
import Heading from "../../../common/Heading";
import useFetchPets from "../../../hooks/data/fetchPets";
import PetListingHolder from "./PetListingHolder";
import PetTagsHolder from "../pet_listing/pet_search/PetTagsHolder";


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

    // use 'DataPetContext' to update data pet context states
    const { petsData, currentPetCategory, setCurrentPetCategory, resetPetsData } = useContext(DataPetContext);

    // state for displaying certain info and elements based on authentication conditions
    const { isAuthenticated } = useContext(AuthContext);

    /**
     * Sets category state based on user selection
     * 
     * @params title - the title of the pet category
     */
    const handleClick = (title) => {
        resetPetsData([]);      // reset pets data state from 'useFetchPets' hook when a new category is selected
        setCurrentPetCategory(title.toLowerCase());  // update state when user selects a pet category to be passed to PetDataContext

    }

    // use custom hook retured utility variables when pets data is fetch from API
    const { pages, loading, error, totalPages, loadMore } = useFetchPets();

    return (

        <Container className="mt-5 mb-5 pb-5 bg-warning w-60" >
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
                {isAuthenticated ? (
                    <div>
                        <p>SingIn to access adoption services</p>
                    </div>
                )
                    :
                    null
                }
            </Row>
            <Container>
                {loading && <Spinner animation="border" role="status">
                    <span className="visually-hidden">Loading...</span>
                </Spinner>}
                {petsData && console.log(petsData)}
                {error && <p>Error fetching pets. Please try later</p>}
                {error && console.log("Error fetching pets: ", { error })}
                {petsData.length >= 0 &&

                    <Container >
                        <PetTagsHolder petCategory={currentPetCategory} />

                        <PetListingHolder
                            pets={petsData}
                        ></PetListingHolder>
                        <div>
                            <button onClick={() => loadMore()} disabled={pages[currentPetCategory]?.page >= totalPages - 1}>More Pets</button>
                        </div>
                    </Container>
                }
            </Container>
        </Container>


    )
}

export default PetCategoriesHolder;