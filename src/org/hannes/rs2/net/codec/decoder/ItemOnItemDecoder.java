package org.hannes.rs2.net.codec.decoder;

import org.hannes.Main;
import org.hannes.rs2.container.Item;
import org.hannes.rs2.container.impl.Inventory;
import org.hannes.rs2.content.event.ItemOnItemEvent;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.codec.Decoder;

public class ItemOnItemDecoder implements Decoder {

	/**
	 * The opcode
	 */
	public static final int OPCODE = 53;

	@Override
	public void decode(Message message, Connection connection) throws Exception {
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
	}

}