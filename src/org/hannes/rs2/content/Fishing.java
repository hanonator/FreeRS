package org.hannes.rs2.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hannes.rs2.action.Action;
import org.hannes.rs2.container.Item;
import org.hannes.rs2.content.event.NPCInteractionEvent;
import org.hannes.rs2.entity.NPC;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.entity.sync.Animation;
import org.hannes.rs2.event.EventHandler;
import org.hannes.rs2.net.codec.decoder.NPCInteractionDecoder;
import org.hannes.rs2.net.packets.TextMessage;

public class Fishing implements EventHandler<NPCInteractionEvent> {

	/**
	 * The collection of fishing spots
	 */
	private static final Map<Integer, FishingSpot> spots = new HashMap<>();
	
	/**
	 * The list of bait
	 */
	private static final List<Integer> bait = new ArrayList<>();

	/**
	 * Random generator
	 */
	private static final Random RANDOM_GENERATOR = new Random();
	
	static {
		bait.add(313);
		bait.add(314);
		
		
		FishingSpot spot = new FishingSpot();
		
		spot.animation = new int[] {
				621, 622
		};
		spot.equipment = new int[][] {
				{303},
				{309, 313}
		};
		spot.fish = new int[][] {
				{317, 321},
				{347, 327}
		};
		
		spots.put(317, spot);
	}

	@Override
	public void handleEvent(NPCInteractionEvent event) throws Exception {
		if (event.getOption() == NPCInteractionDecoder.FIRST_OPTION
				|| event.getOption() == NPCInteractionDecoder.SECOND_OPTION) {
			final Player player = event.getPlayer();
			final NPC npc = event.getNpc();
			final FishingSpot spot = spots.get(event.getNpc().getType());
			final int index = event.getOption() == NPCInteractionDecoder.FIRST_OPTION ? 0 : 1;
			
			/*
			 * Rotate the player towards the npc
			 */
			player.setAcquaintance(npc);
			
			if (spot != null && player != null) {
				player.getActionQueue().clear();
				player.getActionQueue().offer(new Action(4, player) {
	
					@Override
					public boolean doAction(Player player) throws Exception {
						/*
						 * Do nothing if the fishing spot is out of reach
						 */
						if (player.distance(npc) > 1) {
							return reset();
						}
						
						/*
						 * See if the player has all the necessary equipment
						 */
						for (int item : spot.equipment[index]) {
							if (!player.getInventory().contains(item)) {
								player.getConnection().write(new TextMessage("You don't have the necessary equipment to fish here."));
								return reset();
							}
						}
						
						/*
						 * Set the fishing animation
						 */
						player.setAnimation(new Animation(spot.animation[index]));
						
						/*
						 * Get the id of fish the player caught
						 */
						int fish = spot.fish[index][RANDOM_GENERATOR
								.nextInt(spot.fish[index].length)];
						
						/*
						 * Add the loot
						 */
						player.getInventory().add(new Item(fish, 1));

						/*
						 * Remove bait (if necessary)
						 */
						for (int item : spot.equipment[index]) {
							if (bait.contains(item)) {
								player.getInventory().remove(item, 1);
							}
						}
						return false;
					}
					
					private boolean reset() {
						// TODO: other reset codeings
						player.setAnimation(Animation.RESET);
						return true;
					}
					
				});
			}
		}
	}

	private static class FishingSpot {
		
		/**
		 * Fish required
		 */
		private int[][] fish;
		
		/**
		 * Equipment required
		 */
		private int[][] equipment;
		
		/**
		 * Animations performed
		 */
		private int[] animation;
		
	}

}