import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import PetCard from "./PetCard";

/***
 * 
 */
const PetListingHolder = ({ pets }) => {



    return (

        <Container className="m-auto" id="pet_listing_wrapper">
            <Container className="m-auto" id="pet_listing_container">
                <Row className="p-0">
                    {

                        pets && pets.map((pet, index) => {
                            return (
                                <Col key={index} xs={6} md={3} lg={4} className="p-0">
                                    <PetCard
                                        img={pet.images && pet.images[0] ? pet.images[0] : "https://res.cloudinary.com/dthlibbj7/image/upload/c_crop,h_364,w_436/v1718832895/depositphotos_318221368-stock-illustration-missing-picture-page-for-website_jur5he.webp"}
                                        breed={pet.breed} />
                                </Col>
                            )
                        })


                    }
                </Row>

            </Container>
        </Container>
    )
};

export default PetListingHolder;