import React from "react";
import { Badge, Container } from "react-bootstrap";



/**
 * The 'PetTag' component is a React component that displays a badge with a title and triggers an
 * 'onClick' event when clicked.
 * 
 * @param {string} title - the tag name
 * @param {function} onClick - the 'onClick' event
 * @returns The 'PetTag' component 
 */
const PetTag = ({ title, onClick }) => {

    return (
        <Container className="badge_tag" onClick={() => onClick(title)}>{title}</Container>
    );
};

export default PetTag;