package org.hannes.rs2.net.codec;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.RS2ChannelHandler;
import org.hannes.rs2.util.Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class RS2Decoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		Connection connection = RS2ChannelHandler.get(channel);
		
		if (connection != null) {
			switch (connection.getState()) {
			case CONNECTED:
				/*
				 * Copy the buffer and transfer all data from the original buffer so I don't have to
				 * stupid stuff
				 */
				ChannelBuffer copiedBuffer = ChannelBuffers.buffer(buffer.readableBytes());
				copiedBuffer.writeBytes(buffer);
				
				/*
				 * Create a new Message and return it
				 */
				return new Message(copiedBuffer.readableBytes(), copiedBuffer);
			default:
				/*
				 * Read the opcode
				 */
				int opcode = buffer.readByte() & 0xFF;
				
				/*
				 * Get the packet size
				 */
				int length = Constants.PACKET_SIZES[opcode];
				
				/*
				 * If the packet size is -1, this means that the size of the packet
				 * is sent right after the opcode as an unsigned byte
				 */
				if (length == -1 && buffer.readableBytes() > 0) {
					length = buffer.readByte() & 0xFF;
				}
				
				/*
				 * See if there is enough data in the buffer to 
				 */
				if (length != -1 && buffer.readableBytes() >= length) {
					ChannelBuffer payload = ChannelBuffers.buffer(length);
					buffer.readBytes(payload);
					return new Message(opcode, length, payload);
				}
				
				/*
				 * Discard read bytes since there is no message created
				 */
				buffer.discardReadBytes();
				break;
			}
		}
		return null;
	}

}