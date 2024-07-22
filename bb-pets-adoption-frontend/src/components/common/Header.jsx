import React, { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
import { Container, Navbar, Nav } from "react-bootstrap";
import NavLinkComponent from "./NavLinkComponent";
import NavbarDropDown from "./NavbarDropdown";
import LogInDropDown from "./LogInDropDown";
import ImageComponent from "./ImageComponent";



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
    const token = localStorage.getItem("token")


    return (
        <Navbar id="header_navbar" collapseOnSelect sticky="top" expand="lg" className="">
            <Container>
                <Navbar.Brand id="logo_nav" href="/">
                    <Container id="logo_nav_holder" />
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="ms-auto">
                        {/**  Nav Links **/}
                        <Nav.Link id="home_link_nav" href={`/${token}`}>Main</Nav.Link>
                        { // condition to display different links
                            !isAuthenticated ? (
                                <>
                                    <LogInDropDown id="signIn_button" />
                                </>

                            ) : (
                                <NavbarDropDown id="account_dropdown" />

                            )
                        }
                    </Nav>
                </Navbar.Collapse>
            </Container>

        </Navbar>
    )
};


export default Header;