import { useState, useContext } from "react";
import instance from '../../../scripts/axiosConfig';
import { useNavigate } from 'react-router-dom'
import SocialLogin from "./SocialLogin";
import { AuthContext } from '../../../context/AuthContext';
import { Col, Container, Row, Form } from "react-bootstrap";
import Heading from "../../common/Heading";
import ButtonComponent from "../../common/ButtonComponent";
import { FeedbackContext } from "../../../context/FeedBackContext";

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
const LoginPage = () => {

    // set user state after successfull login
    const { login } = useContext(AuthContext);
    const { setPostActionMessage } = useContext(FeedbackContext)

    const [credentials, setCredentials] = useState({ email: '', password: '' });

    const [emailMessage, setEmailMessage] = useState("");
    const [passwordMessage, setPasswordMessage] = useState("");
    const [message, setMessage] = useState("");


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
                const response = await instance.post('/auth/forgot_password', { email: credentials.email });

                // check if response exists
                if (response.status === 200 && response.data) {
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
            const response = await instance.post('/auth/login', credentials);

            if (response.status === 401) {
                setMessage("Invalid email address or password. Please reset data entered and try again.");
            }

            // check if response exists
            if (response.status === 200 && response.data) {
                setPostActionMessage("Welcome back " + response.data.userName)
                login(response.data.token, response.data.registeredBy, response.data.userName);

            }


        } catch (error) {

            // check if error response exists
            if (error.response && error.response.status === 401) {
                setMessage("Invalid email address or password. Please reset data entered and try again.");
            } else if (error.response && error.response.data) {
                console.error("Error response data:", error.response.data);
                console.error("Error response status:", error.response.status);
                setMessage("Unexpected error occurred. Please try again.");
            } else {
                console.error(error);
                setMessage("Something went wrong. Please try again.");
            }
        }
    }

    return (
        <Container id="login_page_wrapper">
            <Container id="login_page_container">
                <Row >
                    {/* <Row >
                        <Heading tagName="h2" id="login_title" text="Login" />
                    </Row> */}
                    <Row >
                        <Form onSubmit={handleSubmit} id="login_form">
                            <Row >
                                {/******** { email address } ********/}
                                <Form.Group controlId="login_email">
                                    <Form.Label>Email Address</Form.Label>
                                    <Form.Control
                                        type="email"
                                        name="email"
                                        value={credentials.email}
                                        onChange={(e) => { handleCredentials(e) }}
                                        required
                                    />
                                </Form.Group>
                                {emailMessage && <p>{emailMessage}</p>}

                            </Row>

                            <Row >
                                {/******** { password } ********/}
                                <Form.Group controlId="login_password">
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control
                                        type="passsword"
                                        name="password"
                                        value={credentials.password}
                                        onChange={(e) => { handleCredentials(e) }}
                                        required
                                    />
                                </Form.Group>
                                {passwordMessage && <p>{passwordMessage}</p>}

                            </Row>
                            <Row id="login_page_action_holder">
                                <Col xs={12} md={6}>
                                    <button type="submit" id="login_page_submit_button" className="btn" >LogIn!</button>
                                </Col>
                                <Col xs={12} md={6} id="forgot_password_holder">

                                    <a href="#nopath" onClick={resetPassword}>Forgot password</a>

                                </Col>
                            </Row>

                        </Form>
                        {message && <p id="login_message">{message}</p>}
                        <div>
                            {/*<SocialLogin></SocialLogin>*/}
                        </div>
                    </Row>

                </Row>

            </Container>

        </Container>
    )
};

export default LoginPage;



<ButtonComponent type="submit" id="reg_submit_button" text="Go!" className="btn btn-primary" />
