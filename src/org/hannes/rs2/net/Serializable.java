package org.hannes.rs2.net;

public interface Serializable {

	/**
	 * Constructs a message
	 * 
	 * @param connection
	 * @return
	 */
	public abstract Message serialize(Connection connection);

}