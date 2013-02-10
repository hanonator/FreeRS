package org.hannes.rs2.content.event;

import org.hannes.rs2.container.Item;
import org.hannes.rs2.entity.Player;

public class ItemOnItemEvent {
	
	/**
	 * the player
	 */
	private final Player player;

	/**
	 * The slots
	 */
	private final int[] slot;
	
	/**
	 * The items
	 */
	private final Item[] items;

	public ItemOnItemEvent(Player player, int[] slot, Item[] items) {
		this.player = player;
		this.slot = slot;
		this.items = items;
	}

	public Player getPlayer() {
		return player;
	}

	public int[] getSlot() {
		return slot;
	}

	public Item[] getItems() {
		return items;
	}

}