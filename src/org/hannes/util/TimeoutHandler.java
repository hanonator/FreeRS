package org.hannes.util;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;

/**
 * Handles a read timeout
 * 
 * @author idk
 */
public class TimeoutHandler extends ReadTimeoutHandler {

	public TimeoutHandler(org.jboss.netty.util.Timer timer, int timeoutSeconds) {
		super(timer, timeoutSeconds);
	}

	@Override
	public void readTimedOut(ChannelHandlerContext ctx) throws Exception {
		ctx.getChannel().close();
	}

}