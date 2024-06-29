import React from "react";
import { Container, Row, Col, Stack } from "react-bootstrap";
import ImageComponent from "../../../common/ImageComponent";
import TextComponent from "../../../common/TextComponent";
import { MdOutlineDelete } from "react-icons/md";
import { FaEdit } from "react-icons/fa";



/***
 * 
 * 
 */
const CardList = ({ motherImage, motherBreed, createdOn, price,
    birthDate, id, petId, onDelete, onUpdate, petListing, petCreatedOn, petUpdatedOn }) => {

    return (

        <Container id={`listing_wrapper_${id}`} className="cardList_wrapper">
            <Container id={`listing_container_${id}`} className="cardList_container">
                <Row >
                    {/******* Mother's Image *******/}
                    <Col xs={4} className="listing_img_holder">
                        <ImageComponent src={motherImage} className={""} alt={""} />
                    </Col>

                    {/******* Birth and Status details *******/}
                    <Col xs={8}>
                        <span hidden ></span>
                        <Row className="listing_details_row">
                            <Col >
                                <Row>
                                    <Col xs={6}> <small >Birth date:</small></Col>
                                    <Col xs={6}> <TextComponent text={birthDate} /></Col>
                                </Row>
                            </Col>
                            <Col >
                                <Row>
                                    <Col xs={4}><small >Status:</small></Col>
                                    <Col xs={8}> <TextComponent text="Status Here" /></Col>
                                </Row>
                            </Col>
                        </Row>

                        <Row className="listing_details_row">
                            {/******* Action buttons *******/}
                            <Col >
                                <Row >
                                    <Col >
                                        <FaEdit onClick={() => onUpdate(petListing.pet, petListing.id)} className="update_listing" />
                                    </Col>
                                    <Col >
                                        <MdOutlineDelete onClick={() => onDelete(petId)} className="delete_listing" />
                                    </Col>
                                    <Col>{/***** FUTURE ADDONS */}</Col>
                                    <Col>{/***** FUTURE ADDONS */}</Col>

                                </Row>

                            </Col>
                            {/******* Price *******/}
                            <Col >
                                <Row>
                                    <Col xs={4}> <small >Price:</small></Col>
                                    <Col xs={8}> <TextComponent text={price.toString() + "€"} /></Col>
                                </Row>
                            </Col>
                        </Row>

                        <Row className="listing_details_row">
                            {/******* Action buttons *******/}
                            <Col >
                                <Row>
                                    <Col xs={7}> <small >Created on:</small></Col>
                                </Row>
                                <Row>
                                    <Col xs={5}> <small >{petCreatedOn}</small></Col>
                                </Row>

                            </Col>
                            {/******* Price *******/}
                            <Col >
                                <Row>
                                    <Col xs={7}> <small >Updated on:</small></Col>
                                    <Col xs={5}> <small >{petUpdatedOn}</small></Col>
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