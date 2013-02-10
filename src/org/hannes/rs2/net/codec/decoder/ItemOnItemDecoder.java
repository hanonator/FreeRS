package org.hannes.rs2.net.codec.decoder;

import org.hannes.Main;
import org.hannes.rs2.container.Item;
import org.hannes.rs2.content.event.ItemOnItemEvent;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.codec.Decoder;

public class ItemOnItemDecoder implements Decoder {

	@Override
	public void decode(Message message, Connection connection) throws Exception {
		int[] slots = new int[2];
		slots[0] = message.getShort();
		slots[1] = message.getShort();
		
		Item[] items = new Item[2];
		items[0] = connection.getPlayer().getInventory().get(slots[0]);
		items[1] = connection.getPlayer().getInventory().get(slots[1]);
		
		Main.getEventhub().offer(new ItemOnItemEvent(connection.getPlayer(), slots, items));
	}

}