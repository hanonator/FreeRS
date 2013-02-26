package org.hannes.rs2.net.codec.decoder;

import org.hannes.Main;
import org.hannes.rs2.container.Item;
import org.hannes.rs2.container.impl.Inventory;
import org.hannes.rs2.content.event.ItemOnItemEvent;
import org.hannes.rs2.content.event.ItemOnObjectEvent;
import org.hannes.rs2.entity.RSObject;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.codec.Decoder;
import org.hannes.util.Location;

public class ItemInteractionDecoder implements Decoder {

	/**
	 * The opcode
	 */
	public static final int ITEM_ON_ITEM = 53;
	
	/**
	 * The opcode for item on object
	 */
	public static final int ITEM_ON_OBJECT = 192;

	@Override
	public void decode(Message message, Connection connection) throws Exception {
		switch (message.getOpcode()) {
		case ITEM_ON_ITEM:
			int[] slots = new int[2];
			int[] itemIds = new int[2];
			itemIds[0] = message.getShort();
			slots[0] = message.getShort();
			int interfaceId = message.getShort();
			itemIds[1] = message.getShort();
			slots[1] = message.getShort();
			
			if (interfaceId == Inventory.INTERFACE_ID) {
				Inventory inventory = connection.getPlayer().getInventory();
				
				Item[] items = {
						inventory.get(slots[0]),
						inventory.get(slots[1]),	
				};
				
				Main.getEventhub().offer(new ItemOnItemEvent(connection.getPlayer(), slots, items));
			}
			break;
		case ITEM_ON_OBJECT:
			interfaceId = message.getShort();
			
			if (interfaceId == Inventory.INTERFACE_ID) {
				RSObject object = new RSObject(message.getShort());
				object.setLocation(new Location(0, message.getShort()));
				
				int slot = message.getShort();
				if (slot < 0 || slot >= Inventory.SIZE) {
					return;
				}
				Item item = connection.getPlayer().getInventory().get(slot);
				if (item == null) {
					return;
				}
				object.getLocation().setX(message.getShort());
				
				if (item.getId() != message.getShort()) {
					return;
				}
				
				Main.getEventhub().offer(new ItemOnObjectEvent(object, item, connection.getPlayer()));
			}
			break;
		}

	}

}