import React from "react";
import { Container, Row, Col, Stack } from "react-bootstrap";
import ImageComponent from "../../../common/ImageComponent";
import TextComponent from "../../../common/TextComponent";
import { MdOutlineDelete } from "react-icons/md";
import { FaEdit } from "react-icons/fa";


/***
 * The CardList represents a card to display information about  apet listings visually organized and appealing
 * It includes various details such as the mother's image, birth date, creation date, price, 
 * and status of the pet. It also provides action buttons for updating and deleting the pet listing. 
 * 
 * Additionally, there is a link to view all pet applications
 */
const CardList = ({ motherImage, motherBreed, createdOn, price, token,
    birthDate, id, petId, onDelete, onUpdate, petListing, petCreatedOn, petUpdatedOn }) => {

    return (

        <Container id={`myPetCard_wrapper_${id}`} className="myPetCard_wrapper">
            <Container id={`myPetCard_container_${id}`} className="myPetCard_container">
                {/******* See Pet Applications Button *******/}
                <Row className="view_pet_applications_link_holder">

                    <Col className="view_pet_applications_link">
                        <a href={`/pet_applications/${petId}/${token}`} >
                            <small >Applications</small>
                        </a>
                    </Col>

                </Row>
                <Row className="myPetCard_body">
                    {/******* Mother's Image *******/}
                    <Col xs={4} className="myPetCard_img_holder">
                        <ImageComponent src={motherImage} className={""} alt={""} />
                    </Col>

                    {/******* Birth and Status details *******/}
                    <Col xs={8} className="pt-1">
                        <span hidden ></span>
                        <Row className="myPetCard_details_row">
                            <Col >
                                <Row>
                                    <Col xs={6}> <small >Birth date:</small></Col>
                                    <Col xs={6}> <TextComponent text={`${birthDate[1]}/${birthDate[0]}`} /></Col>
                                </Row>
                            </Col>
                            <Col >
                                <Row>
                                    <Col xs={4}><small >Status:</small></Col>
                                    <Col xs={8}> <TextComponent text="Status Here" /></Col>
                                </Row>
                            </Col>
                        </Row>

                        <Row className="myPetCard_details_row">
                            {/******* Action buttons *******/}
                            <Col >
                                <Row>
                                    <Col xs={6}> <small >Created on:</small></Col>

                                    <Col xs={6}> <small >{petCreatedOn}</small></Col>
                                </Row>

                            </Col>
                            {/******* Conditional Offer Type Rendering *******/}
                            <Col >
                                {price ?
                                    <Row>
                                        <Col xs={4}> <small >Price:</small></Col>
                                        <Col xs={8}> <TextComponent text={price.toString() + "€"} /></Col>
                                    </Row>
                                    :
                                    <Row>

                                        <Col xs={8}> <TextComponent text={"Free adoption"} /></Col>
                                    </Row>
                                }

                            </Col>
                        </Row>

                        <Row className="myPetCard_details_row">

                            {/******* Updated On *******/}
                            <Col >
                                <Row>
                                    <Col xs={7}> <small >Updated on:</small></Col>
                                    <Col xs={5}> <small >{petUpdatedOn ? petUpdatedOn : "N/S"}</small></Col>
                                </Row>
                            </Col>
                            {/******* Action buttons *******/}
                            <Col >
                                <Row >
                                    <Col xs={4}>
                                        <FaEdit onClick={() => onUpdate(petListing.pet, petListing.id)} className="update_listing" />
                                    </Col>
                                    <Col xs={4} >
                                        <MdOutlineDelete onClick={() => onDelete(petId)} className="delete_listing" />
                                    </Col>
                                </Row>

                            </Col>

                        </Row>
                    </Col>
                </Row>


            </Container>

        </Container>
    );

};


export default CardList;