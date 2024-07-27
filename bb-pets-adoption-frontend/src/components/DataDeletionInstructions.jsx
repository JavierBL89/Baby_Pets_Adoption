import React from "react";
import Heading from "./common/Heading";
import { Container } from "react-bootstrap";
import TextComponent from "./common/TextComponent";

/**
 *Component host the content for page /Data Deletion
 */
const DataDeletionInstructions = () => {


    return (
        <Container id="dataDeletion_wrapper">
            <Container id="dataDeletion_container">
                <Heading tagName="h4" id="data_deletion__heading" className="" text="Data Deletion Instructions" />
                <TextComponent text="If you want to delete your data from our app, please follow these steps:" />
                <ol>
                    <li>
                        Send an email to <a href="mailto:your-email@example.com"> your-email@example.com</a> with the subject line "Data Deletion Request".
                    </li>
                    <li>Include your name and the email address associated with your account in the body of the email.</li>
                    <li>Specify that you want to delete all your data from our app.</li>
                </ol>
                <TextComponent text="We will process your request within 30 days and confirm the deletion via email." />
            </Container>
        </Container>
    )
};

export default DataDeletionInstructions;