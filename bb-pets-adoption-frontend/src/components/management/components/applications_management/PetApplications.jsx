import React, { useCallback, useEffect, useState } from "react";
import { Container, Row, Col, Stack, Spinner, Button } from "react-bootstrap";
import Heading from "../../../common/Heading";
import { useNavigate, useParams } from "react-router-dom";
import instance from "../../../../scripts/axiosConfig";
import TextComponent from "../../../common/TextComponent";
import ButtonComponent from "../../../common/ButtonComponent";
import PetApplicationCard from "./PetApplicationCard";
import ApplicationStatusTabComponent from "./ApplicationStatusTabComponent";

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

    const [message, setMessage] = useState("");

    const [applications, setApplications] = useState([])
    const [selectedTab, setSelectedTab] = useState("Pending")

    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(false);
    const [totalPages, setTotalPages] = useState(0);
    const [orderBy, setOrderBy] = useState("asc");

    const navigate = useNavigate();


    /***
     * 
     */
    const fetchApplicationsData = useCallback(async () => {

        // ensure token is not empty
        if (!token) {
            console.error("Missing authentication token. GET request could not be initiated");
            setMessage("Missing authentication token. Please login and try again");
            return;
        }

        setLoading(true);  // set to true while retrieving data
        console.log(selectedTab);
        setApplications([]);  // reset state to an empty list before fetching any data
        try {

            const response = await instance.get(`/adoption/pet/applications?token=${token}&petId=${petId}&status=${selectedTab}&order=${orderBy}&page=${page}&size=6`);

            if (response.status === 200) {
                console.log(response.data);
                if (response.data && response.data.applications.length > 0) {
                    console.log(response.data);

                    // format Date objects before setting data state
                    const formattedListings = response.data.applications.map(appplication => ({
                        ...appplication,
                        applicationDate: new Date(...appplication.applicationDate).toLocaleDateString(), // Convert array to date string
                        birthDate: new Date(...appplication.pet.birthDate).toLocaleDateString() // Convert array to date string
                    }));
                    setApplications(formattedListings);
                    setTotalPages(response.data.totalPages);
                    console.log(formattedListings);

                } else {
                    setMessage("No results.");
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
                    // set feedback message to be ddiplayed for 3 seconds
                    setMessage("Pet successfully removed from your listings.")
                    setTimeout(() => {
                        setMessage("");
                    }, 3000);
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


    /***
     * 
     */
    const handleUpdate = () => {
        // redirect to
        navigate(`/my_applications/${token}`);
    };

    /***
     * 
     */
    const handleTabSelection = (tabName) => {
        setSelectedTab(tabName);
        console.log("Selected Tab:", tabName); // You can remove this line, it's just for demonstration
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

                    { /*************** APLICATIONS LIST  *********************/}
                    <Row >
                        <TextComponent id="pet_applications_message" text={message && message} />
                    </Row>
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
                                            onFetchData={fetchApplicationsData}
                                            // delete listing button passes the petId
                                            onDelete={() => handleDrop()}
                                            // update listing button (pass the entire listing object)
                                            onUpdate={() => handleUpdate()}

                                        />
                                    )
                                }
                                )

                            }

                        </Row>
                    </Row>

                    { /*************** Load More Button  *********************/}
                    {!loading && page < totalPages - 1 && (
                        <Button onClick={loadMoreListings} disabled={loading}>
                            Load More
                        </Button>
                    )}
                </Row>

            </Container>
        </Container >
    );

};

export default PetApplications;
