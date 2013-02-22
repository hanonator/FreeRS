package org.hannes.rs2.content.misc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.hannes.Main;
import org.hannes.rs2.content.event.ButtonEvent;
import org.hannes.rs2.content.event.MakeEvent;
import org.hannes.rs2.event.EventHandler;

public class MakeButtonEventHandler implements EventHandler<ButtonEvent> {

	/**
	 * The collection of buttons
	 */
	private static final Map<Integer, MakeButton> buttons = new HashMap<>();

	/**
	 * Initializes all the buttons
	 */
	public static void initialize(Document document) throws Exception {
		/*
		 * Parse all packet information
		 */
		for (Iterator<Element> iterator = document.getRootElement().elements().iterator(); iterator.hasNext(); ) {
			Element element = iterator.next();

			int slot = Integer.valueOf(element.attributeValue("slot"));
			int amount = Integer.valueOf(element.attributeValue("amount"));
			int id = Integer.valueOf(element.getText());
			
			buttons.put(id, new MakeButton(slot, amount));
		}
	}

	@Override
	public void handleEvent(ButtonEvent event) throws Exception {
		MakeButton button = buttons.get(event.getButton());
		
		if (button != null) {
			Main.getEventhub().offer(new MakeEvent(event.getPlayer(), button.slot, button.amount));
		}
	}

	private static class MakeButton {
		
		/**
		 * The slot of the item to be produced in the interface
		 */
		private final int slot;
		
		/**
		 * Amount of items to be produced
		 */
		private final int amount;

		public MakeButton(int slot, int amount) {
			this.slot = slot;
			this.amount = amount;
		}
		
	}

}