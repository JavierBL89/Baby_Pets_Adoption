import { useState } from "react";
import axios from '../../../scripts/axiosConfig';
import { useNavigate } from 'react-router-dom'
/**
 * Login component handles the login functionality
 * 
 * - Uses axios for making HTTP requests to the backend.
 * - Uses useNavigate for redirecting the user to login page after a successful reset.
 * - Uses useLocation for extracting the token from the URL.
 * - Receives request status and feedback from backend for each HTTP request,
 *  which is used to provide users with useful information
 * 
 * @returns the form for login 
 */
const Login = () => {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const navigate = useNavigate();


    /**
     *  Handles the initial step for the password reset functionality
     *  Grabs the entered email and sends it to backend 
     *  Uses axios for making the HTTP reuest
     * 
     * @param e the form submit event
     */
    const resetPassword = async () => {

        if (email !== "") {
            try {
                // POST request to the /auth/forgot_password endpoint with the user's credentials
                const response = await axios.post('/auth/forgot_password', { email });

                // check if response exists
                if (response.status === 201 && response.data) {
                    // set the message state with the response data
                    setMessage(response.data);
                    // redirect to home page after successful login
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
        } else {
            setMessage("Please enter an email address and click 'fogot password'.");
        }
    }

    /**
     *  Handles the form submission for user login
     *  Uses axios for making HTTP request to backend
     * 
     * @param e the form submit event
     */
    const handleSubmit = async (e) => {
        e.preventDefault(); // prevents the default form submission

        try {
            // POST request to the /login endpoint with the user's credentials
            const response = await axios.post('/auth/login', { email, password });

            // check if response exists
            if (response.status === 201 && response.data) {
                // set the message state with the response data
                setMessage("Login successful");
                // redirect to home page after successful login
                navigate('/home')
            } else {
                setMessage("Unexpected error occurred. Please try again.");
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
            <h2>Login</h2>
            <form onSubmit={handleSubmit}>
                <label>Email Address</label>
                <input type="email" value={email} onChange={(e) => { setEmail(e.target.value) }} required />
                <label>Password</label>
                <input type="password" value={password} onChange={(e) => { setPassword(e.target.value) }} required />
                <button type="submit">Login</button>
            </form>
            <button type="button" onClick={resetPassword}>Forgot password</button>

            {message && <p>{message}</p>}
        </div>
    )
};

export default Login;