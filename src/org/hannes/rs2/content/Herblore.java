package org.hannes.rs2.content;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.hannes.rs2.container.impl.Inventory;
import org.hannes.rs2.content.event.ItemOnItemEvent;
import org.hannes.rs2.content.misc.MakeAction;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.entity.sync.Animation;
import org.hannes.rs2.event.EventHandler;
import org.hannes.rs2.net.packets.CreateItemInterface;
import org.hannes.rs2.net.packets.TextMessage;

/**
 * Basic herblore (combining ingredients)
 * 
 * @author red
 *
 */
public class Herblore implements EventHandler<ItemOnItemEvent> {

	/**
	 * The chat interface
	 */
	private static final int INTERFACE_ID = 4429;

	/**
	 * The zoom factor
	 */
	private static final int[] MODEL_ZOOM = { 150 };

	/**
	 * The interface
	 */
	private static final int[] MODEL_INTERFACE_IDS = { 1746 };
	
	/**
	 * The animation when the player combines 2 ingredients n shit
	 */
	private static final Animation ANIMATION = new Animation(3283, 0);

	/**
	 * The map of mixtures
	 */
	private static final Map<Integer, Mixture> mixtures = new HashMap<>();

	public static void initialize(Document document) throws Exception {
		/*
		 * Parse all packet information
		 */
		for (Iterator<Element> iterator = document.getRootElement().elements().iterator(); iterator.hasNext(); ) {
			Element element = iterator.next();

			/*
			 * Get the data
			 */
			int primaryIngredient = Integer.valueOf(element.elementText("primaryIngredient"));
			int secondaryIngredient = Integer.valueOf(element.elementText("secondaryIngredient"));
			int result = Integer.valueOf(element.elementText("result"));

			/*
			 * Get the identifier
			 */
			int min = Math.min(primaryIngredient, secondaryIngredient);
			int max = Math.max(primaryIngredient, secondaryIngredient);
			
			/*
			 * Add to the list
			 */
			mixtures.put((max << 16) | min,
					new Mixture(primaryIngredient, secondaryIngredient, result));
		}
	}

	@Override
	public void handleEvent(ItemOnItemEvent event) throws Exception {
		/*
		 * Make sure none of the items is invalid
		 */
		int[] items = new int[2];
		items[0] = event.getItems()[0] == null ? -1 : event.getItems()[0].getId();
		items[1] = event.getItems()[1] == null ? -1 : event.getItems()[1].getId();
		
		/*
		 * Get the identifier
		 */
		int min = Math.min(items[0], items[1]);
		int max = Math.max(items[0], items[1]);
		int identifier = (max << 16) | min;
		
		/*
		 * Get mixture n start cookin'
		 */
		Mixture mixture = mixtures.get(identifier);
		if (mixture != null) {
			Player player = event.getPlayer();
			
			/*
			 * Set the make action
			 */
			player.setMakeAction(new HerbloreAction(player, mixture));
			
			/*
			 * Result because CreateItemInterface only accepts int[]
			 */
			int[] result = new int[] { mixture.result };
			
			/*
			 * Open the chat interface
			 */
			player.getConnection().write(new CreateItemInterface(result, 
					MODEL_ZOOM, MODEL_INTERFACE_IDS, INTERFACE_ID));
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

		public Mixture(int primaryIngredient, int secondaryIngredient, int result) {
			this.primaryIngredient = primaryIngredient;
			this.secondaryIngredient = secondaryIngredient;
			this.result = result;
		}
		
	}

	/**
	 * 
	 * @author red
	 *
	 */
	private static class HerbloreAction extends MakeAction {
		
		/**
		 * The mixture
		 */
		private final Mixture mixture;

		public HerbloreAction(Player player, Mixture mixture) {
			super(2, player);
			this.mixture = mixture;
		}

		@Override
		public boolean doAction(Player player, int slot, int remaining) throws Exception {
			Inventory inventory = player.getInventory();

			/*
			 * If there are no more items to be made, do nothing
			 */
			if (remaining <= 0) {
				return true;
			}
			
			/*
			 * Check for the primary ingredient
			 */
			if (!inventory.contains(mixture.primaryIngredient)
					|| !inventory.contains(mixture.secondaryIngredient)) {
				player.getConnection().write(new TextMessage("You don't have the ingredients to make this."));
				return true;
			}

			/*
			 * Remove ingredients and add result
			 */
			inventory.remove(mixture.primaryIngredient, 1);
			inventory.remove(mixture.secondaryIngredient, 1);
			inventory.add(mixture.result, 1);
			
			/*
			 * Perform emote
			 */
			player.setAnimation(ANIMATION);
			
			super.setAmount(super.getAmount() - 1);
			return remaining - 1 <= 0
					|| !inventory.contains(mixture.primaryIngredient)
					|| !inventory.contains(mixture.secondaryIngredient);
		}
	
	}

}