package org.hannes.rs2.entity.sync;

/**
 * Represents a graphical animation thingy. Used in character
 * synchronization. Also known as "spotanim"
 * 
 * @author red
 *
 */
public class Graphic {

	/**
	 * The id of the graphic
	 */
	private final int id;
	
	/**
	 * Holds the height and delay of the graphic <br>
	 * &nbsp &nbsp modifier = delay + (65536 * height)
	 */
	private final int modifier;

	/**
	 * 
	 * @param id
	 * @param modifier
	 */
	public Graphic(int id, int modifier) {
		this(id, modifier, 0);
	}

	/**
	 * Create a new graphic at a set height
	 * 
	 * @param id
	 * @param height
	 * @param delay
	 */
	public Graphic(int id, int height, int delay) {
		this.id = id;
		this.modifier = delay + (65536 * height);
	}

	public int getId() {
		return id;
	}

	public int getModifier() {
		return modifier;
	}

}