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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * **/
@Service
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

	
	// create an instance of Logger
		private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
		
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // logg authentication failure
    	logger.error("Authentication failed: " + exception.getMessage());
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
