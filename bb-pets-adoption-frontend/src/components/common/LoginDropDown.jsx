import React from "react";
import { Nav, NavDropdown } from "react-bootstrap";
import Login from "../auth/components/LoginHeader";




const LogInDropDown = ({ id }) => {

    return (

        <Nav >
            <NavDropdown
                id={id}
                title="SignIn"
                menuVariant="light"
                className="btn-group dropstart"
            >
                <Login />


            </NavDropdown>
        </Nav>
    )
}

export default LogInDropDown;