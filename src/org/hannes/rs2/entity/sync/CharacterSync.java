package org.hannes.rs2.entity.sync;

import org.hannes.rs2.entity.Player;
import org.hannes.rs2.net.Message;

/**
 * Synchronize a type of character n stuff
 * 
 * @author red
 *
 */
public interface CharacterSync {

	/**
	 * Synchronize all the characters of a given type for a specific player
	 * 
	 * @param player
	 */
	public abstract Message synchronize(Player player);

}