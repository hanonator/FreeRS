package org.hannes.rs2.net.codec;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;

public interface Decoder {

	/**
	 * 
	 * @param connection
	 * @param message
	 */
	public abstract void decode(Message message, Connection connection) throws Exception;

}