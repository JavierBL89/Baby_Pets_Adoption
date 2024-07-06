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

    const { petId, currentPetCategory } = useParams();

    const { isAuthenticated } = useContext(AuthContext);

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
                        {/********** Pets's Mother Image ******** */}
                        <Col id="pet_img_holder" className="pet_view_img_holder">
                            <ImageComponent src={petData.motherImg} alt={petData.breed} className="" />
                        </Col>

                        <Col id="pet_details_holder">
                            <Row >
                                {/********** Pets's Breed ******** */}
                                <Row className="pet_details_row">
                                    <Col>
                                        <small >Mother Breed:</small>
                                        <TextComponent className="pet_detail_text" text={petData.motherBreed} />
                                    </Col>
                                    <Col>
                                        <small >Father Breed:</small>
                                        <TextComponent className="pet_detail_text" text={petData.fatherBreed ? petData.fatherBreed : "No specified"} />
                                    </Col>

                                </Row>

                                {/********** Pets's Birth and Location ******** */}
                                <Row className="pet_details_row">
                                    <Col>
                                        <small >Estimated Birth Date:</small>
                                        <TextComponent className="pet_detail_text" text={`${petData.birthDate[1]}/${petData.birthDate[0]}`} />
                                    </Col>
                                    <Col>
                                        <small >Location:</small>
                                        <TextComponent className="pet_detail_text" text={petData.location} />
                                    </Col>

                                </Row>

                                {/********** Pets's Price and Owner's Name ******** */}
                                <Row className="pet_details_row">
                                    <Col>
                                        <small >Price:</small>

                                        <TextComponent className="pet_detail_text" text={petData.price ? petData.price : "Free Adoption"} />
                                    </Col>
                                    <Col>
                                        <small >Owner's Name</small>
                                        <TextComponent className="pet_detail_text" text={petData.ownerName} />
                                    </Col>
                                </Row>
                            </Row>
                        </Col>
                        {/********** Comments ******** */}
                        <Row id="pet_details_comment_wrapper">
                            <Row className="m-0" id="pet_details_comment_container">
                                <small className="mt-2">Comments</small>
                                <Row id="pet_details_comments_holder">
                                    <TextComponent id="pet_comments_text"
                                        text={
                                            petData.description ? petData.description : "No comments"} />
                                </Row>
                            </Row>
                        </Row>
                    </Row>
                }

                <Row >
                    {/********* Conditional rendering based on user authentication ***********/}
                    {
                        isAuthenticated ?
                            <PetAdoptionComponent petId={petId} currentPetCategory={currentPetCategory} />
                            :
                            <TextComponent text="Only subscribed users have access to adoption services !" />
                    }
                </Row>
            </Container>
        </Container>
    );



};

export default PetDetailsView;