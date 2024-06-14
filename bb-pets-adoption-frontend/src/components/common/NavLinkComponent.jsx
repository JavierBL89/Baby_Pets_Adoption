import React from 'react';
import { Nav } from 'react-bootstrap';


/***
 * Reusable component for navbar links
 * 
 * @prop href - the url link
 * @prop text - the desired text
 * @prop text - the class name
 */
const NavLinkComponent = ({ href, text, className, id }) => {
    return (
        <Nav.Link href={href} className={className}>
            {text}
        </Nav.Link>
    );
};

export default NavLinkComponent;