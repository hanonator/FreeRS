package org.hannes.rs2.content.channel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hannes.rs2.entity.Player;
import org.hannes.rs2.net.packets.TextMessage;

public class Channel {

	/**
	 * The name of the channel
	 */
	private final String name;
	
	/**
	 * The players currently in the chat
	 */
	private final List<Player> players = new ArrayList<>();
	
	/**
	 * The creator of the channel
	 */
	private final Player creator;

	public Channel(String name, Player creator) {
		this.name = name;
		this.creator = creator;
	}

	public void send(TextMessage message) {
		for (Player player : players) {
			player.write(message);
		}
	}

	public void register(Player player) {
		players.add(player);
		player.write(new TextMessage("You are now talking in #" + name + "."));
		if (creator != null) {
			player.write(new TextMessage("The creator and moderator of this channel is " + creator.getUsername() + "."));
		} else {
			player.write(new TextMessage("This is a public channel. It is only moderated by player-moderators."));
		}
	}

	public void remove(Player player) {
		players.remove(player);
		player.write(new TextMessage("You have left " + name + "."));
	}

	public void destroy() {
		for (Iterator<Player> iterator = players.iterator(); iterator.hasNext(); ) {
			Player player = iterator.next();
			player.write(new TextMessage("You have been disconnected from #" + name + "."));
			iterator.remove();
		}
	}

	public void kick(String username) {
		if (username.equalsIgnoreCase(creator.getUsername())) {
			creator.write(new TextMessage("You can't kick yourself"));
		} else {
			for (Iterator<Player> iterator = players.iterator(); iterator.hasNext(); ) {
				Player player = iterator.next();
				if (player.getUsername().equalsIgnoreCase(username)) {
					player.write(new TextMessage("You have been kicked from " + name + "."));
					iterator.remove();
				}
			}
		}
	}
	
	public String getName() {
		return name;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public Player getCreator() {
		return creator;
	}

}