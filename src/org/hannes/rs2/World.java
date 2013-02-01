package org.hannes.rs2;

import java.util.List;

import org.hannes.rs2.entity.NPC;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.util.EntityList;

public class World {

	/**
	 * The global user cap
	 */
	private static final int USER_CAP = 75;

	/**
	 * The world, should do more cool singleton, but w/e
	 */
	private static final World world = new World();

	/**
	 * The player list
	 */
	private final List<Player> players = new EntityList<>(USER_CAP);

	/**
	 * The player list
	 */
	private final List<NPC> npcs = new EntityList<>(2048);

	/**
	 * destroys the player object
	 * 
	 * @param player
	 */
	public void destroy(Player player) {
		players.remove(player.getIndex());
	}

	/**
	 * Allocate a new player object with a free index
	 * 
	 * @return
	 */
	public Player allocatePlayer(Connection connection) {
		for (int i = 0; i < players.size(); i++) {
			if (!players.contains(i)) {
				return new Player(i, connection);
			}
		}
		return null;
	}

	/**
	 * Allocate a new npc object with a free index
	 * 
	 * @return
	 */
	public NPC allocateNPC() {
		for (int i = 0; i < npcs.size(); i++) {
			if (!npcs.contains(i)) {
				return new NPC(i);
			}
		}
		return null;
	}

	/**
	 * Registers the player into the list
	 * 
	 * @param player
	 */
	public void register(Player player) throws Exception {
		if (players.get(player.getIndex()) != null) {
			throw new IllegalStateException("cannot register player");
		}
		players.add(player);
		player.getConnection().setPlayer(player);
	}

	/**
	 * Register an NPC
	 * @param npc
	 */
	public void register(NPC npc) {
		if (npcs.get(npc.getIndex()) != null) {
			throw new IllegalStateException("cannot register npc");
		}
		npcs.add(npc);
	}
	
	public static World getWorld() {
		return world;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<NPC> getNPCs() {
		return npcs;
	}

}