import React, { useContext, useEffect } from "react";
import LogoutButton from "./auth/components/LogoutButton";
import { AuthContext } from "../context/AuthContext";
import { useLocation, useNavigate } from 'react-router-dom'
import { Container, Row, Col, Stack } from "react-bootstrap";
import PetCategoriesHolder from "./pet/components/pet_categories/PetCategoriesHolder";
import AdoptionInfoComponent from "./AdoptionInfoComponent";
import WelcomeComponent from "./pet/components/WelcomeComponent";


/***
 * 
 * 
 */
const Home = () => {

    // state for displaying certain info and elements based on authentication conditions
    const { isAuthenticated, login } = useContext(AuthContext);


    return (


        <Stack>
            <WelcomeComponent />
            <PetCategoriesHolder />
            <AdoptionInfoComponent />
        </Stack>


    )
}

export default Home;