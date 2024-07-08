import React, { useContext, useEffect, useState } from "react";
import { Container, Form, Button, Row, Col } from 'react-bootstrap';
import Heading from "../../../common/Heading";
import ButtonComponent from "../../../common/ButtonComponent";
import instance from "../../../../scripts/axiosConfig";
import { useParams, useNavigate } from "react-router-dom";
import TextComponent from "../../../common/TextComponent";
import ImageComponent from "../../../common/ImageComponent";
import { FeedbackContext } from "../../../../context/FeedBackContext";




/** 
* The above code is a React functional component named `FormComponent` that represents a form for
* submitting pet information. Here is a summary of what the code is doing: 

*/
const PetUpdateForm = () => {

    const [message, setMessage] = useState("");   // message state
    const { petObjectString, petListingId, token } = useParams();      // grab token from url for user authentication
    const [currentPet, setCurrentPet] = useState({})
    const navigate = useNavigate();

    const { setPostActionMessage } = useContext(FeedbackContext);

    // form data state
    const [formData, setFormData] = useState({
        category: '',
        breed: '',
        location: '',
        birthMonth: '',
        birthYear: '',
        providerId: '',
        motherBreed: '',
        motherImg: '',
        fatherBreed: '',
        fatherImg: '',
        price: '',
        comment: '',
        token: '',   // current session token neede for authentication
        petId: '',   // petId needed on API for database operations
        petListingId: ''  // petListId needed on API for database operations
    });


    /* 
    * parse the pet parameter as JSON, and update the formData
    * this will ensure that the form data is properly updated with the current pet information
    * and when the user clicks 'update' only the field updated will change the data, 
    * the rest will be filled with previous pet data
    * */
    useEffect(() => {
        if (petObjectString) {
            try {
                // parse the listing object into a JSON object
                const currentPet = JSON.parse(decodeURIComponent(petObjectString));

                // set pet state with the current pet object
                setCurrentPet(currentPet);

                setFormData({
                    category: currentPet.category,
                    breed: currentPet.breed,
                    location: currentPet.location,
                    birthMonth: currentPet.birthDate[1],
                    birthYear: currentPet.birthDate[0],
                    providerId: currentPet.providerId,
                    motherBreed: currentPet.motherBreed,
                    motherImg: currentPet.motherImg,
                    fatherBreed: currentPet.fatherBreed,
                    fatherImg: currentPet.fatherImg,
                    price: currentPet.price,
                    comment: currentPet.comment,
                    token: token,        // current session token neede for authentication
                    petListingId: petListingId,    // pass the current petlisting id for db operations
                    petId: currentPet.id          // pass the current pet id for db operations
                });
            } catch (error) {
                setMessage("An error occured and operationg could not be done. Please try again later, or contact site administartor.")
                console.error('Error parsing pet data:', error);
            }
        }
    }, [petObjectString, token, petListingId]);


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

    /**
     * Method handles changes on mother's picture file
     * 
     * @param {*} e - the event
     */
    const handleMotherImage = (e) => {
        setFormData({
            ...formData,
            motherImg: e.target.files[0],
        });
    };

    /**
      * Method handles changes on father's picture file
      * 
      * @param {*} e - the event
      */
    const handleFatherImage = (e) => {
        setFormData({
            ...formData,
            fatherImg: e.target.files[0],
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
        formDataToSend.append('category', formData.category);
        formDataToSend.append('message', formData.message);
        formDataToSend.append('file', formData.file);
        formDataToSend.append('breed', formData.breed);
        formDataToSend.append('location', formData.location);
        formDataToSend.append('birthMonth', formData.birthMonth);
        formDataToSend.append('birthYear', formData.birthYear);
        formDataToSend.append('providerId', formData.providerId);
        formDataToSend.append('motherBreed', formData.motherBreed);
        formDataToSend.append('motherImg', formData.motherImg);
        formDataToSend.append('fatherBreed', formData.fatherBreed);
        formDataToSend.append('fatherImg', formData.fatherImg);
        formDataToSend.append('price', formData.price);
        formDataToSend.append('comment', formData.comment);
        formDataToSend.append('token', token.trim());
        formDataToSend.append('petId', formData.petId);
        formDataToSend.append('petListingId', formData.petListingId);


        if (token) {

            try {

                // POST request with headers set to accept and handle multipart files on server side
                const response = await instance.post('/pets/update_pet', formDataToSend, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                    }
                });

                if (response.status === 200) {
                    setPostActionMessage("Pet has been Updated!" +
                        "\n You should can review changes on the pet listing.");
                    navigate(`/my_listings/${token}`);
                } else {
                    console.error("Form submission failed:", response.data);
                    setMessage("Form could not be submited. A server error occured. Please try again or contact admin to inform about the problem. ")
                    console.log(message);
                }
            } catch (error) {
                setMessage("Error submitting form. Please check the form fields and ensure you enter valid data.")
                console.error('Error submitting form:', error);
            }

        } else {
            setMessage("Authentication needed. Form could not be submited!")
        }

    };

    return (
        <Container >
            <Form onSubmit={handleSubmit} id="list_pet_form">
                <Container className="update_pet_info_wraper">

                    {/******** Info Section ******/}

                    <Row>{/************ Heading } ********/}
                        <Heading tagName="h6" text="key Information" className="form_update_info_title" />
                    </Row>

                    <Container className="update_pet_info_holder">
                        <Row >
                            <Col >

                                {/****************************   Birth date  **********************/}
                                <Row id="pet_birth_date_holder">
                                    <Col xs={8} lg={4}>
                                        <Row ><small>Estimated Birth Date</small> </Row>
                                        <Row >
                                            <Col >
                                                {/******** { pet's birth month } ********/}
                                                <Form.Group controlId="formBirthMonth">

                                                    <Form.Control
                                                        type="text"
                                                        name="birthMonth"
                                                        value={formData.birthMonth}
                                                        onChange={handleChange}
                                                        placeholder={`${currentPet.birthMonth}`}
                                                        required
                                                    />
                                                    <Form.Label>Month</Form.Label>
                                                </Form.Group>
                                            </Col>

                                            <Col xs={6}>
                                                {/******** { pet's birth year } ********/}
                                                <Form.Group controlId="formBirthYear">
                                                    <Form.Control
                                                        type="text"
                                                        name="birthYear"
                                                        value={formData.birthYear}
                                                        onChange={handleChange}
                                                        placeholder={`${currentPet.birthYear}`}
                                                        required
                                                    />
                                                    <Form.Label>Year</Form.Label>

                                                </Form.Group>
                                            </Col>

                                        </Row>
                                    </Col>

                                    {/********************* { pet's price } ***********/}
                                    <Col xs={4} lg={4}>
                                        <Row >
                                            <Row><small>Price</small></Row>
                                            <Row  >
                                                <Form.Group controlId="formPrice">
                                                    <Form.Control
                                                        type="text"
                                                        name="price"
                                                        value={formData.price}
                                                        onChange={handleChange}
                                                        placeholder={`${currentPet.price}`}
                                                        required
                                                    />
                                                    <Form.Label>€</Form.Label>

                                                </Form.Group>
                                            </Row>
                                        </Row>
                                    </Col>

                                    {/***************** { pet's location } ********/}
                                    <Col xs={6} lg={4} >
                                        <Row >
                                            <Row><small>Location</small></Row>
                                            <Row  >
                                                <Form.Group controlId="formLocation">
                                                    <Form.Control
                                                        type="text"
                                                        name="location"
                                                        value={formData.location}
                                                        onChange={handleChange}
                                                        placeholder={`${currentPet.location}`}
                                                        required
                                                    />
                                                </Form.Group>
                                                <Form.Label>County</Form.Label>

                                            </Row>
                                        </Row>

                                    </Col>

                                </Row>
                            </Col>


                        </Row>
                        {/******************* {  comment } ***********/}
                        <Form.Group controlId="formComment" id="form_textarea">
                            <Form.Label>Any further information for potential adopters</Form.Label>
                            <Form.Control
                                as="textarea"
                                name="comment"
                                value={formData.comment}
                                onChange={handleChange}
                                placeholder={`${currentPet.comment}`}
                            />
                        </Form.Group>
                    </Container>
                </Container>



                {/********************** Mother Info Section *****************/}

                <Container className="update_pet_info_wraper">

                    <Row>{/************ Heading } ********/}
                        <Heading tagName="h6" text="Pet Mother Info" className="form_update_info_title" />
                    </Row>

                    <Container className="update_pet_info_holder">
                        <Row id="form_mother_information">

                            {/******** { Current Mother's Image } ********/}
                            <Col xs={12} lg={6} id="update_pet_current_motherImg_holder" className="update_pet_img_holder">
                                <Form.Group controlId="formMotherCurrentImg">
                                    <Form.Label>Current mother's picture</Form.Label>
                                    <ImageComponent src={`${currentPet.motherImg}`} />
                                </Form.Group>
                            </Col>

                            <Col xs={12} lg={6}>
                                {/******** {  mother's breed } ********/}
                                <Form.Group controlId="formMotherBreed">
                                    <Form.Label>Update mother's breed</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="motherBreed"
                                        value={formData.motherBreed}
                                        onChange={handleChange}
                                        placeholder={`${currentPet.motherBreed}`}
                                    />
                                </Form.Group>

                                {/****** { Update Mother's Image } ******/}
                                <Form.Group controlId="formMotherImage">
                                    <Form.Label>Upload new mother's picture</Form.Label>
                                    <Form.Control
                                        type="file"
                                        name="motherImg"
                                        onChange={handleMotherImage}

                                    />
                                </Form.Group>
                            </Col>

                        </Row>
                    </Container>
                </Container>


                {/************************** Fatther Info Section *********************/}

                <Container className="update_pet_info_wraper">

                    <Row>{/************ Heading } ********/}
                        <Heading tagName="h6" text="Pet Father Info" className="form_update_info_title" />
                    </Row>

                    <Container className="update_pet_info_holder">
                        <Row >
                            <Col xs={12} lg={6} className="update_pet_img_holder" >
                                {/******** Current father's Image } ********/}
                                <Form.Group controlId="formFatherCurrentImg">
                                    <Form.Label>Current father's picture</Form.Label>
                                    <ImageComponent src={currentPet.fatherImg ? `${currentPet.fatherImg}` : "https://baby-pets-adoption.s3.eu-west-1.amazonaws.com/2.jfif"} />
                                </Form.Group>
                            </Col>
                            <Col xs={12} lg={6}>
                                {/******** { Father's Breed } ********/}
                                <Form.Group controlId="formFatherBreed">
                                    <Form.Label>Update father's breed</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="fatherBreed"
                                        value={formData.fatherBreed}
                                        onChange={handleChange}
                                        placeholder={currentPet.fatherBreed ? `${currentPet.fatherBreed}` : "No previous data."}

                                    />
                                </Form.Group>

                                {/******** { Update father's Image } ********/}
                                <Form.Group controlId="formFatherImage">
                                    <Form.Label>Upload new father's picture</Form.Label>
                                    <Form.Control
                                        type="file"
                                        name="fatherImg"
                                        onChange={handleFatherImage}
                                    />
                                </Form.Group>
                            </Col>
                        </Row>


                    </Container>

                </Container>
                {/******** Submit Button ********/}
                <Row>
                    <button id="update_pet_submit_button" className="btn btn-primary" type="submit">submit</button>
                </Row>
            </Form>

            {/******** Feedback Message ********/}
            <Row id="update_pet_failed_message_holder">
                {message &&
                    <TextComponent id="update_pet_failed_message" text={message} />
                }
            </Row>
        </Container>
    )

};

export default PetUpdateForm;