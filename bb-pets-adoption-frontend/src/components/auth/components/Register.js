import React, { useState } from 'react';
import axios from '../../../scripts/axiosConfig';
import { useNavigate } from 'react-router-dom'

/**
 * Rect Component
 */
const Register = () => {

    const [name, setName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [rePassword, setRePassword] = useState("");
    const [emailMessage, setEmailMessage] = useState("");
    const [passwordMessage, setPasswordMessage] = useState("");
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    /***
     * Password matching check
     */
    const checkPassword = (input) => {
        setRePassword(input)
        if (input !== password) {
            setPasswordMessage("Passwords do not match");
        } else {
            console.log(rePassword)
            setPasswordMessage("Passwords match");

        }
    }

    /**
    * Handles the form submission for user registration.
    * 
    * @param  e - The form submit event.
    */
    const handleSubmit = async (e) => {
        e.preventDefault(); // prevents the default form submition
        console.log(rePassword)
        // Check if the password and re-entered password match.
        if (rePassword !== password) {
            // Set the message state indicating that the passwords do not match.
            setPasswordMessage("Passwords do not match");
            return; // Stop form submission.
        } else {
            setPasswordMessage(''); // Clear the password message if passwords match.
        }


        try {
            // POST request to the registration endpoint with the user's details.
            const response = await axios.post('/auth/register', { name, lastName, email, password });

            // check if response exists
            if (response.status === 201 && response.data) {
                // Set the message state with the response data.
                setMessage(response.data);
                // Redirect to home page after successful registration
                navigate('/home')
            } else {
                setMessage("Unexpected error occurred. Please try again.");
            }

        } catch (error) {

            // Check if error response exists
            if (error.response && error.response.data) {
                // Set the message state with the error response data.
                setMessage(error.response.data);
            } else {
                // Set error message
                setMessage("Something went wrong. Please try again.");
            }
        }



    }

    return (
        <div>
            <h2>Register</h2>
            <form onSubmit={handleSubmit}>
                <label>Name</label>
                <input type="text" value={name} onChange={(e) => setName(e.target.value)} required />
                <label>Last name</label>
                <input type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} required />
                <label>Email</label>
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                {emailMessage && <p>{emailMessage}</p>}
                <label>Password</label>
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                <label>Re-enter password</label>
                <input type="password" value={rePassword} onChange={(e) => checkPassword(e.target.value)} required />
                {passwordMessage && <p>{passwordMessage}</p>}
                <button type="submit">Register</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    )
};

export default Register;