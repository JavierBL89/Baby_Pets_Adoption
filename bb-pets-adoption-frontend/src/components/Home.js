import React, { useContext, useEffect } from "react";
import LogoutButton from "./auth/components/LogoutButton";
import { AuthContext } from "../context/AuthContext";
import { useLocation, useNavigate } from 'react-router-dom'


const Home = () => {

    const { isAuthenticated, login } = useContext(AuthContext);

    const location = useLocation();
    const navigate = useNavigate();



    return (

        <div>

            <h1>BaBy Pets Adoption</h1>
            {isAuthenticated ? (
                <div>
                    <p>Welcome, you are logged in!</p>
                    <LogoutButton></LogoutButton>
                </div>
            )
                :
                (
                    <p>You are not logged in. Please log in to access more features.</p>
                )
            }

        </div>
    )
}

export default Home;