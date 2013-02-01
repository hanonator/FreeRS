package org.hannes.rs2.util;


/**
 * The return code for login. ReturnCode is RSPS slang for the first byte
 * in the login response
 * 
 * @author red
 *
 */
public enum ReturnCode {
	
	/**
	 * dummy
	 */
	NULL,
	
	/**
	 * 2 minute login timer
	 */
	TWO_MINUTE_DELAY,
	
	/**
	 * Successful login
	 */
	SUCCESS,
	
	/**
	 * Invalid details
	 */
	INVALID_DETAILS,
	
	/**
	 * When a user has been banned/disabled
	 */
	ACCOUNT_DISABLED,
	
	/**
	 * When an account is already logged in
	 */
	ALREADY_LOGGED_IN,
	
	/**
	 * When the server has been updated (preferably when client version ID != server version ID)
	 */
	UPDATE,
	
	/**
	 * When the user cap has been reached
	 */
	WORLD_FULL,
			
	/**
	 * When the login server is down
	 */
	LOGIN_SERVER_DOWN,
	
	/**
	 * When the player is sending too many connections
	 */
	TOO_MANY_CONNECTIONS,
	
	/**
	 * Bad session id
	 */
	BAD_SESSION_ID,
			
	/**
	 * When the login server rejected the login attempt
	 */
	LOGIN_SERVER_REJECT,
	
	/**
	 * When the player needs premium in order to login
	 */
	PREMIUM_REQUIRED,
	
	/**
	 * When the login could not be completed
	 */
	LOGIN_INCOMPLETE,
	
	/**
	 * When the server is being updated
	 */
	SERVER_UPDATING,
	
	/**
	 * No idea
	 */
	UNKNOWN_1,
	
	/**
	 * Too many login attempts have been made
	 */
	LOGIN_TIME_OUT,
	
	/**
	 * When the user is standing on a premium only location
	 */
	INVALID_LOCATION,
	
	/**
	 * No idea
	 */
	UNKNOWN_2,
	
	/**
	 * No idea
	 */
	UNKNOWN_3,
	
	/**
	 * Invalid login server
	 */
	INVALID_LOGIN_SERVER,
	
	/**
	 * Login after x amount of time
	 */
	TRANSFER_AFTER_X;
	
}