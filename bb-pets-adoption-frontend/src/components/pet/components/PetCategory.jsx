import React, { useContext, useLocation, useNavigate } from "react";
import { Container, Row, Col } from "react-bootstrap";
import { AuthContext } from "../../../context/AuthContext";

/****
 * 
 * 
 */
const PetCategory = (props) => {

    const { isAuthenticated } = useContext(AuthContext);


    return (

        <Container className="pt-5 pb-5 mb-5">
            <a href={props.url}> <h2>{props.title}</h2> </a>
        </Container>


    )
};
export default PetCategory;