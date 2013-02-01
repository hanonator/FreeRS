package org.hannes.rs2.net;

import java.util.logging.Logger;

import org.hannes.rs2.World;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelLocal;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * The RS2 ChannelHandler implementation
 * 
 * fuck, I hate Rune-Server
 * 
 * @author red
 */
public class RS2ChannelHandler extends SimpleChannelHandler {

	/**
	 * The logger
	 */
	private static final Logger logger = Logger.getLogger(RS2ChannelHandler.class.getName());

	/**
	 * The ChannelLocal for the connections
	 */
	private static final ChannelLocal<Connection> local = new ChannelLocal<Connection>();

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent event) {
		event.getCause().printStackTrace();
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent event) throws Exception {
		logger.info(ctx.getChannel().getRemoteAddress() + " connected");
		local.setIfAbsent(ctx.getChannel(), new Connection(ctx.getChannel()));
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent event) throws Exception {
		logger.info(ctx.getChannel().getRemoteAddress() + " disconnected");
		World.getWorld().destroy(local.get(ctx.getChannel()).getPlayer());
		local.get(ctx.getChannel()).setState(ConnectionState.DISCONNECTED);
		local.remove(ctx.getChannel());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) throws Exception {
		Connection connection = local.get(ctx.getChannel());
		
		if (event.getMessage() instanceof Message) {
			connection.read((Message) event.getMessage());
		}
	}

	public static Connection get(Channel channel) {
		return local.get(channel);
	}

	public static ChannelLocal<Connection> getLocal() {
		return local;
	}

}