package org.hannes.rs2.content;

import org.hannes.rs2.action.Action;
import org.hannes.rs2.content.event.ObjectInteractionEvent;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.entity.sync.Animation;
import org.hannes.rs2.entity.sync.ForcedMovement;
import org.hannes.rs2.entity.sync.UpdateFlags.UpdateFlag;
import org.hannes.rs2.event.EventHandler;
import org.hannes.rs2.util.Direction;
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
		final Player player = event.getPlayer();
		
		switch (event.getId()) {
		case STEPPING_STONE_ID:
			// TODO: Check if object exists
			
			/*
			 * Get the source location
			 */
			int src_x, src_y = event.getLocation().getY();
			int x = event.getPlayer().getLocation().getX();
			if (x > event.getLocation().getX()) {
				src_x = event.getLocation().getX() + 1;
			} else if (x < event.getLocation().getX()) {
				src_x = event.getLocation().getX() - 1;
			} else {
				return;
			}
			
			/*
			 * Rotate the player to face where he's going
			 */
			event.getPlayer().setViewLocation(event.getLocation());
			
			/*
			 * Move the player
			 */
			player.setAnimation(new Animation(769));
			
			/*
			 * Create the locations
			 */
			final Location source = new Location(src_x, src_y);
			final Location destination = new Location(event.getLocation());
			
			
			/*
			 * Add the action
			 */
			event.getPlayer().getActionQueue().offer(new Action(event.getPlayer()) {

				private boolean flag;
				
				@Override
				public boolean doAction(Player player) throws Exception {
					if (flag) {
						destination.delocalize(player);
						player.setTeleportTarget(destination);
						return true;
					} else {
						source.localize(player);
						destination.localize(player);
						
						/*
						 * Create the forced movement object
						 */
						player.setForcedMovement(new ForcedMovement(
								source, destination, 10, Direction.EAST));
						
						/*
						 * Set the forced movement flag
						 */
						player.getUpdateFlags().flag(UpdateFlag.FORCE_WALK);
						flag = true;
						return false;
					}
				}
				
			});
			break;
		}
	}

}