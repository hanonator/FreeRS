package org.hannes.rs2.net.codec.decoder;

import org.hannes.Main;
import org.hannes.rs2.content.event.ChannelTextEvent;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.codec.Decoder;
import org.hannes.util.ChannelBufferUtils;

public class ChannelTextDecoder implements Decoder {

	/**
	 * The opcode
	 */
	public static final int OPCODE = 103;

	@Override
	public void decode(Message message, Connection connection) throws Exception {
		String text = ChannelBufferUtils.readRS2String(message.getBuffer());
		Main.getEventhub().offer(new ChannelTextEvent(text, connection.getPlayer()));
	}

}