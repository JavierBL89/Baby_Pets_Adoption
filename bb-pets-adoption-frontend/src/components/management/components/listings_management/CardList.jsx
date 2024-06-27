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
    birthDate, id, objectId, onDelete, onUpdate }) => {

    return (


        <Container id={`listing_wrapper_${id}`} className="cardList_wrapper">
            <Container id={`listing_container_${id}`} className="cardList_container">
                <Row >

                    <Col xs={4} className="listing_img_holder">

                        <ImageComponent src={motherImage} className={""} alt={""} />

                    </Col>


                    <Col xs={8}>
                        <span hidden ></span>
                        <Row className="listing_details_row">

                            <Col >
                                <Row>
                                    <Col xs={6}> <small >Birth date:</small></Col>
                                    <Col xs={6}> <TextComponent text={createdOn.toString()} /></Col>
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
                            <Col >
                                <Row >
                                    <Col >
                                        <FaEdit onClick={() => onUpdate(objectId)} className="update_listing" />

                                    </Col>
                                    <Col >
                                        <MdOutlineDelete onClick={() => onDelete(objectId)} className="delete_listing" />

                                    </Col>
                                    <Col>{/***** FUTURE ADDONS */}</Col>
                                    <Col>{/***** FUTURE ADDONS */}</Col>

                                </Row>

                            </Col>

                            <Col >
                                <Row>
                                    <Col xs={4}> <small >Price:</small></Col>
                                    <Col xs={8}> <TextComponent text={price.toString() + "€"} /></Col>
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