import React, { useState, useContext } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import instance from "../../../scripts/axiosConfig";

import { AuthContext } from "../../../context/AuthContext";

const LogoutButton = () => {

    const { logout, registeredBy } = useContext(AuthContext);
    const navigate = useNavigate();
    const location = useLocation();
    const [message, setMessage] = useState("");

    /**
     * Handles the application logout process for the user
     * 
     * This function checks the user's registration method and and builds the appropriate
     * logout endpoint URL based on whether the user registered via email or Google
     * 
     * - If the user was registered by email, it appends the token as a query parameter to the logout endpoint 
     * - If the user registered by Google, it uses a different endpoint for Google logout
     * 
     * Handles the response from the server:
     * - If success logout (status 201), it updates the message state, clears the authentication state using the 
     *   `logout` function from `AuthContext`, and navigates the user to the home page
     * - On failure logout , it logs the error and sets an error message in the state
     * 
     * @param {Event} e - the click event triggering the logout process
     */
    const handleLogout = async (e) => {

        // check if there's a token in the URL query parameters
        const searchParams = new URLSearchParams(location.search);
        const token = searchParams.get('token') || localStorage.getItem('token');
        let requestUrl = "";

        if (registeredBy === "email") {
            requestUrl = `http://localhost:8080/auth/logout?token=${token}`;

        } else {
            requestUrl = 'http://localhost:8080/auth/google_logout';
        }

        // try POST request to the appropiate url enpoint
        try {

            const response = await instance.post(requestUrl);
            // check if response exists
            if (response.status === 201 && response.data) {
                // set the message state with the response data
                setMessage(response.data);

                // set session state from AuthContext
                logout();

                // redirect to home page after successful login
                navigate("/home");
            } else {
                setMessage(response.data);
            }

        } catch (error) {

            // set error message and log error
            console.error("Logout failed: ", error)
            setMessage("Something went wrong and you cpuld not logout. Please tr again or contact site administrators");
        }
    }
    return (
        <div>
            <button onClick={handleLogout}>LogOut</button>
        </div>
    );
}

export default LogoutButton;
