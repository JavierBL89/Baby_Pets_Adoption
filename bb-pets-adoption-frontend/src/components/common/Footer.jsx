import React, { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
import { Container, Navbar, Nav, Stack, Row } from "react-bootstrap";
import LogoutButton from "../auth/components/LogoutButton";
import NavLinkComponent from "./NavLinkComponent";





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
        <Navbar id="footer_navbar" expand="lg" className="mt-auto">

            <Stack className="mt-5 text-center" >
                <Container id="footer_bg_holder" >
                    <img src={process.env.PUBLIC_URL + "/assets/images/FootprintPathBig.png"} alt="background" />

                </Container>
                {/**  Nav Links **/}
                <Nav className="m-auto" >
                    <Row className="ms-lg-5">
                        <NavLinkComponent id="footer_about_link" href="/about" text="About Us" />
                        <NavLinkComponent id="footer_contact_link" href="/contact" text="Contact" />
                    </Row>
                    <Row className="ms-lg-5">
                        <NavLinkComponent id="footer_services_link" href="/Services" text="Services" />
                        <NavLinkComponent id="footer_payment_policy_link" href="/payment_policy" text="Payment Policy" />
                        {
                            /*    !isAuthenticated ? (
                                    <NavLinkComponent id="footer_signin_link" href="#privacy" text="SignIn" />
                                ) : (
                                    <NavLinkComponent > <LogoutButton /> </NavLinkComponent>
                                )*/
                        }
                    </Row>
                    <Row className="ms-lg-5">
                        <NavLinkComponent id="footer_privacy_link" href="/privacy_policy" text="Privacy Policy" />
                        <NavLinkComponent id="footer_data_deletion_link" href="/data_deletion" text="Data Deletion" />
                    </Row>

                </Nav>
                <Navbar.Brand id="logo_footer" href="/home"></Navbar.Brand>
                <Navbar.Text className="" id="copyright">
                    {new Date().getFullYear()} BbPets Adoption. All Rights Reserved.
                </Navbar.Text>
            </Stack >

        </Navbar >
    )
};


export default Footer;