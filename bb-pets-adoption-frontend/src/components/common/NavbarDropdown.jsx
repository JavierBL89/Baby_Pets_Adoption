import React, { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
import { Container, Navbar, Nav, NavDropdown } from "react-bootstrap";
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
const NavbarDropDown = () => {

    const { isAuthenticated, logout } = useContext(AuthContext);


    const token = localStorage.getItem('token');

    return (
        <NavDropdown
            id="nav-dropdown-dark-example"
            title="Dropdown"
            menuVariant="light"
        >
            <NavDropdown.Item href="#action/3.1">
                <NavLinkComponent id="profile_link_nav" href={`#profile/?token=${token}`} text="Profile" />
            </NavDropdown.Item>
            <NavDropdown.Item href="#action/3.2">
                <NavLinkComponent id="profile_link_nav" href={`/list_new_pet/${token}`} text="List a Pet" />
            </NavDropdown.Item>

            <NavDropdown.Item href="#action/3.2">
                <NavLinkComponent id="profile_link_nav" href={`/notifications/${token}`} text="Notifications" />
            </NavDropdown.Item>

            <NavDropdown.Divider />
            <NavDropdown.Item href="#action/3.4">
                <NavLinkComponent id="signout_link_nav" onClic={() => logout()} href={"#signOut"} text="SignOut" />

            </NavDropdown.Item>
        </NavDropdown>

    )
};


export default NavbarDropDown;