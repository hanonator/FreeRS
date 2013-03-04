package org.hannes.rs2.content.channel;

import java.util.HashMap;
import java.util.Map;

import org.hannes.rs2.entity.Player;

public class ChannelManager {

	/**
	 * The collection of channels
	 */
	private static final Map<String, Channel> channels = new HashMap<>();

	static {
		create("all", null);
	}

	public static Channel get(String name) {
		return channels.get(name);
	}

	public static boolean exists(String name) {
		return channels.containsKey(name);
	}

	public static Channel register(Channel channel) {
		channels.put(channel.getName(), channel);
		return channel;
	}

	public static void delete(String name) {
		channels.remove(name);
	}

	public static Channel create(String name, Player player) {
		return register(new Channel(name, player));
	}

}