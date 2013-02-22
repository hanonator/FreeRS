package org.hannes.rs2.util;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.MessageBuilder;
import org.hannes.rs2.net.Serializable;

public class ValuePrompt implements Serializable {

	@Override
	public Message serialize(Connection connection) {
		return new MessageBuilder(27).build();
	}

}