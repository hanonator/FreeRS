package org.hannes.rs2.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.hannes.rs2.container.impl.Inventory;
import org.hannes.rs2.content.event.ItemOnObjectEvent;
import org.hannes.rs2.content.misc.MakeAction;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.entity.sync.Animation;
import org.hannes.rs2.event.EventHandler;
import org.hannes.rs2.net.packets.CreateItemInterface;
import org.hannes.rs2.net.packets.TextMessage;

public class Cooking implements EventHandler<ItemOnObjectEvent> {

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
	 * The animation when the player cooks something on a stove
	 */
	private static final Animation STOVE_ANIMATION = new Animation(883, 0);
	
	/**
	 * The animation when the player cooks something on a fire
	 */
	private static final Animation FIRE_ANIMATION = new Animation(897, 0);

	/**
	 * The map of mixtures
	 */
	private static final Map<Integer, Dish> dishes = new HashMap<>();
	
	/**
	 * List of stove ids
	 */
	private static final List<Integer> stoves = new ArrayList<>();
	
	/**
	 * List of fire ids
	 */
	private static final List<Integer> fires = new ArrayList<>();

	public static void initialize(Document document) throws Exception {
		/*
		 * Parse all recipe information
		 */
		for (Iterator<Element> iterator = document.getRootElement().element("recipes").elements().iterator(); iterator.hasNext(); ) {
			Element element = iterator.next();

			/*
			 * Get the data
			 */
			int ingredient = Integer.valueOf(element.attributeValue("ingredient"));
			int result = Integer.valueOf(element.getText());
			
			/*
			 * Add to the list
			 */
			dishes.put(ingredient, new Dish(ingredient, result));
		}
		
		/*
		 * Parse all stove information
		 */
		for (Iterator<Element> iterator = document.getRootElement().element("stoves").elements().iterator(); iterator.hasNext(); ) {
			Element element = iterator.next();
			/*
			 * Add to the list
			 */
			stoves.add(Integer.valueOf(element.getText()));
		}
		
		/*
		 * Parse all fire information
		 */
		for (Iterator<Element> iterator = document.getRootElement().element("fires").elements().iterator(); iterator.hasNext(); ) {
			Element element = iterator.next();
			/*
			 * Add to the list
			 */
			fires.add(Integer.valueOf(element.getText()));
		}
	}

	@Override
	public void handleEvent(ItemOnObjectEvent event) throws Exception {
		/*
		 * Get mixture n start cookin'
		 */
		Dish dish = dishes.get(event.getItem().getId());
		if (dish != null && (fires.contains(event.getObject().getIndex())
				|| stoves.contains(event.getObject().getIndex()))) {
			Player player = event.getPlayer();
			
			/*
			 * Set the make action
			 */
			player.setMakeAction(new CookingAction(player, dish, fires.contains(
					event.getObject().getIndex()) ? FIRE_ANIMATION : STOVE_ANIMATION));
			
			/*
			 * Result because CreateItemInterface only accepts int[]
			 */
			int[] result = new int[] { dish.result };
			
			/*
			 * Open the chat interface
			 */
			player.getConnection().write(new CreateItemInterface(result, 
					MODEL_ZOOM, MODEL_INTERFACE_IDS, INTERFACE_ID));
		}
	}
	
	private static class Dish {
		
		/**
		 * The raw ingredient
		 */
		private final int ingredient;
		
		/**
		 * The resulting item id
		 */
		private final int result;

		public Dish(int ingredient, int result) {
			this.ingredient = ingredient;
			this.result = result;
		}
		
	}

	private static class CookingAction extends MakeAction {

		/**
		 * The dish
		 */
		private final Dish dish;
		
		/**
		 * The animation
		 */
		private final Animation animation;

		public CookingAction(Player player, Dish dish, Animation animation) {
			super(2, player);
			
			this.dish = dish;
			this.animation = animation;
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
			if (!inventory.contains(dish.ingredient)) {
				player.getConnection().write(new TextMessage("You don't have anything to cook."));
				return true;
			}

			/*
			 * Remove ingredients and add result
			 */
			inventory.remove(dish.ingredient, 1);
			inventory.add(dish.result, 1);
			
			/*
			 * Perform emote
			 */
			player.setAnimation(animation);
			
			super.setAmount(super.getAmount() - 1);
			return remaining - 1 <= 0 || !inventory.contains(dish.ingredient);
		}
		
	}

}