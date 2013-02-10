package org.hannes.rs2.net.codec.decoder;

import org.hannes.Main;
import org.hannes.rs2.content.event.ObjectInteractionEvent;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.codec.Decoder;
import org.hannes.util.Location;

public class ObjectInteractionDecoder implements Decoder {

	/**
	 * The first object option
	 */
	public static final int FIRST_OPTION = 132;

	/**
	 * The second object option
	 */
	public static final int SECOND_OPTION = 252;
	
	/**
	 * The third object option
	 */
	public static final int THIRD_OPTION = 70;

	@Override
	public void decode(Message message, Connection connection) throws Exception {
		int x = message.getShort();
		int y = message.getShort();
		int id = message.getShort();
		int option = message.getOpcode() == FIRST_OPTION ? 1 : message
				.getOpcode() == SECOND_OPTION ? 2 : 3;
		Main.getEventhub().offer(new ObjectInteractionEvent(connection.getPlayer(), id, new Location(x, y), option));
	}
	
}