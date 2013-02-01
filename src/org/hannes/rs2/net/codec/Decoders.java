package org.hannes.rs2.net.codec;

import java.util.logging.Logger;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.ConnectionState;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.codec.decoder.ButtonDecoder;
import org.hannes.rs2.net.codec.decoder.EquipmentDecoder;
import org.hannes.rs2.net.codec.decoder.GlobalChatDecoder;
import org.hannes.rs2.net.codec.decoder.MovementDecoder;
import org.hannes.rs2.net.codec.decoder.SilentDecoder;

public class Decoders {

	/**
	 * The decoders
	 */
	private static final Decoder[] decoders = new Decoder[256];

	/**
	 * The login decoder
	 */
	private static final RS2LoginDecoder loginDecoder = new RS2LoginDecoder();
	
	/**
	 * The logger
	 */
	private static final Logger logger = Logger.getLogger(Decoders.class.getName());

	static {
		/*
		 * Silent decoder
		 */
		SilentDecoder u = new SilentDecoder();
		decoders[0] = u;
		decoders[3] = u;
		decoders[77] = u;
		decoders[121] = u;
		decoders[241] = u;

		/*
		 * Equipment
		 */
		EquipmentDecoder equipment = new EquipmentDecoder();
		decoders[41] = equipment;
		decoders[145] = equipment;
		
		/*
		 * Global chat encoder
		 */
		decoders[103] = new GlobalChatDecoder();

		/*
		 * Movement decoder
		 */
		MovementDecoder movement = new MovementDecoder();
		decoders[98] = movement;
		decoders[164] = movement;
		decoders[248] = movement;
		
		/*
		 * The button decoder
		 */
		decoders[185] = new ButtonDecoder();
	}

	/**
	 * 
	 * @param message
	 * @param connection
	 * @throws Exception
	 */
	public static void dispatch(Message message, Connection connection) throws Exception {
		if (connection.getState() == ConnectionState.CONNECTED) {
			loginDecoder.decode(message, connection);
		} else {
			if (decoders[message.getOpcode()] != null) {
				decoders[message.getOpcode()].decode(message, connection);
			} else {
				logger.info("unknown decoder for " + message.getOpcode() + " with size " + message.size());
			}
		}
	}

}