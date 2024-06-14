import React, { useContext, useEffect } from "react";
import LogoutButton from "./auth/components/LogoutButton";
import { AuthContext } from "../context/AuthContext";
import { useLocation, useNavigate } from 'react-router-dom'
import { Container, Row, Col, Stack } from "react-bootstrap";
import PetCategoriesHolder from "./pet/components/PetCategoriesHolder";

const Home = () => {

    const { isAuthenticated, login } = useContext(AuthContext);


    return (


        <Stack>
            <PetCategoriesHolder />
        </Stack>


    )
}

export default Home;