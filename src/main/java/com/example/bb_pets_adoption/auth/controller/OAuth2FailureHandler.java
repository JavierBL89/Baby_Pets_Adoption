package com.example.bb_pets_adoption.auth.controller;

import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * 
 * **/
@Service
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

	
	
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // logg authentication failure
    	System.out.println("Authentication failed: " + exception.getMessage());
        exception.printStackTrace();  // Print the stack trace for detailed debugging
        
        //r edirect to the login page with error=true
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/login")
                .queryParam("error", true)
                .build()
                .toUriString();
                System.out.println("Redirecting to: " + targetUrl);

                response.sendRedirect(targetUrl);
    }
}
