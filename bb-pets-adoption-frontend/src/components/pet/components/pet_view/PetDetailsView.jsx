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
import { FeedbackContext } from "../../../../context/FeedBackContext";
import PostActionMessage from "../../../common/PostActionMessage";

/****
 * 
 * Componenet responsible for rendering the pet view page, with details of the the selected pet.
 * 
 * Component dependencies:
 * - It uses url params to grab the selected petId and the category of this, 
 * which will be crucial for retreivig the pet from database.
 * - Depends on 'AuthContext' to verify the user is authenticated for specific operations
 * - Depends on 'FeedbackContext' to display pots-action feedback messages
 * - Depends on 'useFetchById', a custom hook to make GET requests for a selected. It returns the pet data 
 *   and  usefule variables to be used for UX and error feedback
 */
const PetDetailsView = () => {

    const { petId, currentPetCategory } = useParams();

    const { isAuthenticated, userName } = useContext(AuthContext);
    const { postActionMessage } = useContext(FeedbackContext);

    const { loading, error, petData } = useFetchById(petId);

    useEffect(() => {
        window.scrollTo(0, 0);
    }, [petData]);

    return (
        <Container id="pet_view_wrapper" className="">
            <Container id="pet_view_container" className="">
                {/* Post-action Feedback message */}
                <Row id="post_action_message_holder">
                    <Col>
                        {!loading && postActionMessage && (
                            <PostActionMessage text={postActionMessage} />
                        )}
                    </Col>
                </Row>
                {loading && <p>Loading...</p>}
                {error && <p>Error: {error}</p>}
                {petData && (
                    <Row id="pet_view_info" className="">
                        {/* Pets's Mother Image */}
                        <Col id="pet_img_holder" className="pet_view_img_holder">
                            <ImageComponent src={petData.motherImg} alt={petData.breed} className="" />
                        </Col>

                        {/* Pet Details */}
                        <Col id="pet_details_holder" xs={12} md={6}>
                            <Row>
                                {/* Pets's Breed */}
                                <Row className="pet_details_row">
                                    <Col>
                                        <small>Mother Breed:</small>
                                        <TextComponent className="pet_detail_text" text={petData.motherBreed} />
                                    </Col>
                                    <Col>
                                        <small>Father Breed:</small>
                                        <TextComponent className="pet_detail_text" text={petData.fatherBreed ? petData.fatherBreed : "Not specified"} />
                                    </Col>
                                </Row>

                                {/* Pets's Birth and Location */}
                                <Row className="pet_details_row">
                                    <Col>
                                        <small>Birth Date:</small>
                                        <TextComponent className="pet_detail_text" text={`${petData.birthDate[1]}/${petData.birthDate[0]}`} />
                                    </Col>
                                    <Col>
                                        <small>Location:</small>
                                        <TextComponent className="pet_detail_text" text={petData.location} />
                                    </Col>
                                </Row>

                                {/* Pets's Price and Owner's Name */}
                                <Row className="pet_details_row">
                                    <Col>
                                        <small>Price:</small>
                                        <TextComponent className="pet_detail_text" text={petData.price ? petData.price : "Free Adoption"} />
                                    </Col>
                                    <Col>
                                        <small>Owner's Name:</small>
                                        <TextComponent className="pet_detail_text" text={petData.ownerName} />
                                    </Col>
                                </Row>
                            </Row>
                        </Col>

                        {/* Comments */}
                        <Col xs={12}>
                            <Row id="pet_details_comment_wrapper">
                                <Col>
                                    <small className="mt-2 ms-2">Comments</small>
                                    <Row id="pet_details_comments_holder">
                                        <Col>
                                            <TextComponent
                                                id="pet_comments_text"
                                                text={petData.description ? petData.description : "No comments"}
                                            />
                                        </Col>
                                    </Row>
                                </Col>
                            </Row>
                        </Col>
                    </Row>
                )}

                <Row>
                    {/* Conditional rendering based on user authentication */}
                    {isAuthenticated ? (
                        <PetAdoptionComponent petId={petId} userName={userName} currentPetCategory={currentPetCategory} />
                    ) : (
                        <PostActionMessage text={"Only subscribed users have access to adoption services!"} />
                    )}
                </Row>
            </Container>
        </Container>
    );



};

export default PetDetailsView;