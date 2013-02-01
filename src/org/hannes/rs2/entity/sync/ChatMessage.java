package org.hannes.rs2.entity.sync;

/**
 * A message sent by a player sent to other players that is shown both in the chatbox
 * abd above the player's head. Used in (player) character synchronization
 * 
 * @author red
 *
 */
public class ChatMessage {

	/**
	 * The color of the message
	 */
	private final int color;
	
	/**
	 * The effects on the message (such as :wave:)
	 */
	private final int effects;

	/**
	 * The text dat
	 */
	private final byte[] text;
	
	/**
	 * 
	 * @param color
	 * @param effects
	 * @param text
	 */
	public ChatMessage(int color, int effects, byte[] text) {
		this.color = color;
		this.effects = effects;
		this.text = text;
	}
	
	public int getColor() {
		return color;
	}
	
	public int getEffects() {
		return effects;
	}
	
	public byte[] getText() {
		return text;
	}

}