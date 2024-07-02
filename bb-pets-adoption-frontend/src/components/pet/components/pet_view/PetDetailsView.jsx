import React, { useState, useContext, useEffect } from "react";
import { useParams } from "react-router-dom";

import { AuthContext } from "../../../../context/AuthContext";
import { DataPetContext } from "../../../../context/DataPetContext";
import { Container, Row, Col } from "react-bootstrap";
import ImageComponent from "../../../common/ImageComponent";
import ButtonComponent from "../../../common/ButtonComponent";
import TextComponent from "../../../common/TextComponent";
import useFetchById from "../../../hooks/data/fetchById";
import PetAdoptionComponent from "../pet_adoption/PetAdoptionComponent";



const PetDetailsView = () => {

    const { petId } = useParams();

    const { isAuthenticated } = useContext(AuthContext);

    // const { petData } = useContext(DataPetContext);

    const { loading, error, petData } = useFetchById(petId);

    /****
     * 
     */
    const handleClick = () => {

    };


    return (
        <Container id="pet_view_wrapper" className="">
            <Container id="pet_view_container" className="">
                {loading && <p>Loading...</p>}
                {error && <p>Error: {error}</p>}
                {petData &&
                    <Row id="pet_view_info" className="">
                        <Col id="pet_img_holder" className="pet_view_img_holder">

                            <ImageComponent src={petData.images} alt={petData.breed} className="" />
                        </Col>
                        <Col>
                            <Row >
                                <Row>
                                    <small >Breed</small>
                                    <p className="" >{petData.breed}</p>
                                </Row>
                                <Row>
                                    <Col></Col>
                                    <Col></Col>
                                </Row>
                                <Row>
                                    <Col></Col>
                                    <Col>

                                    </Col>
                                </Row>
                                <Row>
                                    <small >Comments</small>
                                    <p>{petData.description}</p>
                                </Row>
                                <Row>
                                    <small >Price</small>
                                    <p>"Free adoption"</p>
                                </Row>
                            </Row>

                        </Col>
                    </Row>
                }

                <Row >
                    {/* <Row>
                                    <ButtonComponent href={"#"} id="adoption_request_button" text="Go for it!" className="" />
                                    </Row> */}
                    {
                        isAuthenticated ? <PetAdoptionComponent petId={petId} />
                            :

                            <TextComponent
                                text="Only subscribed users have access to adoption services !" />

                    }
                </Row>
            </Container>
        </Container>
    );



};

export default PetDetailsView;