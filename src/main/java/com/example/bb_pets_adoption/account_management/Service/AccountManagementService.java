/**
 * 
 */
package com.example.bb_pets_adoption.account_management.Service;

import org.springframework.stereotype.Service;

import com.example.bb_pets_adoption.account_management.Model.User;

/**
 * Service class to define operations related to user account management
 */
@Service
public interface AccountManagementService {

	/**
	 * Method to update personal details
	 * 
	 * @param {User} user - the user obejct to be updated
	 * @param {String} name - user firts name
	 * @param {String} lastName - user last name
	 * @param {String} location- user location (County)
	 * */
	public void updateProfile(User user, String name, String lastName, String location) throws Exception;
	
	
	/**
	 * Method to the email address
	 * 
	 * @param {User} user - the user object to be updated
	 * @param {String} email - the email address to be updated
	 * */
	public void updateEmailAddress(User user, String email) throws Exception;
	

	/**
	 * Method to authenticate user by token
	 * 
	 * @param {String} token - the current session token authentication
	 * */
	public boolean authenticateUserByToken(String token);
	
	/**
	 * 
	 * **/
	public boolean getUserByToken(String token);
	
	
	/**
	 * Method delete user from db
	 * 
	 * @param {User} user - the user object to be deleted
	 * */
	public void deleteUser(User user)  throws Exception;
}
