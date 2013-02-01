package org.hannes.rs2.net.packets;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.MessageBuilder;
import org.hannes.rs2.net.Packet;

public class ItemModel implements Packet {

	/**
	 * The item id
	 */
	private final int itemId;
	
	/**
	 * The zoom
	 */
	private final int zoom;
	
	/**
	 * The interface id (type 6)
	 */
	private final int interfaceId;

	/**
	 * @param itemId
	 * @param zoom
	 * @param interfaceId
	 */
	public ItemModel(int itemId, int zoom, int interfaceId) {
		this.itemId = itemId;
		this.zoom = zoom;
		this.interfaceId = interfaceId;
	}

	@Override
	public Message build(Connection connection) {
		MessageBuilder bldr = new MessageBuilder(246);
		bldr.putShort((short) interfaceId);
		bldr.putShort((short) itemId);
		bldr.putShort((short) zoom);
		return bldr.build();
	}

	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * @return the zoom
	 */
	public int getZoom() {
		return zoom;
	}

	/**
	 * @return the interfaceId
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

}