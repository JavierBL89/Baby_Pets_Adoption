import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import PetCard from "./PetCard";



/***
 * 
 */
const PetListingHolder = () => {

    return (

        <Container className="m-auto" id="pet_listing_wrapper">
            <Container className="m-auto" id="pet_listing_container">
                <Row className="p-0">
                    <Col xs={6} md={3} lg={4} className="p-0">
                        <PetCard img="/assets/images/esperanza-doronila-ayQs0DB0tEo-unsplash.jpg" name="name" breed="breed" />
                    </Col>
                    <Col xs={6} md={3} lg={4} className="p-0">
                        <PetCard img="/assets/images/jairo-alzate-sssxyuZape8-unsplash.jpg" name="name" breed="breed" />
                    </Col>
                    <Col xs={6} md={3} lg={4} className="p-0">
                        <PetCard img="/assets/images/jacalyn-beales-CKsDMYPDgCs-unsplash.jpg" name="name" breed="breed" />
                    </Col>
                    <Col xs={6} md={3} lg={4} className="p-0">
                        <PetCard img="/assets/images/justin-veenema-NH1d0xX6Ldk-unsplash.jpg" name="name" breed="breed" />
                    </Col>
                    <Col xs={6} md={3} lg={4} className="p-0">
                        <PetCard img="/assets/images/michael-sum-LEpfefQf4rU-unsplash.jpg" name="name" breed="breed" />
                    </Col>
                    <Col xs={6} md={3} lg={4} className="p-0">
                        <PetCard img="/assets/images/pacto-visual-cWOzOnSoh6Q-unsplash.jpg" name="name" breed="breed" />
                    </Col>
                    <Col xs={6} md={3} lg={4} className="p-0">
                        <PetCard img="/assets/images/pauline-loroy-U3aF7hgUSrk-unsplash.jpg" name="name" breed="breed" />
                    </Col>
                    <Col xs={6} md={3} lg={4} className="p-0">
                        <PetCard img="/assets/images/angel-luciano-LATYeZyw88c-unsplash.jpg" name="name" breed="breed" />
                    </Col>
                </Row>

            </Container>
        </Container>
    )
};

export default PetListingHolder;