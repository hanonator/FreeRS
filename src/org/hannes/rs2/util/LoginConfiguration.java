package org.hannes.rs2.util;

import org.hannes.rs2.net.Connection;

public class LoginConfiguration {
	
	/**
	 * The connection
	 */
	private final Connection connection;

	/**
	 * The username
	 */
	private String username;
	
	/**
	 * The password
	 */
	private String password;
	
	/**
	 * The user id
	 */
	private int uid;
	
	/**
	 * Indicates 
	 */
	private boolean reconnect;
	
	/**
	 * The version of the client
	 */
	private int version;
	
	/**
	 * Memory usage (primarily used for sound/music)
	 */
	private MemoryUsage memoryUsage;
	
	/**
	 * The return code
	 */
	private ReturnCode returnCode;

	public LoginConfiguration(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	 * @param reconnect the reconnect to set
	 */
	public void setReconnect(boolean reconnect) {
		this.reconnect = reconnect;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @param memoryUsage the memoryUsage to set
	 */
	public void setMemoryUsage(MemoryUsage memoryUsage) {
		this.memoryUsage = memoryUsage;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the connecton
	 */
	public Connection getConnecton() {
		return connection;
	}

	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * @return the reconnect
	 */
	public boolean isReconnect() {
		return reconnect;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @return the memoryUsage
	 */
	public MemoryUsage getMemoryUsage() {
		return memoryUsage;
	}

	public ReturnCode getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(ReturnCode returnCode) {
		this.returnCode = returnCode;
	}

}