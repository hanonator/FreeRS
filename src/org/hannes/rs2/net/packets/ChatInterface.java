package org.hannes.rs2.net.packets;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.MessageBuilder;
import org.hannes.rs2.net.Serializable;

public class ChatInterface implements Serializable {

	/**
	 * The interface id
	 */
	private final int id;

	public ChatInterface(int id) {
		this.id = id;
	}

	@Override
	public Message serialize(Connection connection) {
		return new MessageBuilder(164).putShort(id).build();
	}

}