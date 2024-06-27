import React, { useCallback, useEffect, useState } from "react";
import { Container, Row, Col, Stack, Spinner, Button } from "react-bootstrap";
import Heading from "../../../common/Heading";
import TimeStampComponent from "./TimeStampComponent";
import CardList from "./CardList";
import { useParams } from "react-router-dom";
import instance from "../../../../scripts/axiosConfig";
import TextComponent from "../../../common/TextComponent";


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
    const [message, setMessage] = useState("");
    const [listings, setListings] = useState([]);
    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(false);
    const [totalPages, setTotalPages] = useState(0);
    const [orderBy, setOrderBy] = useState("asc");

    // amazon S3 services base URL
    const awsS3baseURL = "https://baby-pets-adoption.s3.eu-west-1.amazonaws.com/"

    const fetchListingsData = useCallback(async () => {

        if (!token) {
            console.error("Missing authentication token. GET request could not be initiated");
            setMessage("Missing authentication token. Please login and try again");
            return;
        }

        setLoading(true);
        try {

            const response = await instance.get(`/pets/my_listings?token=${token}&order=${orderBy}&page=${page}&size=6`);

            if (response.status === 200) {

                if (response.data && response.data.length > 0) {
                    console.log(response.data);
                    // format Dtae objects before being passed to "CardList"
                    const formattedListings = response.data.map(listing => ({
                        ...listing,
                        createdOn: new Date(...listing.createdOn).toLocaleDateString(), // Convert array to date string
                        birthDate: new Date(...listing.pet.birthDate).toLocaleDateString() // Convert array to date string
                    }));
                    setListings(formattedListings);
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

    }, [token]);


    /***
     * 
     */
    const handleDelete = async (objectId) => {


        if (!token) {

            setMessage("Operation cannot be processed. Missing authenication token")
            return;
        }

        const trimmedToken = token.trim();

        if (objectId) {

            console.log(objectId);
            console.log(token);
            try {
                // DELETE request
                const response = await instance.delete(`/pets/delete_pet?token=${trimmedToken}&petListId=${objectId}`);
                if (response.status === 200) {
                    fetchListingsData();
                    // set feedback message to be ddiplayed for 3 seconds
                    setMessage("Pet successfully removed from our listings.")
                    setTimeout(() => {
                        setMessage("");
                    }, 3000);
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
    const handleUpdate = (objectId) => {


    };

    /****
     * useEffect listens to changes on 'page', 'token', 'fetchListingsData'
     * to calls the  fetchListingsData()
     */
    useEffect(() => {

        fetchListingsData();

    }, [page, token, orderBy, setOrderBy, fetchListingsData])


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
                <Row >

                    <Col xs={3} id="my_listings_period_holder">
                        <Row >
                            <Heading tagName="h6" id="my_listings_period_title" text="From" />
                        </Row>
                        <Row>
                            <Stack>
                                {

                                    /*******
                                             LOGIC HERE
                                         */
                                    <TimeStampComponent />
                                }


                            </Stack>
                        </Row>
                    </Col>
                    <Col xs={9}>
                        <Row >
                            <TextComponent id="my_listings_message" text={message && message} />
                        </Row>
                        <Row id="my_listings_list_wrapper">

                            <Col id="my_listings_list_holder">

                                {

                                    listings && listings.map((listing, index) => {


                                        return (
                                            <CardList key={index}
                                                id={index}
                                                motherImage={listing.pet.motherImg}
                                                motherBreed={listing.pet.motherBreed}
                                                createdOn={listing.createdOn}
                                                price={listing.pet.price}
                                                birthDate={listing.pet.birthDate}
                                                objectId={listing.id}
                                                onDelete={handleDelete}
                                                onUpdate={handleUpdate}

                                            />)
                                    })

                                }


                            </Col>

                        </Row>

                    </Col>
                    {loading && <Spinner animation="border" />}
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
