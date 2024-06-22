import React, { useContext, useLocation, useNavigate } from "react";
import { Container, Row, Col } from "react-bootstrap";
import { AuthContext } from "../../../../context/AuthContext";



/**
 * Component represents a pet category
 * 
 * When clicked, it triggers an action to update the current pet category in the application state
 *
 * @param {function} onClick - the function called when the category is clicked
 * @param {string} title - the title of the pet category
 */

const PetCategory = ({ onClick, title }) => {

    // state for displaying certain info and elements based on authentication conditions
    const { isAuthenticated } = useContext(AuthContext);


    return (

        <Container onClick={() => onClick(title)} title={title} className="pt-5 pb-5 mb-5">
            <h2 style={{ cursor: 'pointer' }}>{title}</h2>
        </Container>


    )
};
export default PetCategory;