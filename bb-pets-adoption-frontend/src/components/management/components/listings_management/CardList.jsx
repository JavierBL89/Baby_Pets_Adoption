import React, { useState } from "react";
import { Container, Row, Col, Stack, Dropdown } from "react-bootstrap";
import ImageComponent from "../../../common/ImageComponent";
import TextComponent from "../../../common/TextComponent";
import { MdOutlineDelete } from "react-icons/md";
import { FaEdit } from "react-icons/fa";
import NotificationBadge from "../../../notifications/components/NotificationBadge";


/***
 * The CardList represents a card to display information about  apet listings visually organized and appealing
 * It includes various details such as the mother's image, birth date, creation date, price, 
 * and status of the pet. It also provides action buttons for updating and deleting the pet listing. 
 * 
 * Additionally, there is a link to view all pet applications
 */
const CardList = ({ motherImage, motherBreed, createdOn, price, token,
    birthDate, id, petId, onDelete, onUpdate, petListing, petCreatedOn, petUpdatedOn }) => {

    const [viewApplication, setviewApplication] = useState(false);

    /**** 
     * Method toggles the view application accordion.
     * 
     * Sets the state to the oposite of the current state is
     * false to true | true to false
    */
    const viewToggle = () => {
        setviewApplication(!viewApplication);
    }

    return (

        <Container id={`myPetCard_wrapper_${id}`} className="myPetCard_wrapper">
            <Container id={`myPetCard_container_${id}`} className="myPetCard_container">
                {/******* See Pet Applications Button *******/}
                <Row className="view_pet_applications_link_holder">

                    <Col className="view_pet_applications_link">
                        <a href={`/pet_applications/${petId}/${token}`} >
                            <small >See applications</small>
                        </a>
                        {petListing.pendingNotifications.length > 0 &&
                            <NotificationBadge text={petListing.pendingNotifications.length} />
                        }
                    </Col>

                </Row>
                <Row className="myPetCard_body">
                    {/******* Mother's Image *******/}
                    <Col xs={12} md={4} className="myPetCard_img_holder">
                        <ImageComponent src={motherImage} className={""} alt={""} />
                    </Col>

                    {/******* Birth and Status details *******/}
                    <Col xs={12} md={8} className="pt-1">
                        <span hidden ></span>
                        <Row className="myPetCard_details_row">
                            <Col >
                                <Row>
                                    <Col xs={12} md={6}> <small >Birth date:</small></Col>
                                    <Col xs={12} md={6}> <TextComponent text={`${birthDate[1]}/${birthDate[0]}`} /></Col>
                                </Row>
                            </Col>
                            <Col >
                                <Row>
                                    <Col xs={12} md={4}><small >Status:</small></Col>
                                    <Col xs={12} md={8}> <TextComponent text="Available" /></Col>
                                </Row>
                            </Col>
                        </Row>

                        <Row className="myPetCard_details_row">
                            {/******* Action buttons *******/}
                            <Col >
                                <Row>
                                    <Col xs={12} md={6}> <small >Created on:</small></Col>

                                    <Col xs={12} md={6}> <TextComponent text={petCreatedOn} /></Col>
                                </Row>

                            </Col>
                            {/******* Conditional Offer Type Rendering *******/}
                            <Col >
                                {price ?
                                    <Row>
                                        <Col xs={12} md={4}> <small >Price:</small></Col>
                                        <Col xs={12} md={8}> <TextComponent text={price.toString() + "€"} /></Col>
                                    </Row>
                                    :
                                    <Row>

                                        <Col xs={12} md={8}> <TextComponent text={"Free adoption"} /></Col>
                                    </Row>
                                }

                            </Col>
                        </Row>

                        <Row className="myPetCard_details_row">

                            {/******* Updated On *******/}
                            <Col >
                                <Row>
                                    <Col xs={12} md={7}> <small >Updated on:</small></Col>
                                    <Col xs={12} md={5}> <TextComponent text={petUpdatedOn ? petUpdatedOn : "N/S"} /></Col>
                                </Row>
                            </Col>
                            {/******* Action buttons *******/}
                            <Col >
                                <Row >
                                    <Col xs={4}>
                                        <FaEdit onClick={() => onUpdate(petListing.pet, petListing.id)} className="update_listing" />
                                    </Col>
                                    <Col xs={4} >
                                        <MdOutlineDelete onClick={() => viewToggle()} className="delete_listing" />


                                    </Col>
                                </Row>

                            </Col>

                        </Row>
                    </Col>


                </Row>

                {viewApplication ?
                    <Row className="drop_petList_accordion_wrapper">
                        <TextComponent className="drop_petList_warning_text" text="All data related to this application will be deleted, and application will not be recoverable." />
                        <TextComponent onClick={() => onDelete(petListing.id)} text="Confirm" className="btn drop_petList_confirm_button" />

                    </Row>
                    :
                    null
                }
            </Container>

        </Container>
    );

};


export default CardList;