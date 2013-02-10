package org.hannes.rs2.content.channel;

import java.util.ArrayList;
import java.util.List;

import org.hannes.rs2.entity.Player;

public class Channel {

	/**
	 * 
	 */
	private final String name;
	
	/**
	 * 
	 */
	private final List<Player> players = new ArrayList<>();

	public Channel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<Player> getPlayers() {
		return players;
	}

}