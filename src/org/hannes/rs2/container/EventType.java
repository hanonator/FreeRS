package org.hannes.rs2.container;


public enum EventType {
	
	/**
	 * Indicates an item has been added
	 */
	ADD,
	
	/**
	 * Indicates an item has been removed
	 */
	REMOVE,
	
	/**
	 * Indicates a refresh without any updates
	 */
	REFRESH;
}