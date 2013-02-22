package org.hannes.rs2.net.codec.decoder;

import org.hannes.Main;
import org.hannes.rs2.content.event.UserValueEvent;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.codec.Decoder;

public class UserValueDecoder implements Decoder {
	
	/**
	 * The opcode
	 */
	public static final int OPCODE = 208;

	@Override
	public void decode(Message message, Connection connection) throws Exception {
		Main.getEventhub().offer(new UserValueEvent(message.getInt(), connection.getPlayer()));
	}

}