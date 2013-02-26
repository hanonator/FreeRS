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
		
		switch (command) {
		case "join":
			break;
		case "leave":
			break;
		case "create":
			break;
			
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
		
		default:
			System.out.println(command);
			break;
		}
	}

}