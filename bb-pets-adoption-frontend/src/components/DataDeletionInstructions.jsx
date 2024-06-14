import React from "react";
import Heading from "./common/Heading";

/**
*
 */
const DataDeletionInstructions = () => {


    return (
        <div>
            <Heading tagName="h4" id="data_deletion__heading" className="" text="Data Deletion Instructions" />

            <div>
                <p>If you want to delete your data from our app, please follow these steps:</p>
                <ol>
                    <li>Send an email to <a href="mailto:your-email@example.com">your-email@example.com</a> with the subject line "Data Deletion Request".</li>
                    <li>Include your name and the email address associated with your account in the body of the email.</li>
                    <li>Specify that you want to delete all your data from our app.</li>
                </ol>
                <p>We will process your request within 30 days and confirm the deletion via email.</p>

            </div>
        </div>
    )
};

export default DataDeletionInstructions;