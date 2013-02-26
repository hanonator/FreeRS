package org.hannes.rs2.content.event;

import org.hannes.rs2.container.Item;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.entity.RSObject;

public class ItemOnObjectEvent {

	/**
	 * The id of the object
	 */
	private final RSObject object;

	/**
	 * The item being used
	 */
	private final Item item;
	
	/**
	 * The player
	 */
	private final Player player;

	public ItemOnObjectEvent(RSObject object, Item item, Player player) {
		this.object = object;
		this.item = item;
		this.player = player;
	}

	public RSObject getObject() {
		return object;
	}

	public Item getItem() {
		return item;
	}

	public Player getPlayer() {
		return player;
	}

}