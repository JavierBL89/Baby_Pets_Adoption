/**
 * 
 */
package com.example.bb_pets_adoption.auth.service;

import org.springframework.stereotype.Service;

/**
 * 
 */
@Service
public interface AuthenticationService {

	/***
	 * 
	 * */
	public boolean authenticate(String token);
}
