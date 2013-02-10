package org.hannes.rs2.util;

import org.hannes.util.Location;

/**
 * Represents a spawn area
 * 
 * @author red
 *
 */
public class SpawnArea {

	/**
	 * The center of the spawn area
	 */
	private final Location center;
	
	/**
	 * The radius
	 */
	private final int radius;

	public SpawnArea(Location center, int radius) {
		this.center = center;
		this.radius = radius;
	}

	public Location getCenter() {
		return center;
	}

	public int getRadius() {
		return radius;
	}
	
}