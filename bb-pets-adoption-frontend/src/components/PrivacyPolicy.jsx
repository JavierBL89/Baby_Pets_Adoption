import React from "react";
import Heading from "./common/Heading";

/**
*
 */
const PrivacyPolicy = () => {


    return (
        <div>
            <Heading tagName="h4" id="privacy_policy__heading" className="" text="Privacy Policy" />
            <p>Your privacy is important to us. It is [Your Company Name]'s policy to respect your privacy regarding any information we may collect from you across our website, [Website URL], and other sites we own and operate.</p>
            <div>

                <Heading tagName="h4" id="" className="" text="Information We Collect" />
                <p>We only ask for personal information when we truly need it to provide a service to you. We collect it by fair and lawful means, with your knowledge and consent. We also let you know why we’re collecting it and how it will be used.</p>

                <Heading tagName="h4" id="" className="" text="How We Use Your Information" />
                <p>We use the information we collect only to provide our services to you. We do not share any personally identifying information publicly or with third parties, except when required to by law.</p>

                <Heading tagName="h4" id="" className="" text="Security" />
                <p>We protect your personal information within commercially acceptable means to prevent loss and theft, as well as unauthorized access, disclosure, copying, use, or modification.</p>

                <Heading tagName="h4" id="" className="" text="Your Rights" />
                <p>You are free to refuse our request for your personal information, with the understanding that we may be unable to provide you with some of your desired services.</p>

                <Heading tagName="h4" id="" className="" text="Contact Us" />
                <p>If you have any questions about how we handle user data and personal information, feel free to contact us.</p>

            </div>
        </div>
    )
};

export default PrivacyPolicy;