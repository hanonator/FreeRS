package org.hannes.rs2.content.event;

import org.hannes.rs2.entity.Player;

public class UserValueEvent {

	/**
	 * The value entered
	 */
	private final int value;
	
	/**
	 * The user that entered the value
	 */
	private final Player player;

	public UserValueEvent(int value, Player player) {
		this.value = value;
		this.player = player;
	}

	public int getValue() {
		return value;
	}

	public Player getPlayer() {
		return player;
	}

}