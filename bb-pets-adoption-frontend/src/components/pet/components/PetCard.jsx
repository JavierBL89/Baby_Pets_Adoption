import React, { useState, useEffect } from "react";
import { Container, Row, Col } from "react-bootstrap";



/***
 * 
 */
const PetCard = ({ img, breed }) => {

    const [isLoaded, setIsLoaded] = useState(false);

    /***
     * 
     */
    const handleImageLoad = () => {

        setIsLoaded(true);
    }

    /***
     * 
     */
    useEffect(() => {

        // preload image
        const imgeELement = new Image();

        imgeELement.src = img;   // set the src of the 'Image' obejct with the pet image(img) src string
        imgeELement.onload = handleImageLoad;  // check when is fully loaded and handle iit
    });

    /**
     * 
     */
    return (

        <Container className="m-auto pet_card_wrapper" id="" >
            <Container className="m-auto p-0" id="pet_card_container">
                {isLoaded &&
                    <>
                        <Container className="img_holder p-0">
                            <img src={img} alt={breed} />
                        </Container>
                        <Row flex className="p-0">
                            <Col className="">
                                <p>{breed}</p>
                            </Col>
                        </Row>
                        <Row className="">
                            <Col className="text-center">
                                <a href="#" >See more</a>
                            </Col>

                        </Row>

                    </>

                }

            </Container>
        </Container>
    )
};

export default PetCard;