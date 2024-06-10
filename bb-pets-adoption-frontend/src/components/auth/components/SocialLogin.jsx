import React from 'react';
import { GoogleLoginButton } from "react-social-login-buttons";


/**
 * 
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