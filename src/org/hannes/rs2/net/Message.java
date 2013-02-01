package org.hannes.rs2.net;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * A read only buffer containing several functions for use with the RS2 protocol in the 289 client
 * 
 * @author goku
 */
public class Message {

	/**
	 * The buffer
	 */
	private final ChannelBuffer buffer;
	
	/**
	 * The message's opcode. -1 if the packet is raw data
	 */
	private final int opcode;
	
	/**
	 * The length of the buffer
	 */
	private final int size;
	
	/**
	 * Header type thingy
	 */
	private final MessageLength length;

	/**
	 * Create a message with a specific opcode, length and payload
	 * 
	 * @param opcode
	 * @param size
	 * @param buffer
	 */
	public Message(int opcode, int size, ChannelBuffer buffer, MessageLength length) {
		this.buffer = buffer;
		this.opcode = opcode;
		this.size = size;
		this.length = length;
	}

	/**
	 * Create a message without an opcode and a given length and buffer
	 * 
	 * @param length
	 * @param buffer
	 */
	public Message(int opcode, int length, ChannelBuffer buffer) {
		this(opcode, length, buffer, MessageLength.FIXED);
	}

	/**
	 * Create a message without an opcode and a given length and buffer
	 * 
	 * @param length
	 * @param buffer
	 */
	public Message(int length, ChannelBuffer buffer) {
		this(-1, length, buffer, MessageLength.FIXED);
	}

	public byte get() {
		return buffer.readByte();
	}

	public short getShort() {
		return buffer.readShort();
	}

	public int getInt() {
		return buffer.readInt();
	}

	public long getLong() {
		return buffer.readLong();
	}

	public void get(byte[] data, int off, int len) {
		buffer.readBytes(data, off, len);
	}

	public int remaining() {
		return buffer.readableBytes();
	}

	public int getOpcode() {
		return opcode;
	}

	public int size() {
		return size;
	}

	public boolean isRaw() {
		return opcode == -1;
	}

	public ChannelBuffer getBuffer() {
		return buffer;
	}

	public String toString() {
		return super.toString() + " [" + opcode + "," + size + "]";
	}

	public MessageLength getType() {
		return length;
	}

}