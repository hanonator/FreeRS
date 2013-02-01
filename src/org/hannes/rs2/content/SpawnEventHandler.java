package org.hannes.rs2.content;

import org.hannes.rs2.content.event.SpawnEvent;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.event.EventHandler;
import org.hannes.rs2.net.packets.SidebarInterface;
import org.hannes.util.Location;

/**
 * Handles the player's spawn request
 * 
 * @author red
 */
public class SpawnEventHandler implements EventHandler<SpawnEvent> {

	@Override
	public void handleEvent(SpawnEvent event) throws Exception {
		Player player = event.getPlayer();
		
		/*
		 * Reset the player's gear
		 */
		player.getInventory().clear();
		player.getEquipment().clear();
		
		/*
		 * Set the player's equipment tab
		 */
		player.getEquipment().refresh();
		
		/*
		 * Refresh the player's inventory
		 */
		player.getInventory().refresh();
		player.getConnection().write(SidebarInterface.INVENTORY);
		
		/*
		 * Teleport the player to his spawn point
		 */
		player.setTeleportTarget(Location.DEFAULT_LOCATION);
	}

}