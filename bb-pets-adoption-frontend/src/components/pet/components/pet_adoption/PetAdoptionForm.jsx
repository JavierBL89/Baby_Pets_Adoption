import React, { useEffect, useState, useContext } from "react";
import { Container, Form, Button, Row, Col } from 'react-bootstrap';
import Heading from "../../../common/Heading";
import instance from "../../../../scripts/axiosConfig";
import { useNavigate } from "react-router-dom";
import TextComponent from "../../../common/TextComponent";
import ButtonComponent from "../../../common/ButtonComponent";
import { DataPetContext } from "../../../../context/DataPetContext";




/**
 * It is a form component that allows users
 * to submit an adoption application for a pet.
 * 
 * The form includes fields for the user's name, location,
 * and additional comments. 
 * 
 * It also handles the form submission by making a POST request,
 *  and handling returned http responses adn feedback
 * 
 * @returns  `PetAdoptionForm` component 
 */
const PetAdoptionForm = ({ petId }) => {

    const { currentPetCategory } = useContext(DataPetContext);


    const [message, setMessage] = useState("");   // message state
    const [successMessage, setSuccessMessage] = useState(false);   // successMessage state
    const [failedMessage, setFailedMessage] = useState(false);   // failedMessage state

    const [override, setOverride] = useState(false)
    const [isDuplicate, setIsDuplicate] = useState(false);
    var token = localStorage.getItem('token');  // grab current token from locat storage

    const navigate = useNavigate();

    // form data state
    const [formData, setFormData] = useState({
        name: '',
        location: '',
        comment: '',
        token: '',   // current session token neede for authentication
        petId: '',   // petId needed on API for database operations
        petCategory: '',   // petId needed on API for database operations
    });


    // set formdata  each render
    useEffect(() => {
        setFormData(prevFormData => ({
            ...prevFormData,
            token: token,
            petId: petId,
            petCategory: currentPetCategory,
        }));
    }, [token, petId, currentPetCategory]);



    /**
     * Method handles changes on inputs and set formdata values accordingly
     * 
     * @param {*} e - the event
     */
    const handleChange = (e) => {

        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };


    /**
     * Method handles previous application override
     * If the user decides to override, set override to true, 
     * hide the duplicate message, and resubmit the form.
     */
    const handleOverride = () => {
        setOverride(true);
        setIsDuplicate(false);
        handleSubmit(new Event('submit', { bubbles: true }));
    };



    /***
     * Method handles form submission.
     * 
     * Creates a Formdata object to then append form  key-value pairs
     * 
     * @param {*} e - the event
     */
    const handleSubmit = async (e) => {
        e.preventDefault();

        console.log(currentPetCategory);
        const formDataToSend = new FormData();
        formDataToSend.append('location', formData.location);
        formDataToSend.append('comment', formData.comment);
        formDataToSend.append('token', token.trim());
        formDataToSend.append('petId', petId);
        formDataToSend.append('petCategory', formData.petCategory);


        if (token) {

            setSuccessMessage(false);
            setFailedMessage(false);

            try {

                // POST request with headers set to accept and handle multipart files on server side
                const response = await instance.post('/adoption/apply', formDataToSend, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                });

                if (response.status === 200) {
                    setMessage("Form successfuly submitted!" +
                        "\n You should see a new application your Applications section.");
                    setSuccessMessage(true);
                    setTimeout(() => {
                        navigate(`/pets/${currentPetCategory}/view/${petId}`);
                    }, 1000)


                }
                else {
                    console.error("Form submission failed:", response.data);
                    setMessage("Form could not be submited. A server error occured. Please try again or contact admin to inform about the problem. ")
                    setFailedMessage(true)
                }
            } catch (error) {

                if (error.response && error.response.status === 409) {
                    setFailedMessage(true);
                    setMessage("You already applied for this pet. Please go to 'My Appications'")
                    setIsDuplicate(true);
                } else {
                    console.error('Error submitting form:', error.message);
                }
            }
        } else {
            setMessage("Authentication needed. Form could not be submited!")
            setFailedMessage(true);
        }

    };

    return (
        <Container id="adoption_application_wrapper">
            <Form onSubmit={handleSubmit} id="adoption_application_form">
                <Container className="adoption_application_wrapper">

                    {/******** Info Section ******/}

                    <Row>{/************ text ********/}
                        <TextComponent
                            tagName="h6"
                            text="We will provide your name associated to your account 
                        for the reciever. It will be visisble as..."
                            className="form_update_info_title"
                        />
                    </Row>


                    <Row id="adoption_form_email">

                        <Row >
                            {/***************** {  location } ********/}
                            <Form.Label>County of your current location</Form.Label>
                            <Form.Group controlId="formLocation">
                                <Form.Control
                                    type="text"
                                    name="location"
                                    value={formData.location}
                                    onChange={handleChange}
                                    placeholder="Mayo"
                                    required
                                />
                            </Form.Group>
                        </Row>
                    </Row>

                    {/******************* {  comment } ***********/}
                    <Form.Group controlId="formComment" id="form_textarea">
                        <Form.Label>Some other details about you to make you stand out!</Form.Label>
                        <Form.Control
                            as="textarea"
                            name="comment"
                            value={formData.comment}
                            onChange={handleChange}
                            placeholder=""
                        />
                    </Form.Group>
                </Container>
                {/******** Feedback Message ********/}
                <Row >
                    {message &&
                        <>
                            {successMessage && <TextComponent id="adoption_form_success_text" text={message} />}
                            {failedMessage && <TextComponent id="adoption_form_failed_text" text={message} />}
                        </>
                    }
                </Row>

                {/******** Submit Button ********/}
                <Row>
                    <button id="adoption_application_submit_button" className="btn btn-primary" type="submit">submit</button>
                </Row>

            </Form>

        </Container>
    )

};

export default PetAdoptionForm;