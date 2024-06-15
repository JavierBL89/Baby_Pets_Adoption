import React from "react";
import { Container, Row, Col } from "react-bootstrap";



/***
 * 
 */
const PetCard = ({ img, breed, name }) => {

    return (

        <Container className="m-auto pet_card_wrapper" id="" >
            <Container className="m-auto p-0" id="pet_card_container">
                <Container className="img_holder p-0">
                    <img src={img} alt="Pet" />
                </Container>
                <Row flex className="p-0">
                    <Col className="" >
                        <p>{name}</p>
                    </Col >
                    <Col className="">
                        <p>{breed}</p>
                    </Col>
                </Row>
                <Row className="">
                    <Col className="text-center">
                        <a href="#" >See more</a>
                    </Col>

                </Row>

            </Container>
        </Container>
    )
};

export default PetCard;