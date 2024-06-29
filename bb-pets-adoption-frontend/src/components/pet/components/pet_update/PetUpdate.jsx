import React, { useContext } from "react";
import { AuthContext } from "../../../../context/AuthContext";
import { Col, Container, Row } from "react-bootstrap";
import PetUpdateForm from "./PetUpdateForm";

/***
 * 
 */

const PetUpdate = () => {

    const { isAuthenticated } = useContext(AuthContext);

    return (
        <Container id="update_pet_wrapper">
            <Container id="update_pet_container">

                <Row>
                    <PetUpdateForm />

                </Row>
            </Container>
        </Container>

    )

};

export default PetUpdate;