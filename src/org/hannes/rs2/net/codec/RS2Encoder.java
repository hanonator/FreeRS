package org.hannes.rs2.net.codec;

import org.hannes.rs2.net.Message;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * 
 * @author red
 *
 */
public class RS2Encoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object object) throws Exception {
		if (object instanceof Message) {
			Message message = (Message) object;
			
			/*
			 * If the message is a "raw" message only the payload is required to be sent
			 */
			if (message.isRaw()) {
				/*
				 * Create the ChannelBuffer. The size is the length of the message
				 */
				ChannelBuffer buffer = ChannelBuffers.buffer(message.size());
				
				/*
				 * Read the buffer
				 */
				buffer.writeBytes(message.getBuffer(), 0, message.size());
				
				/*
				 * Send it
				 */
				return buffer;
			} else {
				/*
				 * Create the ChannelBuffer. The size is 1 (for opcode) + length
				 * of the payload + the ordinal of the messagetype.
				 */
				ChannelBuffer buffer = ChannelBuffers.buffer(message.size() + message.getType().ordinal() + 1);
				
				/*
				 * Write the opcode as a byte
				 */
				buffer.writeByte(message.getOpcode());
				
				/*
				 * Write the length if necessary
				 */
				switch (message.getType()) {
				case VARIABLE_8_BIT:
					buffer.writeByte(message.size());
					break;
				case VARIABLE_16_BIT:
					buffer.writeShort(message.size());
					break;
				case FIXED:
					break;
				}
				
				/*
				 * Write the payload
				 */
				buffer.writeBytes(message.getBuffer(), 0, message.size());
				
				/*
				 * return the buffer
				 */
				return buffer;
			}
		}
		return null;
	}

}