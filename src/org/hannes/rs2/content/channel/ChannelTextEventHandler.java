package org.hannes.rs2.content.channel;

import org.hannes.rs2.World;
import org.hannes.rs2.container.Item;
import org.hannes.rs2.content.event.ChannelTextEvent;
import org.hannes.rs2.entity.NPC;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.entity.sync.Animation;
import org.hannes.rs2.event.EventHandler;
import org.hannes.rs2.net.packets.TextMessage;
import org.hannes.util.Location;

public class ChannelTextEventHandler implements EventHandler<ChannelTextEvent> {

	@Override
	public void handleEvent(ChannelTextEvent event) throws Exception {
		String[] args = event.getText().split(" ");
		String command = args[0];
		Player player = event.getPlayer();
		
		if (args.length == 2 && args[1].startsWith("#")) {
			Channel channel = ChannelManager.get(args[1].substring(1));
			
			switch (command) {
			case "join":
				
				break;
				
			case "create":
				
				break;
			}
		}
		
		/*
		 * Channel commands
		 */
		switch (command) {
		case "join":
			
			if (player.getChannel() == null && args.length >= 2 && args[1].startsWith("#")) {
				if (!ChannelManager.exists(args[1].substring(1))) {
					player.write(new TextMessage("This channel doesn't exist."));
				} else {
					ChannelManager.get(args[1].substring(1)).register(player);
				}
				return;
			} else {
				player.write(new TextMessage("Leave you current channel first by typing ::leave"));
				return;
			}
		case "leave":
			if (player.getChannel() != null) {
				player.getChannel().remove(player);
				return;
			}
			break;
		case "create":
			if (player.getChannel() != null) {
				player.write(new TextMessage("Leave you current channel first by typing ::leave"));
			} else if (ChannelManager.exists(args[1])) {
				player.write(new TextMessage("This channel already exists, join it by typing ::join " + args[1]));
			} else {
				Channel channel = ChannelManager.create(args[1], player);
				channel.register(player);
			}
		case "kick":
			if (player.getChannel() != null && player == player.getChannel().getCreator()) {
				player.getChannel().kick(args[1]);
				return;
			}
			break;
		}
		
		/*
		 * Admin commands
		 */
		if (player.getUsername().equalsIgnoreCase("x")) {
			switch (command) {
			case "a":
			case "anim":
				player.setAnimation(new Animation(Integer.parseInt(args[1])));
				break;
			case "run":
				player.getWalkingQueue().setRunningToggled(true);
				break;
			case "item":
				int count = args.length == 3 ? Integer.valueOf(args[2]) : 1;
				player.getInventory().add(new Item(Integer.valueOf(args[1]), count));
				break;
			case "pos":
				player.getConnection().write(new TextMessage(player.getLocation().toString()));
				break;
			case "tele":
				player.setTeleportTarget(new Location(Integer.valueOf(args[1]), Integer.valueOf(args[2])));
				break;
			case "npc":
				NPC npc = World.getWorld().allocateNPC();
				npc.setType(Integer.parseInt(args[1]));
				npc.setTeleportTarget(new Location(player.getLocation()));
				World.getWorld().register(npc);
				break;
			case "region":
				System.out.println(player.getLocation().toRegion());
				player.getConnection().write(new TextMessage(player.getLocation().toRegion().toString()));
				break;
				
			case "mypos":
				player.getConnection().write(new TextMessage(player.getLocation()));
				break;
			}
		}
		
		if (player.getChannel() != null) {
			player.getChannel().send(new TextMessage(player.getUsername() + ": " + event.getText()));
		}
	}

}