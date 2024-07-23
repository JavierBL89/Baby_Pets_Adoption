import React, { useContext, useState } from "react";
import { Container, Form, Button, Row, Col } from 'react-bootstrap';
import Heading from "../../../common/Heading";
import ButtonComponent from "../../../common/ButtonComponent";
import instance from "../../../../scripts/axiosConfig";
import { useParams, useNavigate } from "react-router-dom";
import TextComponent from "../../../common/TextComponent";
import { FeedbackContext } from "../../../../context/FeedBackContext";




/** 
* The above code is a React functional component named `FormComponent` that represents a form for
* submitting pet information. Here is a summary of what the code is doing: 

*/
const FormComponent = () => {

    const [message, setMessage] = useState("");   // message state
    const [isForSale, setIsForSale] = useState(false);   // message state
    const { token } = useParams();      // grab token from url for user authentication

    const { setPostActionMessage } = useContext(FeedbackContext);  // get global message from FeedbackContext

    const navigate = useNavigate();

    // form data state
    const [formData, setFormData] = useState({
        petCategory: "",
        breed: "",
        location: "",
        birthMonth: "",
        birthYear: "",
        providerId: "",
        motherBreed: "",
        motherImg: "",
        fatherBreed: "",
        fatherImg: "",
        price: "0.0",
        comment: "",
        token: ""   // current session token neede for authentication
    });

    /**
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
     * 
     */
    const handleMotherImage = (e) => {
        setFormData({
            ...formData,
            motherImg: e.target.files[0],
        });
    };

    /***
   * 
   */
    const handleFatherImage = (e) => {
        setFormData({
            ...formData,
            fatherImg: e.target.files[0],
        });
    };

    /***
     * 
     * 
     * @param {*} e - the event
     */
    const handleSubmit = async (e) => {
        e.preventDefault();

        const formDataToSend = new FormData();
        formDataToSend.append('email', formData.email);
        formDataToSend.append('message', formData.message);
        formDataToSend.append('file', formData.file);

        formDataToSend.append('category', formData.petCategory);
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
        formDataToSend.append('token', token.trim());    // current session token neede for authentication


        if (token) {

            try {
                console.log(formData.price);

                const response = await instance.post('/pets/list_pet', formDataToSend, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                    }
                });

                if (response.status === 200) {
                    setPostActionMessage("Pet listing sucessfully added. From now on users can see it when navigating through the pet category.")
                    navigate(`/my_listings/${token}`);

                } else {
                    console.error("Form submission failed:", response.data);
                    setMessage("Form could not be submited. A server error occured. Please try again or contact admin to inform about the problem. ")
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

                {/********************************* Pet Details Section **************************/}

                <Container className="parents_info_wraper">
                    <Row>{/******** { Section Heading  } ********/}
                        <Heading tagName="h6" text="Pet List Information" className="form_parents_info_title" />
                    </Row>
                    <Container className="parents_info_holder">

                        <Row >
                            {/******** { pet category } ********/}
                            <Col id="pet_category_holder" xs={4}>
                                <Row id="pet_category_row">
                                    <Col xs={6}>
                                        <Form.Group controlId="formPetCategoryCat">
                                            <Form.Label>Cat</Form.Label>
                                            <Form.Check
                                                type="radio"
                                                name="petCategory"
                                                checked={formData.petCategory === "cat"}
                                                value={"cat"}
                                                onChange={handleChange}
                                            />
                                        </Form.Group>
                                    </Col>
                                    <Col xs={6}>
                                        <Form.Group controlId="formPetCategoryDog">
                                            <Form.Label>Dog</Form.Label>
                                            <Form.Check
                                                type="radio"
                                                name="petCategory"
                                                checked={formData.petCategory === "dog"}
                                                value={"dog"}
                                                onChange={handleChange}
                                            />
                                        </Form.Group>
                                    </Col>
                                </Row>
                            </Col>

                            <Col xs={8}>
                                {/******** { pet's breeed } ********/}
                                <Form.Group controlId="formBreed">
                                    <Form.Label>Pet's Breed</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="breed"
                                        value={formData.breed}
                                        onChange={handleChange}
                                        placeholder="Will be set as 'Unkwon' if empty"
                                        required
                                    />
                                </Form.Group>
                            </Col>
                        </Row>
                        <Row>
                            <Col xs={12} lg={6} id="pet_birth_date_holder">
                                {/****************************  pet's birth date  **********************/}
                                <Row  >
                                    <Col xs={6}>
                                        {/******** { pet's birth month } ********/}
                                        <Form.Group controlId="formBirthMonth">
                                            <Form.Label>Birth Month</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="birthMonth"
                                                value={formData.birthMonth}
                                                onChange={handleChange}
                                                placeholder="MM"
                                                required
                                            />
                                        </Form.Group>
                                    </Col>

                                    <Col xs={6}>
                                        {/******** { pet's birth year } ********/}
                                        <Form.Group controlId="formBirthYear">
                                            <Form.Label>Birth Year</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="birthYear"
                                                value={formData.birthYear}
                                                onChange={handleChange}
                                                placeholder="YYYY"
                                                required
                                            />
                                        </Form.Group>
                                    </Col>
                                </Row>
                                {/*********************************/}
                            </Col>

                            <Col xs={12} lg={6} className="mt-0">
                                <Row >
                                    <Col className="mt-0 ps-0">
                                        {/******** { pet's location } ********/}
                                        <Form.Group controlId="formLocation">
                                            <Form.Label>location</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="location"
                                                value={formData.location}
                                                onChange={handleChange}
                                                placeholder="Galway"
                                                required
                                            />
                                        </Form.Group>
                                    </Col>
                                </Row>
                            </Col>
                        </Row>
                        <Row >
                            {/******** { Offer Type }********/}
                            <Col xs={8} md={6}>
                                <Row id="pet_offerType_row">
                                    <Col xs={6}>
                                        <Form.Group controlId="formOfferTypeAdoption">
                                            <Form.Label>For Adoption</Form.Label>
                                            <Form.Check
                                                type="radio"
                                                name="offerType"
                                                value={"adoption"}
                                                onClick={() => { setIsForSale(false) }}
                                            />
                                        </Form.Group>
                                    </Col>
                                    <Col xs={6}>
                                        <Form.Group controlId="formOfferTypeSale">
                                            <Form.Label>For Sale</Form.Label>
                                            <Form.Check
                                                type="radio"
                                                name="offerType"
                                                value={"sale"}
                                                onClick={() => setIsForSale(true)}
                                            />
                                        </Form.Group>
                                    </Col>
                                </Row>
                            </Col>
                            {/* conditional rendering */}
                            {isForSale &&
                                <Col xs={4} md={6}>
                                    <Row >
                                        <Col className="mt-0">
                                            {/******** { pet's price } ********/}
                                            <Form.Group controlId="formPrice">
                                                <Form.Label>Price €</Form.Label>
                                                <Form.Control
                                                    type="text"
                                                    name="price"
                                                    value={formData.price}
                                                    onChange={handleChange}
                                                    placeholder="E.g: 0.00"
                                                    required
                                                />
                                            </Form.Group>
                                        </Col>

                                    </Row>
                                </Col>
                            }
                        </Row>

                        {/******** {  comment  } ********/}
                        <Form.Group controlId="formComment" id="form_textarea">
                            <Form.Label>Any further information for potential adopters</Form.Label>
                            <Form.Control
                                as="textarea"
                                name="fatherBreed"
                                value={formData.fatherBreed}
                                onChange={handleChange}

                            />
                        </Form.Group>
                    </Container>

                </Container>

                {/********************************** Pet Parent's Section **********************/}

                <Container className="parents_info_wraper">
                    <Row>{/******** { Section Heading  } ********/}
                        <Heading tagName="h6" text="Pet Mother Info" className="form_parents_info_title" />
                    </Row>
                    <Container className="parents_info_holder">
                        <Row id="form_mother_information">
                            {/******** { pet mother's breed } ********/}
                            <Form.Group controlId="formMotherBreed">
                                <Form.Label>Pet mother's breed</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="motherBreed"
                                    value={formData.motherBreed}
                                    onChange={handleChange}
                                    placeholder="Will be set as 'Unkwon' if empty"
                                />
                            </Form.Group>

                            {/******** { pet mother's Image } ********/}
                            <Form.Group controlId="formMotherImage">
                                <Form.Label>Pet mother's picture</Form.Label>
                                <Form.Control
                                    type="file"
                                    name="motherImage"
                                    onChange={handleMotherImage}
                                />
                            </Form.Group>
                        </Row>
                    </Container>

                </Container>


                <Container className="parents_info_wraper">
                    <Row>
                        <Heading tagName="h6" text="Pet Father Info" className="form_parents_info_title" />
                    </Row>
                    <Container className="parents_info_holder">

                        {/******** { pet father's breed } ********/}
                        <Form.Group controlId="formFatherBreed">
                            <Form.Label>Pet father's breed</Form.Label>
                            <Form.Control
                                type="text"
                                name="fatherBreed"
                                value={formData.fatherBreed}
                                onChange={handleChange}
                                placeholder="Will be set as 'Unkwon' if empty"

                            />
                        </Form.Group>

                        {/******** { pet father's Image } ********/}
                        <Form.Group controlId="formFatherImage">
                            <Form.Label>Pet father's picture</Form.Label>
                            <Form.Control
                                type="file"
                                name="fatherImage"
                                onChange={handleFatherImage}
                            />
                        </Form.Group>
                    </Container>

                </Container>

                <Row>
                    <button id="list_pet_submit_button" className="btn btn-primary" type="submit">Submit</button>

                </Row>

            </Form>
            {/******** Feedback Message ********/}
            <Row id="create_pet_failed_message_holder">
                {/* {message && */}
                <TextComponent id="create_pet_failed_message" text={message} />
                {/* } */}
            </Row>        </Container>
    )

};

export default FormComponent;