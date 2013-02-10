package org.hannes.rs2.net.codec.decoder;

import org.hannes.rs2.World;
import org.hannes.rs2.container.Item;
import org.hannes.rs2.entity.NPC;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.codec.Decoder;
import org.hannes.rs2.net.packets.TextMessage;
import org.hannes.util.ChannelBufferUtils;
import org.hannes.util.Location;

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
		case "tele":
			connection.getPlayer().setTeleportTarget(new Location(Integer.valueOf(args[1]), Integer.valueOf(args[2])));
			break;
		case "npc":
			NPC npc = World.getWorld().allocateNPC();
			npc.setType(Integer.parseInt(args[1]));
			npc.setTeleportTarget(new Location(connection.getPlayer().getLocation()));
			World.getWorld().register(npc);
			break;
		case "region":
			System.out.println(connection.getPlayer().getLocation().toRegion());
			connection.write(new TextMessage(connection.getPlayer().getLocation().toRegion().toString()));
			break;
		}
	}

}