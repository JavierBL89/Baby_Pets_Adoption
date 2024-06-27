import React from "react";
import { Nav, NavDropdown } from "react-bootstrap";
import Login from "../auth/components/Login";




const LogInDropDown = () => {

    return (

        <Nav >
            <NavDropdown
                id="nav-dropdown-dark-example"
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