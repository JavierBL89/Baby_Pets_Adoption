/**
 * 
 */
package com.example.bb_pets_adoption.auth.controller;

import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.xml.bind.DatatypeConverter;

/**
 * JwtUtil is a utility class for generating, parsing, and validating JWT tokens
 * 
 * It uses the io.jsonwebtoken library for handling JWT operations.
 * 
 * javax.xml.bind dependencies are added to ensure the DatatypeConverter class is available
 * 
 * The DatatypeConverter is used to handle base64 encoding and decoding
 */

@Component
public class JwtUtil {

	
	// secret key for signing JWT tokens
	private String SECRET_KEY = "secret";
	
	
	
	
	
	/**
     * Method extracts the username (getSubject) from the JWT token
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
	public String extractUsername(String token) {
		
		return extractClaim(token, Claims::getSubject);
	}
	
	
	/**
     * Method extracts the expiration date from the JWT token
     *
     * @param token the JWT token
     * @return the expiration date of the token
     */
	public Date extractExpiration(String token) {
		
		return extractClaim(token, Claims::getExpiration);
		
	}
	
	
	
	/**
     * Method extracts a specific claim (then claim desired, e.g getSubject) 
     * from the JWT token using the provided claims resolver function
     *
     * @param <T> the type of the claim
     * @param token the JWT token
     * @param claimsResolver a function to resolve the claim
     * @return the resolved claim
     */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		
		final Claims claims = extractAllClaims(token);
		
		return claimsResolver.apply(claims);
		
	}
	
	
	/**
     * Method parses the JWT token to extract all claims
     *
     * @param token the JWT token
     * @return the claims extracted from the token
     */
	public Claims extractAllClaims(String token) {
		 return Jwts.parser()
                 .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                 .parseClaimsJws(token)
                 .getBody();
	}
	
	
	/**
     * Method checks if the JWT token has expired
     *
     * @param token the JWT token
     * @return true if the token has expired, false otherwise
     */
	private Boolean isTokenExpired(String token) {
		
		return extractExpiration(token).before(new Date());
	}
	
	
	/**
     * Method generates a new JWT token for the given username
     *
     * @param username the username for which the token is generated
     * @return the generated JWT token
     */
	 public String generateToken(String username) {
		 
	        return createToken(username);
	    }
	 
	 
	 /**
	 * Method creates a JWT token for the given username
	 *
	 * @param username the username for which the token is created
	 * @return the created JWT token
	 */
	public String createToken(String username) {
		
		 return Jwts.builder()
                 .setSubject(username)
                 .setIssuedAt(new Date(System.currentTimeMillis()))
                 .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                 .signWith(SignatureAlgorithm.HS256, DatatypeConverter.parseBase64Binary(SECRET_KEY))
                 .compact();
	}
	
	
	/**
     * Method validates the JWT token by checking if the username matches and if the token has not expired
     *
     * @param token the JWT token
     * @param username the username to validate against
     * @return true if the token is valid, false otherwise
     */
	public Boolean validateToken(String token, String username) {
		
		final String extractedUsername = extractUsername(token);
		return (extractedUsername.equals(username) && !isTokenExpired(token));
		
	}
}
