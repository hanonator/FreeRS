package org.hannes.rs2.net.codec.decoder;

import org.hannes.Main;
import org.hannes.rs2.content.event.ButtonEvent;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.codec.Decoder;

/**
 * Decodes buttons n shit
 * 
 * @author red
 */
public class ButtonDecoder implements Decoder {

	@Override
	public void decode(Message message, Connection connection) throws Exception {
		Main.getEventhub().offer(new ButtonEvent(connection.getPlayer(), message.getShort()));
	}

}