package org.hannes.rs2.container;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.MessageBuilder;
import org.hannes.rs2.net.MessageLength;
import org.hannes.rs2.net.Packet;

public class ContainerContext implements Packet {

	/**
	 * The container
	 */
	private final Container container;
	
	/**
	 * The interface id on which the items will be displayed
	 */
	private final int interfaceId;

	/**
	 * 
	 * 
	 * @param container
	 * @param interfaceId
	 */
	public ContainerContext(Container container, int interfaceId) {
		this.container = container;
		this.interfaceId = interfaceId;
	}

	@Override
	public Message build(Connection connection) {
		Item[] items = container.toArray();
		
		MessageBuilder bldr = new MessageBuilder(53, MessageLength.VARIABLE_16_BIT);
		bldr.putShort((short) interfaceId);
		bldr.putShort((short) items.length);
		for(int i = 0; i < items.length; i++) {
			bldr.putSmart(i);
			if(items[i] != null) {
				bldr.putShort((short) (items[i].getId() + 1));
				int count = items[i].getAmount();
				if(count > 254) {
					bldr.put((byte) 255);
					bldr.putInt(count);
				} else {
					bldr.put((byte) count);
				}
			} else {
				bldr.putShort((short) 0);
				bldr.put((byte) 0);
			}
		}
		return bldr.build();
	}

	public Container getContainer() {
		return container;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

}