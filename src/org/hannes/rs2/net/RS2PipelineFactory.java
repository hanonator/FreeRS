package org.hannes.rs2.net;

import org.hannes.rs2.net.codec.RS2Decoder;
import org.hannes.rs2.net.codec.RS2Encoder;
import org.hannes.util.TimeoutHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

/**
 * The default RS2 pipeline factory
 * 
 * @author red
 */
public class RS2PipelineFactory implements ChannelPipelineFactory {

	/**
	 * The timer for the timeouthandler
	 */
	private static Timer timeoutTimer = new HashedWheelTimer();

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		
		/*
		 * TimeoutHandler lol
		 */
		pipeline.addFirst("TimeoutHandler", new TimeoutHandler(timeoutTimer, 5));

		/*
		 * Add the encoder and the decoder
		 */
		pipeline.addLast("encoder", new RS2Encoder());
		pipeline.addLast("decoder", new RS2Decoder());

		/*
		 * Add the handler
		 */
		pipeline.addLast("handler", new RS2ChannelHandler());
		return pipeline;
	}
	
}