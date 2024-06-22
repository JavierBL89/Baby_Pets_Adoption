import React, { useContext, useEffect } from "react";
import LogoutButton from "./auth/components/LogoutButton";
import { AuthContext } from "../context/AuthContext";
import { useLocation, useNavigate } from 'react-router-dom'
import { Container, Row, Col, Stack } from "react-bootstrap";
import PetCategoriesHolder from "./pet/components/pet_listing/PetCategoriesHolder";
import PetListingHolder from "./pet/components/pet_listing/PetListingHolder";
import AdoptionInfoComponent from "./AdoptionInfoComponent";


/***
 * 
 * 
 */
const Home = () => {

    // state for displaying certain info and elements based on authentication conditions
    const { isAuthenticated, login } = useContext(AuthContext);


    return (


        <Stack>
            <PetCategoriesHolder />
            <PetListingHolder />
            <AdoptionInfoComponent />
        </Stack>


    )
}

export default Home;