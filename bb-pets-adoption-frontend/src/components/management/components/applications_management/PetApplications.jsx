import React, { useCallback, useEffect, useState, useContext } from "react";
import { Container, Row, Col, Stack, Spinner, Button } from "react-bootstrap";
import Heading from "../../../common/Heading";
import { useNavigate, useParams } from "react-router-dom";
import instance from "../../../../scripts/axiosConfig";
import TextComponent from "../../../common/TextComponent";
import ButtonComponent from "../../../common/ButtonComponent";
import PetApplicationCard from "./PetApplicationCard";
import ApplicationStatusTabComponent from "./ApplicationStatusTabComponent";
import { FeedbackContext } from "../../../../context/FeedBackContext";
import PostActionMessage from "../../../common/PostActionMessage";
import NotificationMessageComponent from "../../../notifications/components/NotificationMessageComponent";

/***
 * Component atc as core of my_listings page.
 * 
 * Is responsibe for displaying all pet listings assocciated to the user (authenticated),
 * , and allows them to review, update, and delete their listings.
 * 
 * The useEffetc listens to changes on variable token, which is retrieve from the url, to initiate the GET request procces.
 * 
 */
const PetApplications = () => {

    const { token, petId } = useParams();  // grab token from url params
    const { postActionMessage, setPostActionMessage } = useContext(FeedbackContext);  // get global message from FeedbackContext

    const [applications, setApplications] = useState([])
    const [message, setMessage] = useState("")
    const [selectedTab, setSelectedTab] = useState("Pending")

    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(false);
    const [totalPages, setTotalPages] = useState(0);
    const [orderBy, setOrderBy] = useState("asc");

    const navigate = useNavigate();

    /*
    * Method responsible for retrieving all adoption applications related to a single pet,
    * and filters the search based on the applciations status selected
    *
    * Steps:
    * 1. Set data list to empty 
    * 2. Set loading variable to true while proccessing task
    * 2. Check if token variable is missing
    * 4. Make GET request
    * 5. Check and handle the response while setting messages for user feedback
    * 
     * useCallback hook is used to memoize the fetchListingsData. It returns a memoized version 
     * of the callback that only changes if one of the dependencies has changed
     * It optimizes performance by preventing re-renders.
     * 
     */
    const fetchApplicationsData = useCallback(async () => {

        // ensure token is not empty
        if (!token) {
            console.error("Missing authentication token. GET request could not be initiated");
            setMessage("'Missing authentication token. Please login and try again")
            return;
        }

        setLoading(true);  // set to true while retrieving data
        setApplications([]);  // reset state to an empty list before fetching any data
        setMessage("");   // reset message state
        try {

            const response = await instance.get(`/adoption/pet/applications?token=${token}&petId=${petId}&status=${selectedTab}&order=${orderBy}&page=${page}&size=6`);

            if (response.status === 200) {
                console.log(response.data);
                if (response.data && response.data.applications.length >= 1) {

                    // format Date objects before setting data state
                    const formattedListings = response.data.applications.map(appplication => ({
                        ...appplication,
                        applicationDate: new Date(...appplication.applicationDate).toLocaleDateString(), // Convert array to date string
                        birthDate: new Date(...appplication.pet.birthDate).toLocaleDateString() // Convert array to date string
                    }));
                    setApplications(formattedListings);
                    setTotalPages(response.data.totalPages);

                } else {
                    setMessage("No applications with status '" + selectedTab + "'");
                }
            } else {
                throw new Error(`HTTP error status: ${response.status}`);
            }

        } catch (error) {
            setMessage("Error while retrieving data. Please try again later." + error.message);
            throw new Error("Error while retrieving data: " + error.message);
        } finally {
            setLoading(false);
        }

    }, [token, page, selectedTab, petId]);



    /***
     * 
     */
    const handleDrop = async (objectId) => {

        if (!token) {
            setMessage("Operation cannot be processed. Missing authenication token")
            return;
        }

        const trimmedToken = token.trim();   // eliminate any possible white spaces

        if (objectId) {

            try {
                // DELETE request
                const response = await instance.delete(`/pets/delete_pet?token=${trimmedToken}&petListId=${objectId}`);
                if (response.status === 200) {
                    fetchApplicationsData();    // reload page with new data                 
                    // Store feedback message in localStorage
                    localStorage.setItem('feedbackMessage', 'Application successfully removed!');
                    navigate(`/my_applications/${token}`);

                } else {
                    console.error("Item could not be removed:", response.data);
                    setMessage("A server error occured and pe could not be removed.Please try later or contact admin to inform about the problem.")
                }

            } catch (error) {
                console.error('Error deleting item:', error);
            }
        } else {
            setMessage("Item could not be deleted. Logout and try again")
            console.error("Missing object Id to complete operation. Please ensure variable 'objectId' is defined.");
        }

    };

    /**
    * Method to handle notifications as marked.
    * It makes a PUT request to update the notifiction status
    * @param {*} notificationId - the ID of the notification
    * @param {*} applicationId - the ID of the adoptuion application
    * @returns 
    */
    const handleViewed = async (notificationId) => {

        if (!notificationId || notificationId == null) {
            return
        }
        console.log(token);
        console.log(notificationId);
        try {
            // PUT request
            const response = await instance.put(`/notifications/markAsViewed?token=${token}&notificationId=${notificationId}`);
            if (response.status === 200) {
                fetchApplicationsData();
            } else {
                console.error("Item could not be removed:", response.data);
                setMessage("A server error occured and pe could not be removed.Please try later or contact admin to inform about the problem.")
            }
        } catch (error) {
            console.error('Error deleting item:', error);
        }
    }

    /***
     * Method sets the state with the selected status tab name
     * 
     * @param {String} tabName - the selected status tab name
     */
    const handleTabSelection = (tabName) => {
        setSelectedTab(tabName);
    };

    /****
     * useEffect listens to changes on 'page', 'token', 'fetchListingsData'
     * to calls the  fetchListingsData()
     */
    useEffect(() => {
        fetchApplicationsData();
    }, [fetchApplicationsData, token, petId, selectedTab])


    /****
     * When user selects to load more, the page state is updated 
     * and the useEffect gets triggered and starts the GET requets 
     * since it is listening to changes on that state variable.
     */
    const loadMoreListings = () => {
        setPage(prevPage => prevPage + 1);
    };



    return (

        <Container id="pet_applications_wrapper">
            <Container id="pet_applications_container">
                <Row >
                    { /*************** Notification warnings  *********************/}
                    <Row id="notification_message_holder">
                        {applications.flatMap(app =>
                            app.pendingNotifications.map(notification => {
                                let message = "";
                                if (notification.type === "application") {
                                    message = `New adoption application. Application ref: '${app.appTracker}'`;
                                } else if (notification.type === "drop") {
                                    message = notification.message;
                                }
                                return (
                                    message !== null && (
                                        <NotificationMessageComponent
                                            key={notification.id} // Use a unique identifier for keys
                                            notificationId={notification.id}
                                            applcationId={app.id}
                                            onViewed={handleViewed}
                                            token={token}
                                            text={message}
                                        />
                                    )
                                );
                            })
                        )}
                    </Row>
                    { /*************** Post-action Feedback message  *********************/}
                    <Row>
                        <Container id="post_action_message_holder">
                            {!loading && postActionMessage && (
                                <PostActionMessage text={postActionMessage} />
                            )}
                        </Container>
                    </Row>
                    { /*************** APLICATIONS LIST  *********************/}
                    <Row>
                        < ApplicationStatusTabComponent onTabSelect={handleTabSelection} />
                    </Row>
                    <Row id="pet_applications_list_wrapper">
                        <Row id="pet_applications_list_holder" >

                            { /************ LOADING SPINNER  ************/}
                            {loading &&
                                <Row id="my_applications_spinner_holder">
                                    <Spinner animation="border" />
                                </Row>}

                            { /*************** Feedback message  *********************/}
                            {message &&
                                <Row id="pet_applications_message_holder">
                                    <TextComponent id="pet_applications_message" text={message} />
                                </Row>
                            }

                            {
                                applications && applications.map((application, index) => {

                                    return (
                                        <PetApplicationCard key={index}
                                            id={index}
                                            application={application}
                                            status={application.status}
                                            token={token}
                                            comments={application.comments}
                                            petId={application.petId}
                                            onFetchData={() => fetchApplicationsData()}
                                            // delete listing button passes the petId
                                            onDelete={() => handleDrop()}

                                        />
                                    )
                                }
                                )

                            }

                        </Row>
                    </Row>
                </Row>

            </Container>
        </Container >
    );

};

export default PetApplications;
