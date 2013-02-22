package org.hannes.rs2.content.event;

import org.hannes.rs2.entity.Player;

public class MakeEvent {
	
	/**
	 * the player
	 */
	private final Player player;
	
	/**
	 * The slot of the item to be produced in the interface
	 */
	private final int slot;
	
	/**
	 * Amount of items to be produced
	 */
	private final int amount;

	public MakeEvent(Player player, int slot, int amount) {
		this.player = player;
		this.slot = slot;
		this.amount = amount;
	}

	public Player getPlayer() {
		return player;
	}

	public int getSlot() {
		return slot;
	}

	public int getAmount() {
		return amount;
	}
	
}