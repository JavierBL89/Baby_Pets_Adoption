//
import React, { useState, useEffect, createContext, useCallback } from 'react';
import { jwtDecode } from 'jwt-decode'; // Correct import
import { useLocation, useNavigate } from 'react-router-dom';

// create a context for authentication
const AuthContext = createContext();

/**
 *  AuthProvider component that will wrap the application and provide authentication state
 *  The Provider React component is a wrapper component that provides funtionalities 
 *  to all the components down the tree component from which is been wrapped
 *  
 *  This allows the user to navigate through the App as authenticated user without being logout all the time
 * 
 */
const AuthProvider = ({ children }) => {

    // state to keep track of whether the user is authenticated or not
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [registeredBy, setRegisteredBy] = useState("");
    const [userName, setUserName] = useState("");
    const location = useLocation();
    const navigate = useNavigate();

    // useEffect to check the authentication status 
    useEffect(() => {

        const checkToken = () => {
            const searchParams = new URLSearchParams(location.search);
            const token = searchParams.get('token') || localStorage.getItem('token');

            if (token) {
                try {
                    const decodedToken = jwtDecode(token); // decode token
                    if (decodedToken.exp * 1000 > Date.now()) {  // check if token is still valid
                        setIsAuthenticated(true);
                        localStorage.setItem('token', token);
                    } else {
                        localStorage.removeItem('token');
                        setIsAuthenticated(false);
                    }
                } catch (error) {
                    console.error('Invalid token:', error);
                    localStorage.removeItem('token');
                    setIsAuthenticated(false);
                }
            }
        };

        checkToken();
    }, [location.search]);



    /**
    * Login function sets the token in localStorage and update the authentication state
    * @param {string} token - JWT token received after successful login
    */
    const login = (token, registeredBy, userName) => {

        localStorage.setItem('token', token);
        setIsAuthenticated(true);
        setRegisteredBy(registeredBy);
        setUserName(userName);
        navigate(`/token=${token}`); // redirect to home page after successful login
    };

    /**
     * Logout function to remove the token from localStorage and update the authentication state
     */
    const logout = () => {
        localStorage.removeItem("token");
        setIsAuthenticated(false);   // reset state to false
        setRegisteredBy("");  // clear state
        setUserName("");  // clear state
        navigate("/"); // redirect to home page after successful logout
    };

    // pass the authentication state and functions to the component tree
    return (
        <AuthContext.Provider value={{
            isAuthenticated, login, logout, setRegisteredBy, registeredBy,
            setUserName, userName
        }}>
            {children}
        </AuthContext.Provider>
    )
};


export { AuthProvider, AuthContext };
