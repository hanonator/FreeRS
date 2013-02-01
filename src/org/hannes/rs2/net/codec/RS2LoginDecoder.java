package org.hannes.rs2.net.codec;

import java.util.Random;

import org.hannes.Main;
import org.hannes.rs2.World;
import org.hannes.rs2.content.event.RegisterEvent;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.MessageBuilder;
import org.hannes.rs2.util.LoginConfiguration;
import org.hannes.rs2.util.LoginState;
import org.hannes.rs2.util.MemoryUsage;
import org.hannes.rs2.util.ReturnCode;
import org.hannes.util.ChannelBufferUtils;

/**
 * The abstract decoder for the RuneScape 2 protocol
 * 
 * @author red
 * 
 */
public class RS2LoginDecoder implements Decoder {

	/**
	 * The generator for randoms lol
	 */
	private static final Random RANDOM_GENERATOR = new Random();

	/**
	 * The initial response (idk lol)
	 */
	private static final byte[] INITIAL_RESPONSE = { 0x0, 0x0, 0x0, 0x0, 0x0,
			0x0, 0x0, 0x0 };

	/**
	 * Representin' them state, yo
	 */
	private static final String STATE = "login-state";

	@Override
	public void decode(Message message, Connection connection) throws Exception {
		LoginState state = (LoginState) connection.getAttribute(STATE, LoginState.OPCODE);
		
		switch (state) {
		case OPCODE:
			if(message.remaining() >= 2) {
				/*
				 * Read the opcode
				 */
				final int opcode = message.get();
				
				/*
				 * Opcode for game request is 14. No other protocols are
				 * currently supported (and probably never will be because
				 * I'm lazy as fuark)
				 */
				switch (opcode) {
				case 14:
					/*
					 * The name hash.
					 */
					@SuppressWarnings("unused")
					final int hash = message.get();
					
					/*
					 * Send the default response
					 */
					connection.write(new MessageBuilder().put(INITIAL_RESPONSE).put((byte) 0)
							.putLong(RANDOM_GENERATOR.nextLong()).build());
					
					/*
					 * Flush for faster login
					 */
					connection.flush();

					/*
					 * Set the next loginstate
					 */
					connection.setAttribute(STATE, LoginState.DETAILS);
					break;
				default:
					throw new IllegalStateException("Invalid request '" + opcode + "', 14 was expected.");
				}
			}
			break;
		case DETAILS:
			LoginConfiguration configuration = new LoginConfiguration(connection);
			try {
				/*
				 * Opcodes
				 * 
				 * 16 - A player has logged in
				 * 18 - A player has reconnected
				 */
				final int opcode = message.get();
				if (opcode != 16 && opcode != 18) {
					configuration.setReturnCode(ReturnCode.LOGIN_SERVER_REJECT);
					return;
				}
				configuration.setReconnect(opcode == 18);
	
				/*
				 * Some size thingies
				 */
				final int loginPacketSize = message.get();
				final int loginEncryptPacketSize = loginPacketSize - 40;
				if (loginEncryptPacketSize <= 0) {
					configuration.setReturnCode(ReturnCode.LOGIN_SERVER_REJECT);
					return;
				}
	
				/*
				 * People call this the magic ID, Idk what that is
				 */
				final int magicId = message.get() & 0xFF;
				if (magicId != 255) {
					configuration.setReturnCode(ReturnCode.LOGIN_SERVER_REJECT);
					return;
				}
				
				/*
				 * The version of the client
				 */
				final int versionId = message.getShort();
				if(versionId != 317) {
					configuration.setReturnCode(ReturnCode.UPDATE);
					return;
				}
	
				/*
				 * The client type
				 */
				configuration.setMemoryUsage(message.get() == 0 ? MemoryUsage.LOW : MemoryUsage.HIGH);
				
				/*
				 * Read some CRC, I think
				 */
				for (int i = 0; i < 9; i++) {
					message.getInt();
				}
				
				/*
				 * We check if there is a mismatch in the sizing.
				 */
				@SuppressWarnings("unused")
				final int length = message.get();
//				if (length != 0 && length != loginPacketSize) {
//					configuration.setReturnCode(ReturnCode.LOGIN_SERVER_REJECT);
//					return;
//				}
	
				/*
				 * Some check, <i>has</i> to be 10 or this will not work.
				 * 
				 * people called it tmp, so I'm calling it that
				 */
				int tmp = message.get();
				if (tmp != 10) {
					configuration.setReturnCode(ReturnCode.LOGIN_SERVER_REJECT);
					return;
				}
	
				/*
				 * Session keys for like ISAAC or something cool
				 */
				final long clientSessionKey = message.getLong();
				final long serverSessionKey = message.getLong();
				
				/*
				 * Player's UID. Useless in RSPS, really
				 */
				configuration.setUid(message.getInt());
	
				/*
				 * Player credentials
				 */
				final String username = ChannelBufferUtils.readRS2String(message.getBuffer());
				final String password = ChannelBufferUtils.readRS2String(message.getBuffer());
	
				/*
				 * Set the password and username in the configuration
				 */
				configuration.setUsername(username);
				configuration.setPassword(password);
				
				/*
				 * Don't make the same mistake like last time :EOD:
				 */
				if(username == null || username.length() == 0
						|| password  == null || password.length() == 0) {
					configuration.setReturnCode(ReturnCode.INVALID_DETAILS);
					return;
				}
				
				/*
				 * See if the world is full
				 */
				Player player = World.getWorld().allocatePlayer(connection);
				if (player == null) {
					configuration.setReturnCode(ReturnCode.WORLD_FULL);
					return;
				}
				
				/*
				 * Set the connection's player object
				 */
				connection.setPlayer(player);
	
				/*
				 * Session keys for ISAAC
				 */
				final int sessionKey[] = new int[4];
				sessionKey[0] = (int) (clientSessionKey >> 32);
				sessionKey[1] = (int) clientSessionKey;
				sessionKey[2] = (int) (serverSessionKey >> 32);
				sessionKey[3] = (int) serverSessionKey;
				
				/*
				 * Remove login things
				 */
				connection.removeAttribute(STATE);
				
				/*
				 * Set the returncode to success
				 */
				configuration.setReturnCode(ReturnCode.SUCCESS);
			} finally {
				Main.getEventhub().offer(new RegisterEvent(connection, configuration));
			}
		}
	}

}