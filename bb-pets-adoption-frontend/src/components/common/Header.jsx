import React, { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
import { Container, Navbar, Nav } from "react-bootstrap";
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
const Header = () => {

    const { isAuthenticated } = useContext(AuthContext);


    return (
        <Navbar id="header_navbar" collapseOnSelect sticky="top" expand="lg" className="bg-body-tertiary">
            <Container>
                <Navbar.Brand id="logo_nav" href="#home">BbPets</Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="ms-auto">
                        {/**  Nav Links **/}
                        <Nav.Link id="home_link_nav" href="#home">Main</Nav.Link>
                        { // condition to display different links
                            !isAuthenticated ? (

                                <NavLinkComponent id="signin_link_nav" href="/login" text="SignIn" />
                            ) : (
                                <>
                                    <NavLinkComponent id="signout_link_nav" href="#signOut" text="SignOut" />
                                    <NavLinkComponent id="profile_link_nav" href="#profile" text="Profile" />
                                </>
                            )
                        }
                    </Nav>
                </Navbar.Collapse>
            </Container>

        </Navbar>
    )
};


export default Header;