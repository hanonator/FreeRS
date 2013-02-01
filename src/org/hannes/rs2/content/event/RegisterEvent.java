package org.hannes.rs2.content.event;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.util.LoginConfiguration;

public class RegisterEvent {

	/**
	 * The connection to register
	 */
	private final Connection connection;
	
	/**
	 * The login configuration
	 */
	private final LoginConfiguration configuration;

	public RegisterEvent(Connection connection, LoginConfiguration configuration) {
		this.connection = connection;
		this.configuration = configuration;
	}

	public Connection getConnection() {
		return connection;
	}

	public LoginConfiguration getConfiguration() {
		return configuration;
	}

}