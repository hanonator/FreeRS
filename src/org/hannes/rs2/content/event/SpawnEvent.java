package org.hannes.rs2.content.event;

import org.hannes.rs2.entity.Player;

public class SpawnEvent {

	/**
	 * The player to spawn
	 */
	private final Player player;

	public SpawnEvent(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

}