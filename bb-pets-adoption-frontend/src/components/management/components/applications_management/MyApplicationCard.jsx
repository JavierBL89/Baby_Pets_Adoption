import React from "react";
import { Container, Row, Col, Dropdown } from "react-bootstrap";
import ImageComponent from "../../../common/ImageComponent";
import TextComponent from "../../../common/TextComponent";
import { MdOutlineDelete } from "react-icons/md";
import { FaEdit } from "react-icons/fa";



/***
 * 
 * 
 */
const MyApplicationCard = ({ applicationDate, id, applicationId, petId, price,
    location, onDelete, onUpdate, motherImg, motherBreed, ownerName, status }) => {

    return (

        <Container id={`application_wrapper_${id}`} className="applicationCard_wrapper">
            <Container id={`application_container_${id}`} className="applicationCard_container">
                <Row >
                    {/******* Mother's Image *******/}
                    <Col xs={4} className="application_img_holder">
                        <ImageComponent src={motherImg} className={""} alt={motherBreed} />
                    </Col>

                    {/******* Application details *******/}
                    <Col xs={8}>
                        <Row className="application_details_row">
                            <Col >
                                <Row>
                                    <Col xs={6}> <small >Applied On:</small></Col>
                                    <Col xs={6}>
                                        <TextComponent text={`${applicationDate[2]}/${applicationDate[1]}/${applicationDate[0]}`} />
                                    </Col>
                                </Row>
                            </Col>
                            <Col >
                                <Row>
                                    <Col xs={4}><small >Status:</small></Col>
                                    <Col xs={8}> <TextComponent text={status} /></Col>
                                </Row>
                            </Col>
                        </Row>

                        <Row className="application_details_row">
                            {/******* Owner Name *******/}
                            <Col >
                                <Row>
                                    <Col xs={6}> <small >Owner's Name:</small></Col>
                                </Row>
                                <Row>
                                    <Col xs={6} > <TextComponent text={ownerName} /></Col>
                                </Row>

                            </Col>
                            {/******* Location *******/}
                            <Col >
                                <Row>
                                    <Col xs={6}> <small >Pet's Location</small></Col>
                                    <Col xs={6}> <small >{location}</small></Col>
                                </Row>
                            </Col>
                        </Row>
                        <Row >
                            <Col xs={6}>
                                <Row >
                                    <Col xs={6}> <small >Price</small></Col>

                                    <Col xs={6}> {price ? <small >{price}€</small> : <small >"Free Adoption"</small>}</Col>
                                </Row>
                            </Col>
                        </Row>
                    </Col>
                </Row>

                <Row className="application_drop_holder">
                    {/******* Drop app button *******/}
                    <Col >
                        <Row >
                            <Dropdown className="p-0 m-0">
                                <Dropdown.Toggle className="drop_application_dropdown no-arrow ">
                                    Drop
                                </Dropdown.Toggle>
                                <Dropdown.Menu>
                                    <Dropdown.Item as="div">
                                        <TextComponent className="drop_application_warning" text="All data related to this application will be deleted, and application will not be recoverable." />
                                        <TextComponent onClick={() => onDelete(applicationId)} text="Confirm" className="btn btn-primary drop_application_confirm" />
                                    </Dropdown.Item>
                                </Dropdown.Menu>
                            </Dropdown>
                        </Row>

                    </Col>

                </Row>
            </Container>

        </Container>
    );

};


export default MyApplicationCard;