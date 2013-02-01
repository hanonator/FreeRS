package org.hannes.rs2.container;

/**
 * 
 * @author red
 *
 */
public enum StackingPolicy {
	
	/**
	 * Never stack items. dunno
	 */
	NEVER,
	
	/**
	 * Only when necessary. this is for regular containers like the player's inventory.
	 */
	WHEN_NECESSARY,
	
	/**
	 * Always stack items, even if they are not stackable in the player's inventory. This is
	 * for containers such as banks or shops.
	 */
	ALWAYS;
}