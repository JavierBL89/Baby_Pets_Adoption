import { useState } from "react";
import axios from '../../../scripts/axiosConfig';
import { useNavigate } from 'react-router-dom'
/**
 * 
 * @returns 
 */
const Login = () => {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const navigate = useNavigate();


    /**
     *  Handles the form submission for user login.
     * 
     * @param e - The form submit event.
     */
    const handleSubmit = async (e) => {
        e.preventDefault(); // Prevent the default form submission

        try {
            // POST request to the login endpoint with the user's credentials.
            const response = await axios.post('/auth/login', { email, password });

            // check if response exists
            if (response.status === 201 && response.data) {
                // Set the message state with the response data.
                setMessage("Login successful");
                // Redirect to home page after successful login
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
                // Set error message.
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
                <a>Forgot password</a>
                <button type="submit">Login</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    )
};

export default Login;