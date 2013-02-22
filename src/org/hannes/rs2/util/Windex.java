package org.hannes.rs2.util;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.MessageBuilder;
import org.hannes.rs2.net.Serializable;

/**
 * Clears all windows, get it
 * 
 * @author red
 *
 */
public class Windex implements Serializable {

	@Override
	public Message serialize(Connection connection) {
		return new MessageBuilder(219).build();
	}

}