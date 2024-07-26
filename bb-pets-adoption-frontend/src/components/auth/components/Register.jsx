import React, { useState } from 'react';
import axios from '../../../scripts/axiosConfig';
import { useNavigate, useLocation } from 'react-router-dom'
import { AuthContext } from '../../../context/AuthContext';
import { Row, Col, Form, Container } from 'react-bootstrap';
import ButtonComponent from '../../common/ButtonComponent';
import Heading from '../../common/Heading';


/**
 * Register component handles the userregistration functionality
 * 
 * - Uses axios for making HTTP requests to the backend
 * - Uses useNavigate for redirecting the user to login page after a successful reset.
 * - Receives request status and feedback from backend for each HTTP request,
 *  which is used to provide users with useful information
 * 
 * @returns the form for resetting the user's password
 */
const Register = () => {

    const [name, setName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [confirmEmail, setConfirmEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [emailMessage, setEmailMessage] = useState("");
    const [confirmEmailMessage, setConfirmEmailMessage] = useState("");
    const [passwordMessage, setPasswordMessage] = useState("");
    const [confirmPasswordMessage, setConfirmPasswordMessage] = useState("");
    const [message, setMessage] = useState("");
    const navigate = useNavigate();


    /***
     * Check passwords entered match
     * Reset re-entered password state
     * @param {input}
     */
    const checkPassword = (input) => {
        setConfirmPassword(input)
        if (input !== password) {
            setPasswordMessage("Passwords do not match");
            return;
        } else {
            setPasswordMessage("Passwords match");
        }
    }

    /***
     * Check emails entered match
     * Reset re-entered password state
     * @param {input}
     */
    const checkEmail = (input) => {
        setConfirmEmail(input)
        if (input !== confirmEmail) {
            setConfirmEmailMessage("Email addresses do not match!");
            return;
        } else {
            setEmailMessage("Match!");

        }
    }

    /**
     * Handles the form submission for user registration.
     *
     * - Prevents the default form submission 
     * - Checks if the password and the confirmation password match. If they don't, it sets an error message and exits the funtion
     * - If the passwords match, the submit button is enabled.
     * - Checks if the password and the confirmation password match. If they don't, it sets an error message and exits the funtion
     * - Sends a POST request to the registration endpoint with the user's details
     * - Handles the response from the server
     * 
     * @param {Event} e - the form submit event.
     */
    const handleSubmit = async (e) => {
        e.preventDefault(); // prevents the default form submition

        checkPassword(); // check if the password and re-entered password match
        checkEmail()     // check if the email address and confrim email address match

        try {

            //  POST request to the registration endpoint with the user's details
            const response = await axios.post('/auth/register', { name, lastName, email, password });

            // check if response exists
            if (response.status === 201 && response.data) {
                // set the message state with the response data
                setMessage(response.data);
                // sedirect to home page after successful registration
                navigate('/verify_account')
            } else {
                setMessage("Unexpected error occurred. Please try again.");
            }

        } catch (error) {

            // check if error response exists
            if (error.response && error.response.data) {
                // set the message state with the error response data
                setMessage(error.response.data);
            } else {
                // Set error message
                setMessage("Something went wrong. Please try again.");
            }
        }


    }

    return (
        <Container id="register_form_wrapper">
            <Row >
                <Heading tagName="h3" id="reg_heading" text="SignUp" />

            </Row>
            <Container id="register_form_container">

                <Row id="register_form_holder">
                    <Row >{message && <p>{message}</p>}</Row>
                    <Form onSubmit={handleSubmit} id="reg_form">

                        <Row id="reg_user_details_holder" className='reg_form_details_holder'>
                            <Col>
                                {/******** { user name } ********/}
                                <Form.Group controlId="reg_Name">
                                    <Form.Label>Name</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="breed"
                                        value={name}
                                        onChange={(e) => setName(e.target.value)}
                                        required
                                    />
                                </Form.Group>
                            </Col>
                            <Col>
                                {/******** { user last name } ********/}
                                <Form.Group controlId="reg_LastName">
                                    <Form.Label>Last name</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="lastName"
                                        value={lastName}
                                        onChange={(e) => setLastName(e.target.value)}
                                        required
                                    />
                                </Form.Group>
                            </Col>

                        </Row>

                        <Row id="reg_email_holder" className='reg_form_details_holder'>
                            <Col>
                                {/******** { email address } ********/}
                                <Form.Group controlId="reg_email">
                                    <Form.Label>Email Address</Form.Label>
                                    <Form.Control
                                        type="email"
                                        name="email"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                        required
                                    />
                                </Form.Group>
                                {emailMessage && <p>{emailMessage}</p>}

                            </Col>
                            <Col>
                                {/******** { email address confirmation } ********/}
                                <Form.Group controlId="reg_confirmEmail">
                                    <Form.Label>Confirm Email Address</Form.Label>
                                    <Form.Control
                                        type="email"
                                        name="confirmEmail"
                                        value={confirmEmail}
                                        onChange={(e) => setConfirmEmail(e.target.value)}
                                        required
                                    />
                                </Form.Group>
                                {confirmEmailMessage && <p>{confirmEmailMessage}</p>}

                            </Col>

                        </Row>
                        <Row id="reg_password_holder" className='reg_form_details_holder'>
                            <Col>
                                {/******** { password } ********/}
                                <Form.Group controlId="reg_password">
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control
                                        type="password"
                                        name="password"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        required
                                    />
                                </Form.Group>
                                {password && <p>{passwordMessage}</p>}

                            </Col>
                            <Col>
                                {/******** { password} ********/}
                                <Form.Group controlId="reg_confirmPassword">
                                    <Form.Label>Confirm Password</Form.Label>
                                    <Form.Control
                                        type="password"
                                        name="confirmPassword"
                                        value={confirmPassword}
                                        onChange={(e) => setConfirmPassword(e.target.value)}
                                        required

                                    />
                                </Form.Group>
                                {confirmPasswordMessage && <p>{confirmPasswordMessage}</p>}

                            </Col>

                        </Row>
                        <Row >

                            <button type="submit" id="reg_submit_button" className="btn btn-primary" >Go!</button>

                        </Row>
                    </Form>
                </Row>

            </Container>
        </Container>
    )
};

export default Register;