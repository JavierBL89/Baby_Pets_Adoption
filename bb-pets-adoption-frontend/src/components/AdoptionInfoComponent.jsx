import React from "react";
import Heading from "./common/Heading";
import InfoComponent from "./common/InfoComponent";
import { Container } from "react-bootstrap";


/**
 *Component host the content for page /Data Deletion
 */
const AdoptionInfoComponent = () => {


    const sectionsData = [

        {
            heading: "Adopt a Pet with Confidence",
            content: "We understand that people have specific preferences when it comes to adopting pets. " +
                "To ensure the best experience, pet providers can only post adoption availability before the pets are born. " +
                "These announcements will remain active until two months after the estimated birth date.",
            section: [{
                heading: "",
                bulletPoints: []

            }]

        },
        {
            heading: "Would You Like to Adopt a Pet?",
            content: "Only subscribed users can utilize our adoption services and connect with pet providers. " +
                "Pet posts typically include essential information about the parents and facilitate communication methods to reach " +
                "out for further details before proceeding with the adoption application.",
            section: [{
                heading: "",
                bulletPoints: []

            }]
        },
        {
            heading: "Adoption Process",
            content: "",
            section: [{
                heading: "",
                bulletPoints: ["Application: Pet providers have the final say on whether to proceed with your application. To aid their decision, we will ask for some information about yourself during the application process. This ensures pet providers can confidently hand over the pet to the right person, ensuring the animal's safety and care.",
                    "Status Updates: After submitting your application, you will see the status updated to 'Pending.' You can check the status of your applications in the 'Applications' section of your profile. Pet providers will review your application and update the status to either 'Given preference to other applicants' or 'Selected.'",
                    "Multiple Applications: Pets may receive multiple adoption requests, so it's essential to provide as much relevant information as possible in your application."
                ]

            },
            {
                heading: "Want to Offer a Pet?",
                bulletPoints: [
                    "Subscription: You must be subscribed to post a pet for adoption. Fill in a form application where we'll ask for information about the purpose of the announcement.",
                    "Fees for Sales: If you wish to sell the pets, a small fee will be required for each post",
                    "Complete the payment process through our secure payment gateway",
                    "Honesty in Announcements: Pets announced for free but later found to require payment will result in the announcement being removed upon complaint submission. Future pet announcements may also face consequences."
                ]

            }]
        },
        {
            heading: "Lenth of Pet Listings",
            content: "To meet the site purposes and adopters requirements, we will extend the pet post's live up until 60 days after the expected birth date.",
            section: [{
                heading: "",
                bulletPoints: []

            }]
        }

    ]

    return (
        <Container>
            <InfoComponent sections={sectionsData} id="" title="Adoption Information"></InfoComponent>

        </Container>
    )
};

export default AdoptionInfoComponent;