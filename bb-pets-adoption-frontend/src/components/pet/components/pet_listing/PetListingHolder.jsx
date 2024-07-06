import React, { useContext, useEffect } from "react";
import { Container, Row, Col, Spinner } from "react-bootstrap";
import PetCard from "../pet_listing/PetCard";
import { useNavigate } from "react-router-dom";

import { DataPetContext } from "../../../../context/DataPetContext";
import useFetchById from "../../../hooks/data/fetchById";
import TextComponent from "../../../common/TextComponent";

/***
 * 
 */
const PetListingHolder = () => {

    const { petsData } = useContext(DataPetContext);


    return (
        <Container className="m-auto" id="pet_listing_wrapper">
            <Container className="m-auto" id="pet_listing_container">
                <Row className="p-0">
                    {petsData && petsData.map((pet, index) => (
                        <Col key={index} xs={6} md={3} lg={4} className="p-0">
                            <PetCard
                                petId={pet.id}
                                location={pet.location}
                                price={pet.price}
                                img={pet.motherImg ? pet.motherImg : "https://res.cloudinary.com/dthlibbj7/image/upload/c_crop,h_364,w_436/v1718832895/depositphotos_318221368-stock-illustration-missing-picture-page-for-website_jur5he.webp"}
                                breed={pet.motherBreed}
                            />
                        </Col>
                    ))}
                </Row>

            </Container>
        </Container>
    );
};

export default PetListingHolder;