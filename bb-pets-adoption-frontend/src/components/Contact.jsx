import React from "react";
import Heading from "./common/Heading";
import { Container, Row } from "react-bootstrap";
import TextComponent from "./common/TextComponent";

/**
 *Component host the content for page /Data Deletion
 */
const Contact = () => {


    return (
        <Container id="contact_wrapper">
            <Container id="contact_container">
                <Heading tagName="h1" id="contact_heading" text="Contact Us" />
                <TextComponent text="We’re here to help! Whether you have questions about adopting a pet, need support with our services, or want to give us feedback, you can reach out to us using the following methods." />
                <Heading tagName="h3" id="getIn_touch_heading" text="Get in Touch" />
                <Row>

                    <Heading tagName="h5" id="getIn_touch_heading" text="Email Us" />
                    <ul>
                        <li>
                            For general inquiries: <a href="mailto:your-email@example.com">info@yourservice.com</a>
                        </li>
                        <li>For support: <a href="mailto:your-email@example.com">support@yourservice.com</a>
                        </li>
                        <li>For feedback: <a href="mailto:your-email@example.com">feedback@yourservice.com</a>
                        </li>
                    </ul>
                </Row>
                <Row>

                    <Heading tagName="h5" id="call_us_heading" text="Call Us:" />
                    <ul>
                        <li>
                            General inquiries: +1 (123) 456-7890
                        </li>
                        <li>Support hotline: +1 (123) 456-7891
                        </li>
                    </ul>
                </Row>

                <Row>

                    <Heading tagName="h5" id="visit_us_heading" text="Visit Us:" />
                    <TextComponent text="[Your Service Name]" />
                    <TextComponent text="123 Pet Adoption Lane" />
                    <TextComponent text="Animal Town, PA 12345" />
                </Row>

                <TextComponent text="We will process your request within 30 days and confirm the deletion via email." />
            </Container>
        </Container>
    )
};

export default Contact;