import React, { useState, useEffect, useContext, useCallback } from "react";
import { Container, Row, Col } from "react-bootstrap";
import ButtonComponent from "../../../common/ButtonComponent";
import { useNavigate } from "react-router-dom";
import { DataPetContext } from '../../../../context/DataPetContext';
import useFetchById from "../../../hooks/data/fetchById";


/** 
 *  Component represents a card to diplay pets using some information organized visaully appealing 
 * 
 * @param {string} img - pet's image
 * @param {string} breed - pet's breed type
 * */
const PetCard = ({ img, breed, petId, onView }) => {


    // isLoaded flag is used to ensure that the pet image is only displayed when fully loaded
    const [isLoaded, setIsLoaded] = useState(false);
    const navigate = useNavigate();

    const { currentPetCategory } = useContext(DataPetContext);

    /***
     * Manages the state of isLoaded variable
     * 
     */
    const handleImageLoad = () => {
        setIsLoaded(true);
    }


    /** 
    * The `useEffect` hook is being used to preload an image into an Image obejct before
    * rendering it in the component. 
    * 
    * In this way we can tap into the object attributes and perform operations 
    * such as check when the image if fully loaded into the new object.
    * This is a technique to ensure all images are loaded and rendered at the same type, improving user experience.
    * 
    * */
    useEffect(() => {

        // preload image
        const imgeELement = new Image();

        imgeELement.src = img;   // set the src of the 'Image' obejct with the pet image(img) src string
        imgeELement.onload = handleImageLoad;  // check when is fully loaded and handle iit
    });

    /***
     * 
     */
    const handleView = (petId) => {
        navigate(`/pets/${currentPetCategory}/view/${petId}`);
    };

    return (

        <Container className="m-auto pet_card_wrapper" id="" >
            <Container className="m-auto p-0" id="pet_card_container">
                {isLoaded &&
                    <>
                        <Container className="img_holder p-0">
                            <img src={img} alt={breed} />
                        </Container>
                        <Row className="p-0">
                            <Col className="">
                                <p>{breed}</p>
                            </Col>
                        </Row>
                        <Row className="">
                            <Col className="text-center">
                                <ButtonComponent onClick={() => handleView(petId)} text="View this beauty" />
                            </Col>

                        </Row>

                    </>

                }

            </Container>
        </Container>
    )
};

export default React.memo(PetCard);