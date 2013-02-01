package org.hannes.rs2.net;

import org.hannes.util.ChannelBufferUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * The class to create messages
 * 
 * @author goku
 */
public class MessageBuilder {

	/**
	 * Bit mask out
	 */
	public static final int[] BIT_MASK_OUT = new int[32];
	
	static {
		for(int i = 0; i < BIT_MASK_OUT.length; i++) {
			BIT_MASK_OUT[i] = (1 << i) - 1;
		}
	}

	/**
	 * The opcode. -1 if the data is to be sent without a header
	 */
	private final int opcode;

	/**
	 * The buffer
	 */
	private final ChannelBuffer buffer;
	
	/**
	 * Header type thingy
	 */
	private final MessageLength type;

	/**
	 * Current bit position
	 */
	private int bitPosition;

	/**
	 * Creates a new messagebuilder with opcode -1, default size of 256
	 * and a fixed headertype
	 * 
	 * @param opcode
	 */
	public MessageBuilder() {
		this(-1, ChannelBuffers.buffer(256), MessageLength.FIXED);
	}

	/**
	 * Creates a new messagebuilder with a given opcode, default size of 256
	 * and fixed headertype
	 * 
	 * @param opcode
	 */
	public MessageBuilder(int opcode) {
		this(opcode, ChannelBuffers.buffer(256), MessageLength.FIXED);
	}

	/**
	 * Creates a new messagebuilder with a given buffer and opcode and fixed headertype
	 * 
	 * @param opcode
	 * @param buffer
	 */
	public MessageBuilder(int opcode, ChannelBuffer buffer) {
		this(opcode, buffer, MessageLength.FIXED);
	}

	/**
	 * Creates a new messagebuilder with a default buffer with size 256
	 * 
	 * and an opcode and a given type
	 * @param opcode
	 * @param buffer
	 */
	public MessageBuilder(int opcode, MessageLength length) {
		this(opcode, ChannelBuffers.buffer(2048), length);
	}

	/**
	 * Creates a MessageBuilder with a given opcode, buffer and headertype
	 * 
	 * @param opcode
	 * @param buffer
	 * @param type
	 */
	public MessageBuilder(int opcode, ChannelBuffer buffer, MessageLength type) {
		this.opcode = opcode;
		this.buffer = buffer;
		this.type = type;
	}

	/**
	 * Start the bit access and set the bitposition
	 * 
	 * @return
	 */
	public MessageBuilder startBitAccess() {
		bitPosition = buffer.writerIndex() * 8;
		return this;
	}
	
	/**
	 * Finish bit access
	 * 
	 * @return
	 */
	public MessageBuilder finishBitAccess() {
		buffer.writerIndex((bitPosition + 7) / 8);
		return this;
	}

	/**
	 * Write a specific amount of bits to the buffer
	 * 
	 * @param numBits
	 * @param value
	 * @return
	 */
	public MessageBuilder putBits(int numBits, int value) {
		int bytes = (int) Math.ceil((double) numBits / 8D) + 1;
		int position = (bitPosition + 7) / 8;

		buffer.ensureWritableBytes(position + bytes);
		buffer.writerIndex(position);
		
		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += numBits;
		
		for (; numBits > bitOffset; bitOffset = 8) {
			byte b = buffer.getByte(bytePos);
			buffer.setByte(bytePos, (byte) (b & ~BIT_MASK_OUT[bitOffset]));
			buffer.setByte(bytePos, (byte) (b | (value >> (numBits - bitOffset)) & BIT_MASK_OUT[bitOffset]));
			bytePos++;
			numBits -= bitOffset;
		}
		byte b = buffer.getByte(bytePos);
		if (numBits == bitOffset) {
			buffer.setByte(bytePos, (byte) (b & ~BIT_MASK_OUT[bitOffset]));
			buffer.setByte(bytePos, (byte) (b | value & BIT_MASK_OUT[bitOffset]));
		} else {
			buffer.setByte(bytePos, (byte) (b & ~(BIT_MASK_OUT[numBits] << (bitOffset - numBits))));
			buffer.setByte(bytePos, (byte) (b | (value & BIT_MASK_OUT[numBits]) << (bitOffset - numBits)));
		}
		return this;
	}

	/**
	 * Writes the contents of a message
	 * 
	 * @throws IllegalArgumentException if message is not raw data
	 * @param message
	 * @return
	 */
	public MessageBuilder put(Message message) {
		if (!message.isRaw()) {
			throw new IllegalArgumentException("message is not raw");
		}
		buffer.writeBytes(message.getBuffer());
		return this;
	}

	/**
	 * Write a byte to the buffer
	 * 
	 * @param b
	 * @return
	 */
	public MessageBuilder put(int b) {
		buffer.writeByte(b);
		return this;
	}

	/**
	 * Write a series of bytes to the buffer
	 * 
	 * @param b
	 * @return
	 */
	public MessageBuilder put(byte[] b) {
		buffer.writeBytes(b);
		return this;
	}
	
	/**
	 * Write a short to the buffer
	 * @param s
	 * @return
	 */
	public MessageBuilder putShort(int s) {
		buffer.writeShort(s);
		return this;
	}

	/**
	 * Write an int to the buffer
	 * 
	 * @param i
	 * @return
	 */
	public MessageBuilder putInt(int i) {
		buffer.writeInt(i);
		return this;
	}
	
	/**
	 * Write a long to the buffer
	 * @param l
	 * @return
	 */
	public MessageBuilder putLong(long l) {
		buffer.writeLong(l);
		return this;
	}

	public MessageBuilder putString(String string) {
		ChannelBufferUtils.writeRS2String(string, buffer);
		return this;
	}
	
	public int getOpcode() {
		return opcode;
	}

	public Message build() {
		return new Message(opcode, buffer.readableBytes(), buffer, type);
	}

	public MessageLength getType() {
		return type;
	}

	public boolean isEmpty() {
		return buffer.readableBytes() == 0;
	}

	public void putSmart(int i) {
		if (i >= Byte.MAX_VALUE - 3) {
			putShort((short) i);
		} else {
			put((byte) i);
		}
	}

}