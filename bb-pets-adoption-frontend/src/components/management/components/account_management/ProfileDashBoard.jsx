import React, { useState, useContext } from "react";

import { AuthContext } from "../../../../context/AuthContext";
import { Container, Row, Spinner } from "react-bootstrap";

import { FeedbackContext } from "../../../../context/FeedBackContext";
import PostActionMessage from "../../../common/PostActionMessage";
import DashBoardTabComponent from "./DashBoardTabComponent";
import EmailUpdateComponent from "./EmailUpdateComponent";
import DetailsUpdateComponent from "./DetailsUpdateComponent";


/****
 * 
 * Componenet responsible for rendering the pet view page, with details of the the selected pet.
 * 
 * Component dependencies:
 * - It uses url params to grab the selected petId and the category of this, 
 * which will be crucial for retreivig the pet from database.
 * - Depends on 'AuthContext' to verify the user is authenticated for specific operations
 * - Depends on 'FeedbackContext' to display pots-action feedback messages
 * - Depends on 'useFetchById', a custom hook to make GET requests for a selected. It returns the pet data 
 *   and  usefule variables to be used for UX and error feedback
 */
const ProfileDashBoard = () => {


    const { isAuthenticated } = useContext(AuthContext);
    const { postActionMessage } = useContext(FeedbackContext);
    const [message, setMessage] = useState(false);   // failedMessage state

    const [myDetails, setMyDetails] = useState(false);
    const [emailChange, setEmailChange] = useState(false);
    const [loading, setLoading] = useState(false);


    /***
   * Method sets the state with the selected status tab name
   * 
   * @param {String} tabName - the selected status tab name
   */
    const handleTabSelection = (tabName) => {
        switch (tabName) {
            case "My details":
                if (emailChange) {
                    setEmailChange(!emailChange);
                }
                setMyDetails(!myDetails);
                break;
            case "Email address":
                if (myDetails) {
                    setMyDetails(!myDetails);
                }
                setEmailChange(!emailChange);
                break;

            default:
                setMessage("Invalid tab name.")
                break;
        }
    };

    return (
        <Container id="profile_dashboard_wrapper" className="">
            <Container id="profile_dashboard_container" className="">

                { /*************** Post-action Feedback message  *********************/}
                <Row >
                    <Container id="post_action_message_holder">
                        {!loading && postActionMessage && (
                            <PostActionMessage text={postActionMessage} />
                        )}
                    </Container>
                </Row>
                { /************ Loading spinner  ************/}
                {loading &&
                    <Row id="profile_dashboard_spinner_holder">
                        <Spinner animation="border" />
                    </Row>
                }


                { /*************** dashboard options component  *********************/}
                <Row>
                    <DashBoardTabComponent onTabSelect={handleTabSelection} />
                </Row>
                <Row>
                    {myDetails &&
                        <DetailsUpdateComponent />
                    }
                    {emailChange &&
                        <EmailUpdateComponent />
                    }
                </Row>


            </Container>
        </Container >
    );



};

export default ProfileDashBoard;