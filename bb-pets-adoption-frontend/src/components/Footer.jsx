import React, { useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import { Container, Navbar, Nav, Stack, Row } from "react-bootstrap";
import LogoutButton from "./auth/components/LogoutButton";





/* 
* Header component holds the navigation top-bar of the application
*
* It uses React Boostrap components for grid an layout
* 
* Here's a breakdown of what the code is doing: 
* - It checks the application isAuthenticated state from the AuthContext
* - Conditional rendering is used to display different links based on whether the user is authencticated in the current session or not
*
* @returns the header navigation var
*/
const Footer = () => {

    const { isAuthenticated } = useContext(AuthContext);


    return (
        <Navbar id="footer_navbar" expand="lg" bg="dark" variant="dark" fixed="bottom" className="mt-auto">
            <Stack className="mt-5 text-center" >
                {/**  Nav Links **/}
                <Nav className="m-auto" >
                    <Row className="ms-lg-5">
                        <Nav.Link id="footer_about_link" href="#about">About Us</Nav.Link>
                        <Nav.Link id="footer_contact_link" href="#services">Contact</Nav.Link>
                    </Row>
                    <Row className="ms-lg-5">
                        <Nav.Link id="footer_services_link" href="#contact"> Services</Nav.Link>
                        {
                            !isAuthenticated ? (
                                <Nav.Link id="footer_signin_link" href="#privacy">SignIn</Nav.Link>
                            ) : (
                                <Nav.Link > <LogoutButton /> </Nav.Link>
                            )
                        }
                    </Row>
                    <Row className="ms-lg-5">
                        <Nav.Link id="footer_privacy_link" href="/privacy_policy">Privacy Policy</Nav.Link>
                        <Nav.Link id="footer_data_deletion_link" href="/data_deletion" >Data Deletion</Nav.Link>

                    </Row>

                </Nav>
                <Navbar.Text className="text-muted">
                    {new Date().getFullYear()} BbPets Adoption. All Rights Reserved.
                </Navbar.Text>

                <Navbar.Brand id="logo_nav" href="#home">BbPets</Navbar.Brand>
            </Stack >

        </Navbar>
    )
};


export default Footer;