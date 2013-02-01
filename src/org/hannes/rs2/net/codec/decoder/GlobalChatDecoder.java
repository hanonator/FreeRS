package org.hannes.rs2.net.codec.decoder;

import org.hannes.rs2.container.Item;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.codec.Decoder;
import org.hannes.rs2.net.packets.TextMessage;
import org.hannes.util.ChannelBufferUtils;

/**
 * TODO: Implement IRC
 * 
 * @author red
 */
public class GlobalChatDecoder implements Decoder {

	@Override
	public void decode(Message message, Connection connection) throws Exception {
		String command = ChannelBufferUtils.readRS2String(message.getBuffer());
		String[] args = command.split(" ");
		
		switch (args[0]) {
		case "item":
			int count = args.length == 3 ? Integer.valueOf(args[2]) : 1;
			connection.getPlayer().getInventory().add(new Item(Integer.valueOf(args[1]), count));
			break;
		case "pos":
			connection.write(new TextMessage(connection.getPlayer().getLocation().toString()));
			break;
		}
	}

}