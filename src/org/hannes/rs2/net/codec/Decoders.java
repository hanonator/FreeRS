package org.hannes.rs2.net.codec;

import java.util.logging.Logger;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.ConnectionState;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.codec.decoder.ButtonDecoder;
import org.hannes.rs2.net.codec.decoder.ChannelTextDecoder;
import org.hannes.rs2.net.codec.decoder.EquipmentDecoder;
import org.hannes.rs2.net.codec.decoder.ItemInteractionDecoder;
import org.hannes.rs2.net.codec.decoder.MovementDecoder;
import org.hannes.rs2.net.codec.decoder.ObjectInteractionDecoder;
import org.hannes.rs2.net.codec.decoder.SilentDecoder;
import org.hannes.rs2.net.codec.decoder.UserValueDecoder;

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
		 * Misc
		 */
		decoders[ChannelTextDecoder.OPCODE] = new ChannelTextDecoder();
		
		/*
		 * Equipment
		 */
		EquipmentDecoder equipment = new EquipmentDecoder();
		decoders[EquipmentDecoder.OPCODE_ADD] = equipment;
		decoders[EquipmentDecoder.OPCODE_REMOVE] = equipment;

		/*
		 * Movement decoder
		 */
		MovementDecoder movement = new MovementDecoder();
		decoders[MovementDecoder.PRIMARY_OPCODE] = movement;
		decoders[MovementDecoder.SECONDARY_OPCODE] = movement;
		decoders[MovementDecoder.TERNARY_OPCODE] = movement;
		
		/*
		 * The button decoder
		 */
		decoders[ButtonDecoder.OPCODE] = new ButtonDecoder();
		
		/*
		 * The object interaction decoders
		 */
		ObjectInteractionDecoder object = new ObjectInteractionDecoder();
		decoders[ObjectInteractionDecoder.FIRST_OPTION] = object;
		decoders[ObjectInteractionDecoder.SECOND_OPTION] = object;
		decoders[ObjectInteractionDecoder.THIRD_OPTION] = object;
	
		/*
		 * Item interaction
		 */
		ItemInteractionDecoder items = new ItemInteractionDecoder();
		decoders[ItemInteractionDecoder.ITEM_ON_ITEM] = items;
		decoders[ItemInteractionDecoder.ITEM_ON_OBJECT] = items;
		
		/*
		 * User prompt
		 */
		decoders[UserValueDecoder.OPCODE] = new UserValueDecoder();
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