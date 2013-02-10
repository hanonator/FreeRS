package org.hannes.rs2.entity;

import java.awt.Point;

import org.hannes.util.Location;

public class Entity {

	/**
	 * The index of the entity
	 */
	private final int index;
	
	/**
	 * The location of the entity
	 */
	private Location location = new Location(Location.NULL_LOCATION);

	/**
	 * Create a new entity at a given index
	 * 
	 * @param index
	 */
	public Entity(int index) {
		this.index = index;
	}

	public int distance(Location other) {
		return (int) Point.distance(location.getX(), location.getY(), other.getX(), other.getY());
	}

	public int distance(Entity other) {
		return distance(other.location);
	}

	public int getIndex() {
		return index;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}