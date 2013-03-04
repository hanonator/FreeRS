package org.hannes.rs2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.dom4j.Document;
import org.dom4j.Element;
import org.hannes.rs2.entity.NPC;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.util.EntityList;
import org.hannes.rs2.util.SpawnArea;
import org.hannes.util.Location;

public class World {

	/**
	 * The random generator for generating randoms lol
	 */
	private static final Random RANDOM_GENERATOR = new Random();

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
	 * The spawn areas
	 */
	private final List<SpawnArea> areas = new ArrayList<>();

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

	public NPC getNpc(int index) {
		return npcs.get(index);
	}

	/**
	 * Initializes the wandering NPC spawns
	 * 
	 * @throws Exception
	 */
	public void initialize(Document document) throws Exception {
		/*
		 * Parse all packet information
		 */
		for (Iterator<Element> iterator = document.getRootElement().elements().iterator(); iterator.hasNext(); ) {
			Element element = iterator.next();
			
			/*
			 * Get the radius in the document
			 */
			int radius = Integer.valueOf(element.elementText("radius"));
			
			/*
			 * 
			 */
			Element location = element.element("center");
			int x = Integer.valueOf(location.elementText("x"));
			int y = Integer.valueOf(location.elementText("y"));
			
			/*
			 * Add the spawn area
			 */
			areas.add(new SpawnArea(new Location(x, y), radius));
		}
		
		/*
		 * Spawn the monsters
		 */
		for (SpawnArea area : areas) {
			for (int i = 0; i < 50; i++) {
				NPC npc = World.getWorld().allocateNPC();
				npc.setType(1);
				
				int x = area.getCenter().getX() + RANDOM_GENERATOR.nextInt(area.getRadius() * 2) - area.getRadius();
				int y = area.getCenter().getY() + RANDOM_GENERATOR.nextInt(area.getRadius() * 2) - area.getRadius();
				
				npc.setTeleportTarget(new Location(x, y));
				World.getWorld().register(npc);
			}
		}
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