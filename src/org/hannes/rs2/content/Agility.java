package org.hannes.rs2.content;

import org.hannes.rs2.content.event.ObjectInteractionEvent;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.entity.sync.Animation;
import org.hannes.rs2.entity.sync.ForcedMovement;
import org.hannes.rs2.event.EventHandler;
import org.hannes.rs2.util.Direction;
import org.hannes.rs2.util.Cooldowns.Cooldown;
import org.hannes.util.Location;

/**
 * ugliest class imo
 * 
 * @author red
 *
 */
public class Agility implements EventHandler<ObjectInteractionEvent> {

	/**
	 * The object id of the stepping stones in Draynor
	 */
	private static final int STEPPING_STONE_ID = 9315;

	@Override
	public void handleEvent(ObjectInteractionEvent event) throws Exception {
		// TODO: Check if object exists
		final Player player = event.getPlayer();
		
		/*
		 * Agility is affected by mobility cooldowns
		 */
		if (!player.getCooldowns().check(Cooldown.MOVEMENT)) {
			return;
		}
		
		switch (event.getId()) {
		case STEPPING_STONE_ID:
			/*
			 * Don't do anything if the player is already standing on the
			 * stepping stone
			 */
			if (player.getLocation().equals(event.getLocation())) {
				return;
			}
			
			/*
			 * Get both the source and destination locations
			 */
			Location source = new Location(event.getLocation());
			Location destination = new Location(event.getLocation());
			source.transform(player.getLocation().getX() > destination.getX() ? 1 : -1, 0, 0);
			
			/*
			 * Turn the player towards the stone
			 */
			player.setViewLocation(destination);
			
			/*
			 * Move the player
			 */
			player.move(new ForcedMovement(source, destination, 20, 40, source.getX() < destination.getX() ? Direction.NORTH : Direction.WEST), new Animation(769));
			break;
		}
	}

}