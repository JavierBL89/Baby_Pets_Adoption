import React, { useContext } from 'react';
import { GoogleLoginButton } from "react-social-login-buttons";
import { AuthContext } from '../../../context/AuthContext';


/**
 * SocialLogin component handles login with social providers like Google
 * 
 * - Uses AuthContext for managing authentication state
 * - Redirects the user to Google's OAuth2 authorization endpoint
 * - After successful login, the backend should handle redirecting back to the application
 */
const SocialLogin = () => {

    // url endpoints
    const googleLoginUrl = 'http://localhost:8080/oauth2/authorization/google';

    return (
        <div>
            <h3>Login with</h3>
            <a href={googleLoginUrl}><GoogleLoginButton></GoogleLoginButton></a>
        </div>
    )

};

export default SocialLogin;