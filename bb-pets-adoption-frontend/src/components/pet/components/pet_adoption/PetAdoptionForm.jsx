import React, { useEffect, useState } from "react";
import { Container, Form, Button, Row, Col } from 'react-bootstrap';
import Heading from "../../../common/Heading";
import ButtonComponent from "../../../common/ButtonComponent";
import instance from "../../../../scripts/axiosConfig";
import { useParams, useNavigate } from "react-router-dom";
import TextComponent from "../../../common/TextComponent";
import ImageComponent from "../../../common/ImageComponent";




/** 
* The above code is a React functional component named `FormComponent` that represents a form for
* submitting pet information. Here is a summary of what the code is doing: 

*/
const PetAdoptionForm = ({ petId }) => {

    const [message, setMessage] = useState("");   // message state
    const [dataToSend, setDataToSend] = useState({})
    var token = localStorage.getItem('token');  // grab current token from locat storage

    const navigate = useNavigate();

    // form data state
    const [formData, setFormData] = useState({
        name: '',
        location: '',
        comment: '',
        token: '',   // current session token neede for authentication
        petId: '',   // petId needed on API for database operations
    });

    // set formdata when comoponent is rendered
    useEffect(() => {
        setFormData(prevFormData => ({
            ...prevFormData,
            token: token,
            petId: petId
        }));
    }, [token, petId]);



    /**
     * Method handles changes on inputs and set formdat values accordingly
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

    /***
     * Method handles form submission.
     * 
     * Creates a Formdata object to then append form  key-values
     * 
     * @param {*} e - the event
     */
    const handleSubmit = async (e) => {
        e.preventDefault();

        const formDataToSend = new FormData();
        formDataToSend.append('location', formData.location);
        formDataToSend.append('comment', formData.comment);
        formDataToSend.append('token', token.trim());
        formDataToSend.append('petId', petId);


        if (token) {

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
                    navigate(`/pet_view/${petId}`);
                } else {
                    console.error("Form submission failed:", response.data);
                    setMessage("Form could not be submited. A server error occured. Please try again or contact admin to inform about the problem. ")
                }
            } catch (error) {
                console.error('Error submitting form:', error);
            }

        } else {
            setMessage("Authentication needed. Form could not be submited!")
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


                {/******** Submit Button ********/}
                <Row>
                    <button id="adoption_application_submit_button" className="btn btn-primary" type="submit">submit</button>
                </Row>

                {/******** Feedback Message ********/}
                <Row >
                    {message &&
                        <TextComponent text={message} />
                    }
                </Row>
            </Form>

        </Container>
    )

};

export default PetAdoptionForm;