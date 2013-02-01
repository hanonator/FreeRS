package org.hannes.util;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Graham Edgecombe
 */
public class ChannelBufferUtils {

	/**
	 * Reads a string n stuff
	 * 
	 * @param payload
	 * @return
	 */
	public static String readRS2String(ChannelBuffer payload) {
		StringBuilder bldr = new StringBuilder();
		byte b;
		while(payload.readable() && (b = payload.readByte()) != 10) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}

	/**
	 * Write a string to a buffer
	 * 
	 * @param string
	 * @param buffer
	 */
	public static void writeRS2String(String string, ChannelBuffer buffer) {
		buffer.writeBytes(string.getBytes());
		buffer.writeByte(10);
	}

}