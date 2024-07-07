import React, { useEffect, useState, useContext } from "react";
import { Container, Form, Button, Row, Col } from 'react-bootstrap';
import Heading from "../../../common/Heading";
import instance from "../../../../scripts/axiosConfig";
import { useNavigate } from "react-router-dom";
import TextComponent from "../../../common/TextComponent";
import ButtonComponent from "../../../common/ButtonComponent";
import { DataPetContext } from "../../../../context/DataPetContext";




/**

 * @returns  `PetAdoptionForm` component 
 */
const ViewApplicationComponent = ({ application, status, comments, applicationId, onFetchData, token }) => {

    const { currentPetCategory } = useContext(DataPetContext);


    const [message, setMessage] = useState("");   // message state
    const [successMessage, setSuccessMessage] = useState(false);   // successMessage state
    const [failedMessage, setFailedMessage] = useState(false);   // failedMessage state


    // form data state
    const [formData, setFormData] = useState({
        status: '',
        token: '',   // current session token neede for authentication
        petId: '',   // petId needed on API for database operations
        petCategory: '',   // petId needed on API for database operations
    });


    // set formdata  each render
    useEffect(() => {
        setFormData(prevFormData => ({
            ...prevFormData,
            status: status,
            token: token,
            applicationId: applicationId,
        }));
        console.log(applicationId);
    }, [token, status, applicationId]);



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
        console.log(e.target);
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

        const formDataToSend = new FormData();
        formDataToSend.append('token', token.trim());
        formDataToSend.append('applicationId', applicationId.toString());
        formDataToSend.append('status', formData.status);


        if (token) {

            setSuccessMessage(false);
            setFailedMessage(false);

            try {

                // POST request with headers set to accept and handle multipart files on server side
                const response = await instance.put('/adoption/pet/applications/updateStatus', formDataToSend, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                });

                if (response.status === 200) {
                    setMessage("Application status successfully updated!" +
                        "\n You should see a the application moved to on the status tab.");
                    setSuccessMessage(true);
                    setTimeout(() => {
                        onFetchData();
                    }, 2000)
                }
                else {
                    console.error("Form submission failed:", response.data);
                    setMessage("Update could not be submited. A server error occured. Please try again or contact admin to inform about the problem. ")
                    setFailedMessage(true)
                }
            } catch (error) {

                if (error.response && error.response.status === 409) {
                    setFailedMessage(true);
                } else {
                    console.error('Error submitting form:', error.message);
                }
            }
        } else {
            setMessage("Authentication needed. Update could not be submited!")
            setFailedMessage(true);
        }

    };

    return (

        <Container id="view_application_wrapper" >
            <Form onSubmit={handleSubmit} id="update_application_form">
                <Container className="view_application_wrapper">
                    {/********** Comments ******** */}
                    <Row id="view_application_comment_wrapper">
                        <Row className="m-0" id="view_application_comment_container">
                            <Row id="pet_details_comments_holder">
                                <TextComponent id="pet_comments_text"
                                    text={
                                        comments ? comments : "The applicant did not leave any comment"} />
                            </Row>
                        </Row>
                    </Row>

                    <Row >
                        <Heading id="update_status_heading" tagName="h6" text="Update Status" />
                    </Row>
                    <Row id="app_update_status_holder">
                        {/******** Update status ********/}
                        <Row id="app_update_status_row">
                            <Col xs={3} >
                                <Form.Group controlId="formStatusPending">
                                    <Form.Label>Pending</Form.Label>
                                    <Form.Check
                                        type="radio"
                                        name="status"
                                        checked={formData.status === "Pending"}
                                        value="Pending"
                                        onChange={handleChange}
                                    />
                                </Form.Group>
                            </Col>
                            <Col xs={3}>
                                <Form.Group controlId="formStatusView">
                                    <Form.Label>Viewed</Form.Label>
                                    <Form.Check
                                        type="radio"
                                        name="status"
                                        checked={formData.status === "Viewed"}
                                        value="Viewed"
                                        onChange={handleChange}
                                    />
                                </Form.Group>
                            </Col>
                            <Col xs={3}>
                                <Form.Group controlId="formStatusPendingAccepted">
                                    <Form.Label>Accept</Form.Label>
                                    <Form.Check
                                        type="radio"
                                        name="status"
                                        checked={formData.status === "Accepted"}
                                        value="Accepted"
                                        onChange={handleChange}
                                    />
                                </Form.Group>
                            </Col>
                            <Col xs={3}>
                                <Form.Group controlId="formStatusViewSelected">
                                    <Form.Label>Select</Form.Label>
                                    <Form.Check
                                        type="radio"
                                        name="status"
                                        checked={formData.status === "Selected"}
                                        value="Selected"
                                        onChange={handleChange}
                                    />
                                </Form.Group>
                            </Col>
                        </Row>

                    </Row>

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
                    <button id="view_app_update_status_submit_button" className="btn btn-primary" type="submit">Update</button>
                </Row>

            </Form>

        </Container>
    )

};

export default ViewApplicationComponent;