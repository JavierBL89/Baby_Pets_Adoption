import React, { useCallback, useContext, useEffect, useState } from "react";
import { Container, Row, Col, Stack, Spinner, Button } from "react-bootstrap";
import Heading from "../../../common/Heading";
import TimeStampComponent from "./TimeStampComponent";
import CardList from "./CardList";
import { useNavigate, useParams } from "react-router-dom";
import instance from "../../../../scripts/axiosConfig";
import TextComponent from "../../../common/TextComponent";
import PostActionMessage from "../../../common/PostActionMessage";
import { FeedbackContext } from "../../../../context/FeedBackContext";
import { NotificationsContext } from "../../../../context/NotificationsContext";
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
const MyListings = () => {

    const { token } = useParams();  // grab token from url params
    const { postActionMessage, setPostActionMessage } = useContext(FeedbackContext);
    const { listOfDroppAppsNotifications, setListOfDroppAppsNotifications } = useContext(NotificationsContext);

    const [filteredNotifications, setFilteredNotifications] = useState([]);

    const [message, setMessage] = useState("");
    const [listings, setListings] = useState([]);
    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(true);
    const [totalPages, setTotalPages] = useState(0);
    const [orderBy, setOrderBy] = useState("asc");

    const navigate = useNavigate();


    /***
       * Method responsible for making a GET request to retrieve all pet listings
       *  related to the current authenticated user
       * 
       * Steps:
        * 1. Set data list to empty 
        * 2. Check if token variable is missing
        * 3. Set loading variable to true while proccessing task
        * 4. Make GET request
        * 5. Check and handle the response while setting messages for user feedback
        * 
        * useCallback hook is used to memoize the fetchListingsData. It returns a memoized version 
        * of the callback that only changes if one of the dependencies has changed
        * It optimizes performance by preventing re-renders.
        * 
        * @param {String} objectId - the id of the petList to be removed
        */
    const fetchListingsData = useCallback(async () => {

        setListings([]);   // reset to empty before every render
        if (!token) {
            console.error("Missing authentication token. GET request could not be initiated");
            setMessage("Missing authentication token. Please login and try again");
            return;
        }

        setLoading(true);

        try {
            const response = await instance.get(`/pets/my_listings?token=${token}&order=${orderBy}&pageNo=${page}&page=${page}&size=6`);
            if (response.status === 200) {
                if (response.data && response.data.petList && response.data.petList != null && response.data.petList.length > 0) {
                    console.log(response.data.petList);
                    // format Date objects before setting data state
                    const formattedListings = response.data.petList.map(listing => ({
                        ...listing,
                        createdOn: new Date(...listing.createdOn).toLocaleDateString(), // Convert array to date string
                        birthDate: new Date(...listing.pet.birthDate).toLocaleDateString() // Convert array to date string
                    }));

                    setListings(formattedListings);
                    setTotalPages(response.data.totalPages);
                } else {
                    setMessage("You have currently no pets listed available.");
                }
            } else {
                throw new Error(`HTTP error status: ${response.error}`);
            }
        } catch (error) {
            setMessage("Error while retrieving data. Please try again later." + error.message);
            throw new Error("Error while retrieving data: " + error.message);
        } finally {
            setLoading(false);
        }
    }, [token, page, orderBy]);



    /***
    * Method responsible for making a DELETE request to remove the selected petList
    * from the database
    * 
    * Steps:
     * 1. Check if token variable is missing
     * 2. Check if the current pet listing id is not empty
     * 3. Make DELETE request
     * 5. Check and handle the response while setting messages for user feedback
     * 
     * @param {String} objectId - the id of the petList to be removed
     */
    const handleDelete = async (objectId) => {

        if (!token) {
            setMessage("Operation cannot be processed. Missing authenication token")
            return;
        }
        const trimmedToken = token.trim();   // eliminate any possible white spaces

        // check id variable is not empty
        if (objectId) {
            try {
                // DELETE request
                const response = await instance.delete(`/pets/delete_pet?token=${trimmedToken}&petListId=${objectId}`);
                if (response.status === 200) {
                    setPostActionMessage("Pet listing has been sucessfully removed.") // store post-action message
                    setListings([]);   // reset to empty before every render
                    fetchListingsData();    // reload page with new data
                } else {
                    console.error("Item could not be removed:", response.data);
                    setMessage("A server error occured and pet could not be removed.Please try later or contact admin to inform about the problem.")
                }
            } catch (error) {
                console.error('Error deleting item:', error);
            }
        } else {
            setMessage("Item could not be deleted. Logout and try again")
            console.error("Missing object Id to complete operation. Please ensure variable 'objectId' is defined.");
        }

    };


    /***
     * Method to redirect user to /update_pet page
     * to allow them to update the selected pet data
     * 
     * @param {String} petObject - the objectId of the curent pet
     * @param {String} petListingId - the id of the current PetList object(container for post data)
     */
    const handleUpdate = (petObject, petListingId) => {
        // serialize listing object into a JSON string to pass it in URL param
        const petObjectString = encodeURIComponent(JSON.stringify(petObject));
        navigate(`/update_pet/${petObjectString}/${petListingId}/${token}`);
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
                fetchListingsData();
            } else {
                console.error("Item could not be removed:", response.data);
                setMessage("A server error occured and pe could not be removed.Please try later or contact admin to inform about the problem.")
            }
        } catch (error) {
            console.error('Error deleting item:', error);
        }
    }

    /* 
    * useEffect listens to changes on 'page', 'token', 'fetchListingsData'
    * to calls the  fetchListingsData()
    */
    useEffect(() => {
        fetchListingsData();
    }, [page, token, orderBy, fetchListingsData]);


    /****
     * 
     * It iterates over the list of pet listings and filters out 
     * the ones having pending notifications where notification type is "drop"
     */
    useEffect(() => {
        if (listings.length > 0) {
            setFilteredNotifications(listings.flatMap(petListing => petListing.pendingNotifications.filter(notification => notification.type === "drop")));
        };
    }, [listings]);


    /****
     * useEffect listens to changes on 'filteredNotifications' to then iterate over the list
     * of notifications and store their messages in another list kept in application state
     * from NotificationsContex 'listOfDroppAppsNotifications'
     * 
     * That list is to notify the user of dropped notifications
     */
    useEffect(() => {
        if (filteredNotifications.length >= 1) {
            const messageList = filteredNotifications.map(notification => {
                return notification.message;
            });
            setListOfDroppAppsNotifications(messageList);
        }
    }, [filteredNotifications, setListOfDroppAppsNotifications]);


    /****
     * When user selects to load more, the page state is updated 
     * and the useEffect gets triggered and starts the GET requets 
     * since it is listening to changes on that state variable.
     */
    const loadMoreListings = () => {
        setPage(prevPage => prevPage + 1);
    };

    return (

        <Container id="my_listings_wrapper">
            <Container id="my_listings_container">
                { /*************** Post-action Feedback message  *********************/}
                <Row >
                    <Container id="post_action_message_holder">
                        {!loading && postActionMessage && (
                            <PostActionMessage text={postActionMessage} />
                        )}
                    </Container>
                </Row>
                { /*************** Notification warnings  *********************/}
                <Row >
                    <Container id="notification_message_holder">
                        {filteredNotifications.map(notification =>
                            <NotificationMessageComponent
                                token={token}
                                text={notification.message}
                                notificationId={notification.id}
                                onViewed={handleViewed}
                            />)
                        }
                    </Container>
                </Row>
                <Row >

                    { /**************** FILTER LISTINGS  ***********/}
                    <Col xs={7} sm={5} md={3} id="my_listings_period_holder">
                        <Row > { /***** CREATE NEW PET BUTTON  *****/}
                            <a id="create_new_pet" href={`/list_new_pet/${token}`} className="btn btn-primary" >List a New Pet</a>
                        </Row>
                    </Col>

                    { /******************************* PET LISTINGS  ******************************/}
                    <Col xs={12} md={9}>

                        <Row id="my_listings_list_holder">
                            { /*************** LOADING SPINNER  *********************/}
                            {loading &&
                                <Row id="my_listings_spinner_holder">
                                    <Spinner animation="border" />
                                </Row>}

                            {
                                listings.length > 0 ? (
                                    listings.map((petListing, index) => {
                                        return (
                                            <CardList key={index}
                                                id={index}
                                                token={token}
                                                // this field is crucial to retreive the obeject on back-end for updating functionality
                                                petListing={petListing}
                                                petId={petListing.pet.id}
                                                motherImage={petListing.pet.motherImg}
                                                motherBreed={petListing.pet.motherBreed}
                                                createdOn={petListing.createdOn}
                                                price={petListing.pet.price}
                                                birthDate={petListing.pet.birthDate}
                                                petCreatedOn={petListing.createdOn}
                                                petUpdatedOn={petListing.updatedOn}
                                                // delete listing button passes the petId
                                                onDelete={handleDelete}
                                                // update listing button (pass the entire listing object)
                                                onUpdate={handleUpdate}

                                            />)
                                    })
                                ) : <>
                                    <Row >
                                        <TextComponent id="my_listings_message" text={message && message} />
                                    </Row>
                                </>
                            }
                        </Row>

                    </Col>
                    {!loading && page < totalPages - 1 && (
                        <Button onClick={loadMoreListings} disabled={loading}>
                            Load More
                        </Button>
                    )}
                </Row>
            </Container>
        </Container>
    );
};

export default MyListings;
