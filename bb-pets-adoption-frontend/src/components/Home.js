import React, { useContext, useEffect } from "react";
import LogoutButton from "./auth/components/LogoutButton";
import { AuthContext } from "../context/AuthContext";
import { useLocation, useNavigate } from 'react-router-dom'
import { Container, Row, Col, Stack } from "react-bootstrap";
import PetCategoriesHolder from "./pet/components/PetCategoriesHolder";
import AdoptionInfoComponent from "./AdoptionInfoComponent";

const Home = () => {

    const { isAuthenticated, login } = useContext(AuthContext);


    return (


        <Stack>
            <PetCategoriesHolder />
            <AdoptionInfoComponent />
        </Stack>


    )
}

export default Home;