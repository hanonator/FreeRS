package org.hannes.rs2.net.codec.decoder;

import org.hannes.rs2.entity.Player;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.codec.Decoder;
import org.hannes.rs2.util.Cooldowns.Cooldown;
import org.hannes.util.Location;

/**
 * Decodes the player's movement
 * 
 * @author red
 */
public class MovementDecoder implements Decoder {
	
	/**
	 * The opcode
	 */
	public static final int PRIMARY_OPCODE = 98;
	
	/**
	 * The opcode
	 */
	public static final int SECONDARY_OPCODE = 164;
	
	/**
	 * The opcode
	 */
	public static final int TERNARY_OPCODE = 248;

	@Override
	public void decode(Message message, Connection connection) throws Exception {
		Player player = connection.getPlayer();
		
		/*
		 * Get the size of the message
		 */
		int size = message.getOpcode() == 248 ? message.size() - 14 : message.size();
		
		if (!player.getCooldowns().check(Cooldown.MOVEMENT)) {
			return;
		}
		
		/*
		 * Clear the player's action queue n shit
		 */
		player.getActionQueue().clear();
		
		/*
		 * Reset the player's walking queue
		 */
		player.getWalkingQueue().reset();
		
		/*
		 * Reset the player's acquaintance
		 */
		if (player.getAcquaintance() != null) {
			player.setAcquaintance(null);
		}

		/*
		 * Make the player walk or run depending on the event
		 */
		player.getWalkingQueue().setRunningQueue(message.get() == 1);

		/*
		 * n = data.length / 2 where n is the amount of steps
		 */
		final int steps = (size - 5) / 2;
		
		/*
		 * The initial location
		 */
		Location location = new Location(message.getShort(), message.getShort());

		/*
		 * Gather dat path from dem message
		 */
		final int[][] path = new int[steps][2];
		for (int i = 0; i < steps; i++) {
			path[i][0] = message.get();
			path[i][1] = message.get();
		}
		
		/*
		 * Add everything to the walking queue
		 */
		player.getWalkingQueue().addStep(location.getX(), location.getY());
		for (int i = 0; i < steps; i++) {
			path[i][0] += location.getX();
			path[i][1] += location.getY();
			player.getWalkingQueue().addStep(path[i][0], path[i][1]);
		}
		
		/*
		 * Finish the walking queue, otherwise there will be a full cycle delay
		 * between the request and the client
		 */
		player.getWalkingQueue().finish();
	}

}