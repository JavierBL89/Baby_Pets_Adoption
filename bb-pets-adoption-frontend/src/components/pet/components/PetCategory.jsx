import React, { useContext, useLocation, useNavigate } from "react";
import { Container, Row, Col } from "react-bootstrap";
import { AuthContext } from "../../../context/AuthContext";

/****
 * 
 * 
 */
const PetCategory = ({ onClick, url, title }) => {

    const { isAuthenticated } = useContext(AuthContext);


    return (

        <Container onClick={() => onClick(title)} title={title} className="pt-5 pb-5 mb-5">
            <h2 style={{ cursor: 'pointer' }}>{title}</h2>
        </Container>


    )
};
export default PetCategory;