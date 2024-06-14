import React, { useContext } from "react";
import { AuthContext } from '../../../context/AuthContext';
import { useNavigate } from 'react-router-dom'
import { Container, Row, Col } from "react-bootstrap";
import PetCategory from "./PetCategory";
import Heading from "../../common/Heading";
/****
 * 
 * 
 */
const PetCategoriesHolder = () => {

    const { isAuthenticated } = useContext(AuthContext);



    return (

        <Container className="mt-5 mb-5 pb-5 bg-warning w-60" >
            <Heading tagName="h1" id="pet_categories_heading" className="text-center mt-4 mb-5" text="Pet Categories" />
            <Row display="flex" direction="row" className="text-center w-50 m-auto">

                <Col>
                    <PetCategory url="" title="Kitties" />
                </Col>
                <Col>
                    <PetCategory url="" title="Puppies" />
                </Col>
                <Col>
                    <PetCategory url="" title="Elephants" />

                </Col>
            </Row>
            <Row>
                {isAuthenticated ? (
                    <div>
                        <p>SingIn to access adoption services</p>
                    </div>
                )
                    :
                    null
                }
            </Row>
        </Container>


    )
}

export default PetCategoriesHolder;