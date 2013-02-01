package org.hannes.rs2.content;

import org.hannes.Main;
import org.hannes.rs2.World;
import org.hannes.rs2.content.event.RegisterEvent;
import org.hannes.rs2.content.event.SpawnEvent;
import org.hannes.rs2.event.EventHandler;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.ConnectionState;
import org.hannes.rs2.net.MessageBuilder;
import org.hannes.rs2.util.LoginConfiguration;

public class RegisterEventHandler implements EventHandler<RegisterEvent> {

	@Override
	public void handleEvent(RegisterEvent event) throws Exception {
		LoginConfiguration configuration = event.getConfiguration();
		Connection connection = event.getConnection();
		
		connection.getPlayer().setUsername(configuration.getUsername());
		
		/*
		 * Register the player
		 */
		World.getWorld().register(connection.getPlayer());

		/*
		 * Write the login response
		 */
		connection.write(new MessageBuilder()
				.put((byte) configuration.getReturnCode().ordinal())
				.put((byte) 0).put((byte) 0).build());
		
		/*
		 * Flush the connection
		 */
		connection.flush();
		
		/*
		 * Make the ConnectionState active
		 */
		connection.setState(ConnectionState.ACTIVE);
		
		/*
		 * Attempt to spawn the player
		 */
		Main.getEventhub().offer(new SpawnEvent(connection.getPlayer()));
	}

}