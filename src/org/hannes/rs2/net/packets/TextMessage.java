package org.hannes.rs2.net.packets;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.MessageBuilder;
import org.hannes.rs2.net.MessageLength;
import org.hannes.rs2.net.Packet;

public class TextMessage implements Packet {

	private final String text;

	public TextMessage(String text) {
		this.text = text;
	}

	@Override
	public Message build(Connection connection) {
		return new MessageBuilder(253, MessageLength.VARIABLE_8_BIT).putString(text).build();
	}

}