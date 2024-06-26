import React from "react";
import { Container } from "react-bootstrap";
import Heading from "../../common/Heading";



const VerifyAccount = () => {
    return (
        <Container id="very_account_wrapper">
            <Container id="very_account_container">
                <Heading type="h2" id="" text="Account verification Needed" />
                <Heading type="h3" id="" text="Please check inbox of the email address account entered" />
            </Container>
        </Container>
    );
};


export default VerifyAccount;