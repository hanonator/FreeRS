package org.hannes.rs2.content.event;

import org.hannes.rs2.entity.Player;

public class ChannelTextEvent {

	/**
	 * The string entered
	 */
	private final String string;
	
	/**
	 * The player
	 */
	private final Player player;

	public ChannelTextEvent(String string, Player player) {
		this.string = string;
		this.player = player;
	}

	public String getText() {
		return string;
	}

	public Player getPlayer() {
		return player;
	}

}