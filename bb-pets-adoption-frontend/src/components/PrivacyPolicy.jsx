import React from "react";
import Heading from "./common/Heading";
import TextComponent from "./common/TextComponent";
import { Container } from "react-bootstrap";

/**
*
 */
const PrivacyPolicy = () => {


    return (
        <Container id="privacy_policy_wrapper">
            <Container id="privacy_policy_container">
                <Heading tagName="h4" id="privacy_policy__heading" text="Privacy Policy" />
                <TextComponent text="Your privacy is important to us. It is [Your Company Name]'s policy to respect your privacy regarding any information we may collect from you across our website, [Website URL], and other sites we own and operate." />

                <Heading tagName="h4" text="Information We Collect" />
                <TextComponent text="We only ask for personal information when we truly need it to provide a service to you. We collect it by fair and lawful means, with your knowledge and consent. We also let you know why we’re collecting it and how it will be used." />

                <Heading tagName="h4" text="How We Use Your Information" />
                <TextComponent text="We use the information we collect only to provide our services to you. We do not share any personally identifying information publicly or with third parties, except when required to by law." />

                <Heading tagName="h4" text="Security" />
                <TextComponent text="We protect your personal information within commercially acceptable means to prevent loss and theft, as well as unauthorized access, disclosure, copying, use, or modification." />

                <Heading tagName="h4" text="Your Rights" />
                <TextComponent text="You are free to refuse our request for your personal information, with the understanding that we may be unable to provide you with some of your desired services." />

                <Heading tagName="h4" text="Contact Us" />
                <p>If you have any questions about how we handle user data and personal information, feel free to <a href="/contact" title="Link to contact page" >contact us. </a></p>

            </Container>
        </Container>
    )
};

export default PrivacyPolicy;