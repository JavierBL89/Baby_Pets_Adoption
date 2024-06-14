import React, { useState } from "react";
import axios from '../../../scripts/axiosConfig';
import { useNavigate, useLocation } from 'react-router-dom';



/**
 * PasswordReset component handles the password reset functionality
 * 
 * - Uses axios for making HTTP requests to the backend
 * - Uses useNavigate for redirecting the user to login page after a successful reset
 * - Uses useLocation for grabbing the token from the URL
 * - Receives request status and feedback from backend for each HTTP request,
 *  which is used to provide users with useful information
 * 
 * @returns the form for resetting the user's password
 */
const PasswordReset = () => {

    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [passwordMessage, setPasswordMessage] = useState("");
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    // get the token from the URL
    const searchParams = new URLSearchParams(useLocation().search);
    const token = searchParams.get('token');

    /***
    * Check passwords entered match
    * 
    * @param input the form submit event
    */
    const checkPassword = (input) => {
        setConfirmPassword(input)
        if (input !== newPassword) {
            setPasswordMessage("Passwords do not match");
        } else {
            setPasswordMessage("Passwords match");

        }
    }

    /**
     * Handle form submission
     * 
     * - Checks if the new password and re-entered password match
     * - Updates the password message state
     * - Handles the response from the server
     * 
     * @param {string} input  the re-entered password
     */
    const handleSubmit = async (e) => {
        e.preventDefault(); // prevents default form submission

        try {
            /* POST request to the reset_password endpoint with the user's data
            * sends an object to be received as a Map<String,String> data structure in endpoint*/
            const response = await axios.post('/auth/reset_password', { password: newPassword, token: token });

            // check if response exists
            if (response.status === 201 && response.data) {
                // Set the message state with the response data
                setMessage(response.data);
                // Redirect to login page
                navigate('/login')
            } else {
                setMessage(response.data);
            }

        } catch (error) {

            // check if error response exists
            if (error.response && error.response.data) {
                // set the message state with the error response data
                setMessage(error.response.data);
            } else {
                // set error message
                setMessage("Something went wrong. Please try again.");
            }
        }
    }
    return (
        <div>
            <h2>Reset Password</h2>
            <form onSubmit={handleSubmit}>

                <label>New Password</label>
                <input type="password" value={newPassword} onChange={(e) => { setNewPassword(e.target.value) }} required />
                <label>Re-enter new password</label>
                <input type="password" value={confirmPassword} onChange={(e) => { checkPassword(e.target.value) }} required />
                {passwordMessage && <p>{passwordMessage}</p>}
                {newPassword === confirmPassword ? <button type="submit">Submit</button> : null}

            </form>
        </div>
    )
};

export default PasswordReset;