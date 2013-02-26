package org.hannes.rs2.net.packets;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.MessageBuilder;
import org.hannes.rs2.net.MessageLength;
import org.hannes.rs2.net.Serializable;

public class TextMessage implements Serializable {

	private final String text;

	public TextMessage(String text) {
		this.text = text;
	}

	public TextMessage(Object obj) {
		this(obj.toString());
	}

	@Override
	public Message serialize(Connection connection) {
		return new MessageBuilder(253, MessageLength.VARIABLE_8_BIT).putString(text).build();
	}

}