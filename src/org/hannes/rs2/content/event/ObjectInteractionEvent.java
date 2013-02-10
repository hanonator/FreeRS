package org.hannes.rs2.content.event;

import org.hannes.rs2.entity.Player;
import org.hannes.util.Location;

public class ObjectInteractionEvent {

	/**
	 * The player who clicked the button
	 */
	private final Player player;
	
	/**
	 * The object id
	 */
	private final int id;
	
	/**
	 * The location of the object
	 */
	private final Location location;
	
	/**
	 * The option that has been chosen
	 */
	private final int option;

	public ObjectInteractionEvent(Player player, int id, Location location, int option) {
		this.player = player;
		this.id = id;
		this.location = location;
		this.option = option;
	}

	public Player getPlayer() {
		return player;
	}

	public int getId() {
		return id;
	}

	public Location getLocation() {
		return location;
	}

	public int getOption() {
		return option;
	}

}