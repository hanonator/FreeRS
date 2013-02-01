package org.hannes.rs2.net;

public interface Packet {

	/**
	 * Constructs a message
	 * 
	 * @param connection
	 * @return
	 */
	public abstract Message build(Connection connection);

}