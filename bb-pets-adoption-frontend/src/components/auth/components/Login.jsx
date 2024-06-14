import { useState, useContext } from "react";
import instance from '../../../scripts/axiosConfig';
import { useNavigate } from 'react-router-dom'
import SocialLogin from "./SocialLogin";
import { AuthContext } from '../../../context/AuthContext';

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


    const [credentials, setCredentials] = useState({ email: '', password: '' });
    const [message, setMessage] = useState("");
    const navigate = useNavigate();
    const { login, setRegisteredBy } = useContext(AuthContext);

    /**
     * Handles form input changes and updates the credentials state
     *
     * @param e The form input change event.
     */
    const handleCredentials = (e) => {
        const { name, value } = e.target;
        setCredentials({ ...credentials, [name]: value });
    }


    /**
     *  Handles the initial step for the password reset functionality
     *  Grabs the entered email and sends it to backend 
     *  Uses axios for making the HTTP reuest
     * 
     * @param e the form submit event
     */
    const resetPassword = async () => {

        if (credentials.email !== "") {
            try {
                // POST request to the /auth/forgot_password endpoint with the user's credentials
                const response = await instance.post('/auth/forgot_password', credentials.email);

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
     * 
     * - Uses axios for making HTTP request to backend
     * - Handles the response from the server
     * 
     * @param e the form submit event
     */
    const handleSubmit = async (e) => {
        e.preventDefault(); // prevents the default form submission

        try {
            // POST request to the /login url enpoint

            const response = await instance.post('/login', credentials);

            // check if response exists
            if (response.status === 201 && response.data) {
                // set the message state with the response data
                setMessage(response.data.message);

                login(response.data.token, response.data.registeredBy);
            } else {
                setMessage("Unexpected error occurred. Please try again.");
            }

        } catch (error) {

            // check if error response exists
            if (error.response && error.response.data) {
                // check if error response exists
                if (error.response) {
                    console.error("Error response data:", error.response.data);
                    console.error("Error response status:", error.response.status);
                    // set the message state with the error response data
                    setMessage(error.response.data);
                }
            } else {
                // set error message and log error
                console.error(error);
                setMessage("Something went wrong. Please try again.");
            }
        }
    }

    return (
        <div>
            <h2>Login</h2>
            <form onSubmit={handleSubmit}>
                <label>Email Address</label>
                <input type="email" value={credentials.email} name="email" onChange={(e) => { handleCredentials(e) }} required />
                <label>Password</label>
                <input type="password" value={credentials.password} name="password" onChange={(e) => { handleCredentials(e) }} required />
                <button type="submit">Login</button>
            </form>
            <button type="button" onClick={resetPassword}>Forgot password</button>
            {message && <p>{message}</p>}

            <div>
                {/*<SocialLogin></SocialLogin>*/}
            </div>
        </div>
    )
};

export default Login;