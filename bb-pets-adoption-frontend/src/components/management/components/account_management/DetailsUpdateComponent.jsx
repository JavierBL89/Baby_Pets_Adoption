import React, { useEffect, useState, useContext } from "react";
import { Container, Form, Button, Row, Col } from 'react-bootstrap';
import Heading from "../../../common/Heading";
import instance from "../../../../scripts/axiosConfig";
import { useNavigate } from "react-router-dom";
import TextComponent from "../../../common/TextComponent";
import ButtonComponent from "../../../common/ButtonComponent";
import { DataPetContext } from "../../../../context/DataPetContext";
import { FeedbackContext } from "../../../../context/FeedBackContext";




/**
 * It is a form component that allows users
 * to update the personal detalis suc as
 * name, last name location...
 * 
 * It also handles the form submission by making a PUT request,
 *  and handling returned http responses adn feedback
 * 
 * @returns  `DetailsUpdateComponent` component 
 */
const DetailsUpdateComponent = () => {

    const [postActionMessage, setPostActionMessage] = useState(FeedbackContext)

    const [message, setMessage] = useState(false);   // failedMessage state
    const [name, setName] = useState("");
    const [lastName, setLastName] = useState("");
    const [location, setLocation] = useState("");
    const navigate = useNavigate();
    var token = localStorage.getItem('token');  // grab current token from locat storage


    const [warningElement, setwarningElement] = useState(false);

    /**** 
     * Method toggles the view application accordion.
     * 
     * Sets the state to the oposite of the current state is
     * false to true | true to false
    */
    const viewToggle = () => {
        setwarningElement(!warningElement);
    }

    /***
     * Method handles form submission.
     * 
     * Creates a Formdata object to then append form  key-value pairs
     * 
     * @param {*} e - the event
     */
    const handleSubmit = async (e) => {
        e.preventDefault();


        const formDataToSend = new FormData();
        formDataToSend.append('token', token.trim());
        formDataToSend.append('name', name);
        formDataToSend.append('lastName', lastName);
        formDataToSend.append('location', location);

        if (token) {

            setMessage(false);

            try {

                // POST request with headers set to accept and handle multipart files on server side
                const response = await instance.put(`/account_management/update_details`, formDataToSend, {
                    headers: {
                        'Content-Type': 'multipart/form-data'  // alows to send data in a form structure
                    }
                });

                if (response.status === 200) {
                    setPostActionMessage("Your details have been successfylly updated!.");
                    localStorage.setItem('feedbackMessage', "Your details have been successfylly updated!");
                    window.location.reload();
                }
                else {
                    console.error("Form submission failed:", response.data);
                    setMessage("Form could not be submited. A server error occured. Please try again or contact admin to inform about the problem. ")
                    setMessage(true)
                }
            } catch (error) {
                console.error('Something went wrong! Error submitting form:', error.message);
            }
        } else {
            setMessage("Authentication needed. Form could not be submited!")
        }

    };

    /****
     * 
     */
    const handleUserProfileDelete = async () => {

        if (token) {

            setMessage(false);

            try {

                // POST request with headers set to accept and handle multipart files on server side
                const response = await instance.delete(`/account_management/delete_user?token=${token}`);

                if (response.status === 200) {
                    navigate("/")
                }
                else {
                    console.error("Form submission failed:", response.data);
                    setMessage("Form could not be submited. A server error occured. Please try again or contact admin to inform about the problem. ")
                    setMessage(true)
                }
            } catch (error) {
                console.error('Something went wrong! Error submitting form:', error.message);
            }
        } else {
            setMessage("Authentication needed. Form could not be submited!")
        }

    }


    return (
        <Container id="personal_details_update_wrapper">
            <Container className="personal_details_update_container">
                <Heading
                    tagName="h6"
                    id="personal_details_update_heading"
                    text="Personal details update"
                />
                <Form onSubmit={handleSubmit} id="personal_details_update_form">

                    <Row id="personal_details_update_holder">
                        {/******** { user name } ********/}
                        <Form.Group controlId="formUpdateName">
                            <Form.Label>Name</Form.Label>
                            <Form.Control
                                type="text"
                                name="breed"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                                required
                            />
                        </Form.Group>
                        {/******** { user last name } ********/}
                        <Form.Group controlId="formUpdateLastName">
                            <Form.Label>Last name</Form.Label>
                            <Form.Control
                                type="text"
                                name="lastName"
                                value={lastName}
                                onChange={(e) => setLastName(e.target.value)}
                                required
                            />
                        </Form.Group>
                        {/***************** { location  } ********/}
                        <Form.Label>Your currrent location (County)</Form.Label>
                        <Form.Group controlId="formUpdateLocation">
                            <Form.Control
                                type="text"
                                name="updateLocation"
                                value={location}
                                onChange={(e) => setLocation(e.target.value)}
                                required
                            />
                        </Form.Group>
                    </Row>


                    {/******** Feedback Message ********/}
                    <Row >
                        {message &&
                            <>
                                {message && <TextComponent id="email_update_form_failed_text" text={message} />}
                            </>
                        }
                    </Row>

                    {/******** Submit Button ********/}
                    <Row>
                        <button id="personal_details_update_submit_button" className="btn btn-primary" type="submit">Submit</button>
                    </Row>
                </Form>


                <Heading
                    tagName="h6"
                    id="delete_profile_heading"
                    text="Delete Profile"
                />
                <Row>
                    <TextComponent onClick={() => viewToggle()} id="delete_user_button" text="Delete profile" />
                    {warningElement ?
                        <Row className="delete_user_accordion_wrapper">
                            <TextComponent className="delete_user_warning_text" text="Are you sure you want to delete your profile and related data?, It will not be recoverable." />
                            <TextComponent onClick={() => handleUserProfileDelete(token)} text="Delete" className="btn delete_profile_confirm_button" />

                        </Row>
                        :
                        null
                    }
                </Row>
            </Container >

        </Container >
    )

};

export default DetailsUpdateComponent;