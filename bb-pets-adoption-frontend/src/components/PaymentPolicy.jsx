import React from "react";
import Heading from "./common/Heading";
import InfoComponent from "./common/InfoComponent";
import { Container, Stack } from "react-bootstrap";
import TextComponent from "./common/TextComponent";


/**
 *Component host the content for page /Data Deletion
 */
const PaymentPolicy = () => {


    const dataSections = [

        {
            heading: "Adoption Listings",
            content: "Free Adoption Listings: Posting pets for free adoption on BbPets Adoption is completely free. Pet providers can post detailed information about the pets available for adoption at no cost.",
            section: [{
                heading: "",
                bulletPoints: []

            }]
        },
        {
            heading: "Sale Listings",
            content: "Fee for Sale Listings: If you wish to put pets up for sale, a fee of €20 per pet post will be charged. This fee helps maintain the quality of our listings and ensures that only serious offers are posted.",
            section: [{
                heading: "",
                bulletPoints: []

            }]
        },
        {
            heading: "Payment Process",
            content: "Subscription Requirement: You must be a subscribed user to post any pet listings. Ensure your subscription is active before posting.",
            section: [{
                heading: "",
                bulletPoints: []

            },
            {
                heading: "Posting a Sale Listing:",
                bulletPoints: [
                    "Fill in the required information about the pet.",
                    "Select the option to list the pet for sale.",
                    "Complete the payment process through our secure payment gateway",
                    "Once payment is confirmed, your listing will be published."]

            }]
        },
        {
            heading: "Refund Policy",
            content: "Non-Refundable Fees: The €20 fee for sale listings is non-refundable. Ensure all information is accurate and complete before submitting your listing.",
            section: [{
                heading: "",
                bulletPoints: []

            }]
        },
        {
            heading: "Compliance and Consequences",
            content: "Non-Refundable Fees: The €20 fee for sale listings is non-refundable. Ensure all information is accurate and complete before submitting your listing.",
            section: [{
                heading: "",
                bulletPoints: [
                    "Honesty in Listings: All listings must accurately represent the terms of adoption or sale. If a pet listed for free adoption is later found to require payment, the listing will be removed, and the pet provider may face restrictions on future listings.",
                    "Complaints and Investigations: Users can submit complaints if they find discrepancies in listings. BbPets Adoption will investigate complaints and take necessary actions, including listing removal and potential account suspension.",
                ]

            }]
        },
        {
            heading: "Contact Us",
            content: "For any questions or concerns regarding this Payment Policy, please contact our support team at [insert contact information].",
            section: [{
                heading: "",
                bulletPoints: [
                    "Honesty in Listings: All listings must accurately represent the terms of adoption or sale. If a pet listed for free adoption is later found to require payment, the listing will be removed, and the pet provider may face restrictions on future listings.",
                    "Complaints and Investigations: Users can submit complaints if they find discrepancies in listings. BbPets Adoption will investigate complaints and take necessary actions, including listing removal and potential account suspension.",
                ]

            }]
        }

    ]


    return (

        <Container id="payment_policy_wrapper">
            <Container id="payment_policy_container">
                <InfoComponent sections={dataSections} id="payment_policy_heading" title="Payment Policy"></InfoComponent>
                <Container>
                    <p>Thank you for using BbPets Adoption. We appreciate your cooperation in maintaining a trustworthy and effective pet adoption platform.</p>
                    <p>We will process your request within 30 days and confirm the deletion via email.</p>

                </Container>
            </Container>
        </Container>
    )
};

export default PaymentPolicy;