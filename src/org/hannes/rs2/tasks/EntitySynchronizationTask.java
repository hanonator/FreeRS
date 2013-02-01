package org.hannes.rs2.tasks;

import java.util.Iterator;

import org.hannes.rs2.World;
import org.hannes.rs2.entity.NPC;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.entity.sync.CharacterSync;
import org.hannes.rs2.entity.sync.NPCSync;
import org.hannes.rs2.entity.sync.PlayerSync;
import org.hannes.util.GameEngine;
import org.hannes.util.TimedTask;


/**
 * Periodically updates all entities
 * 
 * @author red
 */
public class EntitySynchronizationTask extends TimedTask {

	/**
	 * All of the character synchronizations
	 */
	private final CharacterSync[] syncs = new CharacterSync[2];

	/**
	 * Create a new EntitySynchronizationTask with 1 gamecycle delay
	 */
	public EntitySynchronizationTask() {
		super(1);

		syncs[0] = new PlayerSync();
		syncs[1] = new NPCSync();
	}

	@Override
	public boolean cycle(GameEngine engine) throws Exception {
		/*
		 * Update the npcs
		 */
		for (Iterator<NPC> iterator = World.getWorld().getNPCs().iterator(); iterator.hasNext(); ) {
			NPC npc = iterator.next();
			
			/*
			 * ok
			 */
			npc.getWalkingQueue().processNextMovement();
		}
		
		/*
		 * Update the players
		 */
		for (Iterator<Player> iterator = World.getWorld().getPlayers().iterator(); iterator.hasNext(); ){
			Player player = iterator.next();
			
			// TODO: create the update block
			
			// TODO: Store the player's "update parameters" for new player login requests (direction, ...)
			
			/*
			 * Process the player's next movement.
			 */
			player.getWalkingQueue().processNextMovement();
		}
		
		for (Iterator<Player> iterator = World.getWorld().getPlayers().iterator(); iterator.hasNext(); ){
			Player player = iterator.next();
			
			/*
			 * Send the sync
			 */
			for (CharacterSync sync : syncs) {
				player.getConnection().write(sync.synchronize(player));
			}
			 
			/*
			 * Finish the synchronization
			 */
			player.finishSynchronization();
		}
		return false;
	}

}