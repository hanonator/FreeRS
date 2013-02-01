package org.hannes.rs2.entity.sync;

/**
 * Represents an animation performed by a character. This is
 * used in the character synchronization
 * 
 * @author red
 *
 */
public class Animation {

	/**
	 * The id.
	 */
	private final int id;
	
	/**
	 * The delay.
	 */
	private final int delay;
	
	/**
	 * Creates an animation.
	 * @param id The id.
	 * @param delay The delay.
	 */
	public Animation(int id, int delay) {
		this.id = id;
		this.delay = delay;
	}
	
	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the delay.
	 * @return The delay.
	 */
	public int getDelay() {
		return delay;
	}

}