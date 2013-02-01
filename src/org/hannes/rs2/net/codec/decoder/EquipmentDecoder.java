package org.hannes.rs2.net.codec.decoder;

import org.hannes.rs2.container.impl.Equipment;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.codec.Decoder;


/**
 * Wear Item
 **/
public class EquipmentDecoder implements Decoder {

	/**
	 * Opcode of the add item packet
	 */
	public static final int ADD = 41;
	
	/**
	 * Opcode of the remove item packet
	 */
	public static final int REMOVE = 145;

	@Override
	public void decode(Message message, Connection connection) throws Exception {
		switch (message.getOpcode()) {
		case ADD:
			int itemId = message.getShort();
			int slot = message.getShort();
			int interfaceId = message.getShort();
			
			if (interfaceId == Equipment.INTERFACE) {
				connection.getPlayer().getEquipment().equip(interfaceId, itemId, slot);
			}
			break;
		case REMOVE:
			interfaceId = message.getShort();
			slot = message.getShort();
			itemId = message.getShort();
			
			if (interfaceId == Equipment.INTERFACE) {
				connection.getPlayer().getEquipment().unequip(slot);
			}
			break;
		}
	}

}