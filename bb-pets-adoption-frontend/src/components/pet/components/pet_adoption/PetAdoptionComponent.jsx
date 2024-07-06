import React, { useCallback, useEffect, useState } from "react";
import { Container, Row, Col, Stack, Spinner, Button, Dropdown, Accordion } from "react-bootstrap";
import Heading from "../../../common/Heading";
import { useNavigate, useParams } from "react-router-dom";
import instance from "../../../../scripts/axiosConfig";
import TextComponent from "../../../common/TextComponent";
import ButtonComponent from "../../../common/ButtonComponent";
import PetAdoptionForm from "./PetAdoptionForm";


/***
 * Component acts as core for the addoption application section
 * 
 * It is responsibe for holding the form for a pet adoption, 
 * and any other components that could be added in future
 * 
 * @returns  'PetAdoptionComponent' - thr component
 */
const PetAdoptionComponent = ({ petId }) => {

    const [message, setMessage] = useState("");


    return (

        <Container id="pet_adoption_wrapper">
            <Container id="pet_adoption_container">
                <Row >

                    <Heading id="adoption_form_title" tagName="h4" text="Adoption Applications" />
                </Row>
                <Row >
                    <TextComponent id="before_application_text"
                        text="Before you apply....\n We'd like you to be sure of the responsabilities that come with introducing
                    a pet in yor life. \n Additionally we ask you ensure a correct communication with the pet provider,
                     and to make you aware that there could be applications before yours and pet provider could take so time to evaluate each application. 
                     In the meantime you can track your application status. We also notify you of any statues changes.\n "

                    />
                </Row>

                { /***** Adoption Accordion *****/}
                <Row id="adoption_form_accordion_wrapper">
                    <Accordion >
                        <Accordion.Item eventKey="0">
                            <Accordion.Header>Adoption Request</Accordion.Header>
                            <Accordion.Body>
                                <Heading tagName="h6" id="before_application_text"
                                    text="Such a beautiful decision from you!"

                                />
                                <PetAdoptionForm
                                    petId={petId}
                                />
                            </Accordion.Body>
                        </Accordion.Item>

                    </Accordion>
                </Row>
            </Container>

        </Container>
    );

};

export default PetAdoptionComponent;
