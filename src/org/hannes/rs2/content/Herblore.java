package org.hannes.rs2.content;

import java.util.HashMap;
import java.util.Map;

import org.hannes.rs2.action.Action;
import org.hannes.rs2.content.event.ItemOnItemEvent;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.event.EventHandler;

public class Herblore implements EventHandler<ItemOnItemEvent> {

	/**
	 * The map of mixtures
	 */
	private static final Map<Integer, Mixture> mixtures = new HashMap<>();

	@Override
	public void handleEvent(ItemOnItemEvent event) throws Exception {
		int[] items = new int[2];
		items[0] = event.getItems()[0] == null ? -1 : event.getItems()[0].getId();
		items[1] = event.getItems()[1] == null ? -1 : event.getItems()[1].getId();
		
		int min = Math.min(items[0], items[1]);
		int max = Math.max(items[0], items[1]);
		
		int identifier = (max << 16) | min;
		
		Mixture mixture = mixtures.get(identifier);
		
		if (mixture != null) {
			Player player = event.getPlayer();
			
			
		}
	}

	public static void mix(final Player player, final Mixture mixture, int amount) {
		for (int i = 0; i < amount; i++) {
			player.getActionQueue().offer(new Action(player) {

				@Override
				public boolean doAction(Player player) throws Exception {
					
					return false;
				}
				
			});
		}
	}

	/**
	 * A mixture of 2 items and items 
	 * 
	 * @author red
	 */
	private static class Mixture {
		
		/**
		 * The primary item id
		 */
		private final int primaryIngredient;
		
		/**
		 * The secondary item id
		 */
		private final int secondaryIngredient;
		
		/**
		 * The resulting item id
		 */
		private final int result;
		
		/**
		 * The experience earned
		 */
		private final int experience;

		public Mixture(int primaryIngredient, int secondaryIngredient, int result, int experience) {
			this.primaryIngredient = primaryIngredient;
			this.secondaryIngredient = secondaryIngredient;
			this.result = result;
			this.experience = experience;
		}
		
	}

}