import React, { useContext } from "react";
import { AuthContext } from "../../../../context/AuthContext";
import { Col, Container, Row } from "react-bootstrap";
import NewPetForm from "./NewPetForm";

/***
 * 
 */

const CreatePet = () => {

    const { isAuthenticated } = useContext(AuthContext);

    return (
        <Container id="create_pet_wrapper">
            <Container id="create_pet_container">

                <Row>
                    <NewPetForm />

                </Row>
            </Container>
        </Container>

    )

};

export default CreatePet;