package org.hannes.rs2.entity.sync;

import java.util.BitSet;

/**
 * nicked graham's updateflags lol
 * 
 * @author Graham Edgecombe
 *
 */
public class UpdateFlags {

	public static enum UpdateFlag {
		
		/**
		 * Appearance update.
		 */
		APPEARANCE,
		
		/**
		 * Chat update.
		 */
		CHAT,
		
		/**
		 * Graphics update.
		 */
		GRAPHICS,
		
		/**
		 * Animation update.
		 */
		ANIMATION,
		
		/**
		 * Forced chat update.
		 */
		FORCED_CHAT,
		
		/**
		 * Interacting entity update.
		 */
		FACE_ENTITY,
		
		/**
		 * Face coordinate entity update.
		 */
		FACE_COORDINATE,
		
		/**
		 * Hit update.
		 */
		HIT,
		
		/**
		 * Hit 2 update/
		 */
		HIT_2,
		
		/**
		 * Update flag used to transform npc to another.
		 */
		TRANSFORM,
		
		/**
		 * Forces the player to walk from a given coordinate to a given coordinate.
		 */
		FORCE_WALK
	}

	private final BitSet flags = new BitSet();

	/**
	 * Checks if an update required.
	 * @return <code>true</code> if 1 or more flags are set,
	 * <code>false</code> if not.
	 */
	public boolean isUpdateRequired() {
		return !flags.isEmpty();
	}
	
	/**
	 * Flags (sets to true) a flag.
	 * @param flag The flag to flag.
	 */
	public void flag(UpdateFlag flag) {
		flags.set(flag.ordinal(), true);
	}
	
	/**
	 * Sets a flag.
	 * @param flag The flag.
	 * @param value The value.
	 */
	public void set(UpdateFlag flag, boolean value) {
		flags.set(flag.ordinal(), value);
	}
	
	/**
	 * Gets the value of a flag.
	 * @param flag The flag to get the value of.
	 * @return The flag value.
	 */
	public boolean get(UpdateFlag flag) {
		return flags.get(flag.ordinal());
	}
	
	/**
	 * Resest all update flags.
	 */
	public void reset() {
		flags.clear();
	}

}