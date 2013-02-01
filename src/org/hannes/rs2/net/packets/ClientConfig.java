package org.hannes.rs2.net.packets;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.MessageBuilder;
import org.hannes.rs2.net.Packet;

public class ClientConfig implements Packet {

	/**
	 * The value of the configuration setting
	 */
	private final int key;
	
	/**
	 * The value of the configuration setting
	 */
	private final int value;

	public ClientConfig(int key, int value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public Message build(Connection connection) {
		return value > Byte.MAX_VALUE || value < Byte.MIN_VALUE ?
				new MessageBuilder(86).putShort((short) key).putInt(value).build() :
				new MessageBuilder(36).putShort((short) key).put((byte) value).build();
	}

}