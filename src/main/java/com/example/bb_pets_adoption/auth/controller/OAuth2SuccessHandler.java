/**
 * 
 */
package com.example.bb_pets_adoption.auth.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/*
 * 
 * */
@Service
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{


	
    private final JwtUtil jwtTokenUtil;
    
    @Autowired
    public OAuth2SuccessHandler(JwtUtil jwtTokenUtil) {
    	
        this.jwtTokenUtil = jwtTokenUtil;
        System.out.println("OAuth2SuccessHandler initialized");
        System.out.println(jwtTokenUtil.toString());
    }

    
    /**
     * Triggered when a user has been successfully authenticated with Google Login service
     * It generates a JWT token and redirects the user to the specified URL with the token
     * 
     * @param request - the HttpServletRequest
     * @param response - the HttpServletResponse
     * @param authentication - the Authentication object
     * @throws IOException if an input or output exception occurs
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    	
        
    	OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    	// logg autheticated user
    	 System.out.println("Authenticated user: " + oAuth2User.getName());
    	 
    	// generate JWT token
        String token = jwtTokenUtil.generateToken(oAuth2User.getName());
        // logg generated token
        System.out.println("Generated JWT token: " + token);
        
        // set header
        response.setHeader("Authorization", "Bearer " + token);
        
        // create URL with token as a query parameter
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/home?")
                .queryParam("token", token)
                .build()
                .toUriString(); // convert the UriComponentsBuilder to a String
        
        // Debug statement
        System.out.println("Redirecting to:" + targetUrl);
        
        // redirect to the target URL
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
    

}