import React, { useCallback, useEffect, useState } from "react";
import { Container, Row, Col, Stack, Spinner, Button } from "react-bootstrap";
import Heading from "../../../common/Heading";
import { useNavigate, useParams } from "react-router-dom";
import instance from "../../../../scripts/axiosConfig";
import TextComponent from "../../../common/TextComponent";
import ButtonComponent from "../../../common/ButtonComponent";
import ApplicationCard from "./ApplicationCard";
import useFetchById from "../../../hooks/data/fetchById";

/***
 * Component atc as core of my_listings page.
 * 
 * Is responsibe for displaying all pet listings assocciated to the user (authenticated),
 * , and allows them to review, update, and delete their listings.
 * 
 * The useEffetc listens to changes on variable token, which is retrieve from the url, to initiate the GET request procces.
 * 
 */
const MyApplications = () => {

    const { token } = useParams();  // grab token from url params


    const [message, setMessage] = useState("");
    const [applications, setApplications] = useState([]);
    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(false);
    const [totalPages, setTotalPages] = useState(0);
    const [orderBy, setOrderBy] = useState("asc");

    const navigate = useNavigate();

    const fetchApplicationsData = useCallback(async () => {

        setApplications([]);   // rest data state every time the method is called


        // ensure token is not empty
        if (!token) {
            console.error("Missing authentication token. GET request could not be initiated");
            setMessage("Missing authentication token. Please login and try again");
            return;
        }

        setLoading(true);  // set to true while retrieving data

        try {

            const response = await instance.get(`/adoption/my_applications?token=${token}&order=${orderBy}&page=${page}&size=6`);

            if (response.status === 200) {

                if (response.data && response.data.applications.length > 0) {
                    console.log(response.data.applications);
                    // format Date objects before setting data state
                    const formattedListings = response.data.applications.map(listing => ({
                        ...listing,
                        createdOn: new Date(...listing.applicationDate).toLocaleDateString(), // Convert array to date string
                    }));
                    setApplications(formattedListings);
                    setTotalPages(response.data.totalPages);
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

    }, [token, page, orderBy]);

    /***
     * 
     */
    const handleDrop = async (applicationId) => {
        console.log("putaa");
        if (!token) {

            setMessage("Operation cannot be processed. Missing authenication token")
            return;
        }

        const trimmedToken = token.trim();   // eliminate any possible white spaces

        if (applicationId) {

            try {
                // DELETE request
                const response = await instance.delete(`/adoption/delete_application?token=${trimmedToken}&applicationId=${applicationId}`);
                if (response.status === 200) {
                    fetchApplicationsData();

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


    /****
     * useEffect listens to changes on 'page', 'token', 'fetchListingsData'
     * to calls the  fetchListingsData()
     */
    useEffect(() => {

        fetchApplicationsData();

    }, [page, token, orderBy, fetchApplicationsData])


    /****
     * When user selects to load more, the page state is updated 
     * and the useEffect gets triggered and starts the GET requets 
     * since it is listening to changes on that state variable.
     */
    const loadMoreListings = () => {
        setPage(prevPage => prevPage + 1);
    };


    return (

        <Container id="my_applications_wrapper">
            <Container id="my_applications_container">
                <Row >
                    { /*************** APLICATIONS LIST  *********************/}
                    <Row >
                        <TextComponent id="my_applications_message" text={message && message} />
                    </Row>
                    <Row id="my_applications_list_wrapper">
                        <Row id="my_applications_list_holder">
                            { /************ LOADING SPINNER  ************/}
                            {loading &&
                                <Row id="my_applications_spinner_holder">
                                    <Spinner animation="border" />
                                </Row>}


                            {
                                applications && applications.map((application, index) => {

                                    return (
                                        <ApplicationCard key={index}
                                            id={index}
                                            applicationId={application.id}
                                            status={application.status}
                                            price={application.pet.price}
                                            ownerName={application.pet.ownerName}
                                            motherImg={application.pet.motherImg}
                                            motherBreed={application.pet.motherBreed}
                                            applicationDate={application.applicationDate}
                                            location={application.pet.location}
                                            // delete listing button passes the petId
                                            onDelete={handleDrop}

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

export default MyApplications;
