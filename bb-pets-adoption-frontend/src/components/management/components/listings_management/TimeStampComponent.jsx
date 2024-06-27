import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import Heading from "../../../common/Heading";
import TextComponent from "../../../common/TextComponent";


/***
 * 
 * 
 */
const TimeStampComponent = ({ id, className, text }) => {


    return (

        <Container id={`timeStamp_wrapper_${id}`} className={className}>
            <Container id={`timeStamp_container_${id}`}>
                <Row >
                    <TextComponent id={`timeStamp_text_${id}`} text={text} />
                </Row>

            </Container>

        </Container>
    );

};


export default TimeStampComponent;