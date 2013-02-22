package org.hannes.rs2.net.packets;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.Serializable;

/**
 * 
 * 
 * @author red
 *
 */
public class CreateItemInterface implements Serializable {

	/**
	 * The item models to be shown
	 */
	private final int[] items;
	
	/**
	 * The zoom factor for each item
	 */
	private final int[] zoom;
	
	/**
	 * The interface ids for the models
	 */
	private final int[] modelInterfaceIds;
	
	/**
	 * The interface id
	 */
	private final int interfaceId;

	public CreateItemInterface(int[] items, int[] zoom, int[] modelInterfaceIds, int interfaceId) {
		this.items = items;
		this.zoom = zoom;
		this.modelInterfaceIds = modelInterfaceIds;
		this.interfaceId = interfaceId;
	}

	@Override
	public Message serialize(Connection connection) {
		if(items.length != modelInterfaceIds.length && items.length != zoom.length) {
			throw new IllegalArgumentException();
		}
		for(int i = 0; i < items.length; i++) {
			connection.write(new ItemModel(items[i], zoom[i], modelInterfaceIds[i]));
		}
		connection.write(new ChatInterface(interfaceId));
		return null;
	}

}