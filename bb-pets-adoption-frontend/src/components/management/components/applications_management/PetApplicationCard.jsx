import React, { useState } from "react";
import { Container, Row, Col, Accordion, Stack } from "react-bootstrap";
import ImageComponent from "../../../common/ImageComponent";
import TextComponent from "../../../common/TextComponent";
import { MdOutlineDelete } from "react-icons/md";
import { FaEdit } from "react-icons/fa";
import ButtonComponent from "../../../common/ButtonComponent";
import ViewApplicationComponent from "./ViewApplicationComponent";



/***
 * 
 * 
 */
const PetApplicationCard = ({ application, id, token, onFetchData }) => {

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

        <Container id={`pet_application_wrapper_${id}`} className="pet_application_wrapper ">
            <Container id={`pet_application_container_${id}`} className="pet_application_container ">
                <Row >

                    {/******* Application details *******/}
                    <Col xs={10}>
                        <Row className="application_details_row ">
                            {/******* Applicant Name *******/}
                            <Col xs={6} >
                                <Row>
                                    <Col xs={4}> <small > Name:</small></Col>
                                </Row>
                                <Row>
                                    <Col xs={7} > <TextComponent text={application.applicant.name} /></Col>
                                </Row>

                            </Col>
                            {/******* Location *******/}
                            <Col xs={6}>
                                <Row>
                                    <Col xs={4}> <small >Email address</small></Col>
                                    <Col xs={6}>  <TextComponent text={application.applicant.email} /></Col>
                                </Row>
                            </Col>
                        </Row>
                        <Row className="application_details_row ">
                            <Col >
                                <Row >
                                    <Col xs={4}> <small >Applied On:</small></Col>
                                    <Col xs={4}>
                                        <TextComponent text={`${application.applicationDate[2]} / ${application.applicationDate[1]} / ${application.applicationDate[0]}`} />
                                    </Col>
                                </Row>
                            </Col>
                            <Col >
                                <Row>
                                    <Col xs={4}><small >Status:</small></Col>
                                    <Col xs={8}> <TextComponent text={application.status} /></Col>
                                </Row>
                            </Col>
                        </Row>

                        <Row >
                            {/* <Col xs={6}>
                                <Row >
                                    <Col xs={6}> <small >Price</small></Col>

                                    <Col xs={6}> {price ? <small >{price}€</small> : <small >"Free Adoption"</small>}</Col>
                                </Row>
                            </Col> */}
                        </Row>
                    </Col>

                    <Col xs={2}>
                        <Row className="action-buttons-holder">
                            <ButtonComponent text="Drop" id="drop_button" className="btn" />
                        </Row>
                        <div id="separator"></div>
                        { /***** Adoption Accordion *****/}
                        <Row className="action-buttons-holder">
                            <ButtonComponent onClick={() => viewToggle()} text="View" id="view_button" className="btn" />
                        </Row>
                    </Col>
                </Row>
                {/************* toggle componenet when user clicks on above button ****** */}
                {viewApplication ?
                    <Row className="view_app_accordion_wrapper">
                        <ViewApplicationComponent
                            applicationId={application.id}
                            token={token}
                            onFetchData={onFetchData}
                        />

                    </Row>
                    :
                    null
                }

            </Container>
        </Container >
    );

};


export default PetApplicationCard;