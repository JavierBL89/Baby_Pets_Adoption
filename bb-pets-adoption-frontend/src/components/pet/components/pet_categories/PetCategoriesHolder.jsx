import React, { useContext, useCallback, useEffect } from "react";
import { Container, Row, Col, Spinner } from "react-bootstrap";
import PetCategory from "./PetCategory";

/**
 * React component that acts as a holder for the different 'PetCategory' components.
 * 
 * It allows users to select a pet category and pass the request up to the parent component 'Home' throught the method 'handleClick'
 * which triggeres the useFecthPets hook reponsible for making GET request to retreive pets data based on the category selected.
 * 
 * Once the data is back, it passes it down to 'PetListingHolder' responsible for rendering PetCards with pagination
 *
 */
const PetCategoriesHolder = ({ handleClick }) => {


    return (
        <Container id="categories_wrapper">
            <Container id="hero_holder" >
                <img src={process.env.PUBLIC_URL + "/assets/images/CategoriesBgImSmall.png"} alt="background" />

            </Container>

            {/* <Heading tagName="h2" id="pet_categories_heading" className="text-center mt-4 mb-5" text="Browse..." /> */}
            <Row className="text-center m-auto" id="categories_holder">
                <Col xs={6} className="">
                    <PetCategory onClick={handleClick} url="" title="Kitties" />

                </Col>
                <Col xs={6}>
                    <PetCategory onClick={handleClick} url="" title="Puppies" />
                </Col>
            </Row>
        </Container>
    );
};

export default PetCategoriesHolder;