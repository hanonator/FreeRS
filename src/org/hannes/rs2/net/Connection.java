package org.hannes.rs2.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.hannes.rs2.entity.Player;
import org.hannes.rs2.net.codec.Decoders;
import org.jboss.netty.channel.Channel;


/**
 * A connection between the client and the server.
 * 
 * @author goku
 */
public class Connection {

	/**
	 * The current state of the connection
	 */
	private ConnectionState state = ConnectionState.CONNECTED;

	/**
	 * This connection's channel
	 */
	private final Channel channel;
	
	/**
	 * The messages scheduled to be sent to the client
	 */
	private final Queue<Message> out = new LinkedList<Message>();
	
	/**
	 * The connection's attributes. Will not be saved
	 */
	private final Map<Object, Object> attributes = new HashMap<Object, Object>();
	
	/**
	 * The player connected to this channel
	 */
	private Player player;

	/**
	 * Creates a new connection object around a given channel
	 * 
	 * @param channel
	 */
	public Connection(Channel channel) {
		this.channel = channel;
	}

	/**
	 * Send the messages to the client
	 */
	public void flush() {
		synchronized (out) {
			for (Iterator<Message> iterator = out.iterator(); iterator.hasNext(); ) {
				Message msg = iterator.next();
				
				/*
				 * Write the message
				 */
				channel.write(msg);
				
				/*
				 * Remove the message from the queue
				 */
				iterator.remove();
			}
		}
	}

	/**
	 * Adds a message to the end of the outgoing message-queue
	 * 
	 * @param serializable
	 */
	public void write(Message msg) {
		synchronized (out) {
			if (msg != null)
				out.add(msg);
		}
	}

	/**
	 * Adds a message to the end of the outgoing message-queue
	 * 
	 * @param serializable
	 */
	public void write(Serializable pkt) {
		write(pkt.serialize(this));
	}

	/**
	 * Dispatches the message to the appropriate decoder if possible. Will attempt
	 * to queue the messages if login has not been completed yet, otherwise if the
	 * queue is full, it will just discard them.
	 * 
	 * @param message
	 */
	public void read(Message message) throws Exception {
		/*
		 * TODO: Queue if login has not been completed yet.
		 */
		Decoders.dispatch(message, this);
	}

	/**
	 * Set an attribute
	 * 
	 * @param key
	 * @param value
	 */
	public Object setAttribute(Object key, Object value) {
		if (value == null) {
			return null;
		}
		attributes.put(key, value);
		return value;
	}

	/**
	 * Gets an attribute and returns a default value if no value is available
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Object getAttribute(Object key, Object defaultValue) {
		if (attributes.containsKey(key)) {
			return attributes.get(key);
		}
		return setAttribute(key, defaultValue);
	}

	/**
	 * Gets an attribute and returns a default value if no value is available
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Object getAttribute(Object key) {
		return getAttribute(key, null);
	}

	/**
	 * Removes an attribute and returns its previous value
	 * 
	 * @param key
	 * @return
	 */
	public Object removeAttribute(Object key) {
		return attributes.remove(key);
	}

	public Channel getChannel() {
		return channel;
	}

	public ConnectionState getState() {
		return state;
	}

	public void setState(ConnectionState state) {
		this.state = state;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}