package org.hannes.rs2.net.codec.decoder;

import org.hannes.Main;
import org.hannes.rs2.World;
import org.hannes.rs2.content.event.NPCInteractionEvent;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.codec.Decoder;

public class NPCInteractionDecoder implements Decoder {
	
	/**
	 * The attack interaction option
	 */
	public static final int ATTACK = 72;
	
	/**
	 * Interaction with the player's spell
	 */
	public static final int MAGIC = 131;
	
	/**
	 * The first interaction option
	 */
	public static final int FIRST_OPTION = 155;
	
	/**
	 * The second interaction option
	 */
	public static final int SECOND_OPTION = 17;
	
	/**
	 * The third interaction option
	 */
	public static final int THIRD_OPTION = 21;

	@Override
	public void decode(Message message, Connection connection) throws Exception {
		NPCInteractionEvent event = new NPCInteractionEvent(
				connection.getPlayer(), message.getOpcode());
		
		switch (message.getOpcode()) {
		case ATTACK:
		case FIRST_OPTION:
		case SECOND_OPTION:
		case THIRD_OPTION:
			event.setNpc(World.getWorld().getNpc(message.getShort()));
			break;
		case MAGIC:
			event.setNpc(World.getWorld().getNpc(message.getShort()));
			System.out.println("test");
			break;
		}
		Main.getEventhub().offer(event);
	}

}