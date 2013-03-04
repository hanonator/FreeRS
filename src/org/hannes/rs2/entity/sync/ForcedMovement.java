package org.hannes.rs2.entity.sync;

import org.hannes.rs2.util.Direction;
import org.hannes.util.Location;

public class ForcedMovement {

	/**
	 * The source location
	 */
	private final Location source;
	
	/**
	 * The destination location
	 */
	private final Location destination;
	
	/**
	 * The primary velocity
	 */
	private final int primaryVelocity;
	
	/**
	 * The secondary velocity
	 */
	private final int secondaryVelocity;
	
	/**
	 * The direction the character is facing
	 */
	private final Direction direction;

	public ForcedMovement(Location source, Location destination, int primaryVelocity, int secondaryVelocity, Direction direction) {
		this.source = source;
		this.destination = destination;
		this.primaryVelocity = primaryVelocity;
		this.secondaryVelocity = secondaryVelocity;
		this.direction = direction;
	}

	public Location getSource() {
		return source;
	}

	public Location getDestination() {
		return destination;
	}

	public int getSecondaryVelocity() {
		return secondaryVelocity;
	}

	public Direction getDirection() {
		return direction;
	}

	public int getPrimaryVelocity() {
		return primaryVelocity;
	}

}