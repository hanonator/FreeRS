package org.hannes.rs2.util;


/**
 * Indicates what type of client the user is running.
 * 
 * @author red
 *
 */
public enum MemoryUsage {
	
	/**
	 * High memory client. This does have the music tab
	 */
	HIGH(),

	/**
	 * Low memory client. This does not have the music tab
	 */
	LOW(),

}