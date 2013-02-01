package org.hannes.rs2.util;

import java.util.HashMap;
import java.util.Map;

public class Animations {

	/**
	 * The map containing all the animations
	 */
	private static final Map<Integer, Animations> animations = new HashMap<>();

	/**
	 * The index of the animation when idle
	 */
	private final int idleAnimation;

	/**
	 * The index of the animation when blocking an attack
	 */
	private final int blockAnimation;

	/**
	 * The index of the animation when walking
	 */
	private final int walkAnimation;
	
	/**
	 * The index of the animation when running
	 */
	private final int runAnimation;

	/**
	 * The index of the animation when turning 180 degrees
	 */
	private final int turn180Animation;
	
	/**
	 * The index of the animation when turning 90 degrees clockwise
	 */
	private final int turn90CWAnimation;
	
	/**
	 * The index of the animation when turning 90 degrees counter clockwise
	 */
	private final int turn90CCWAnimation;

	/**
	 * @param idleAnimation
	 * @param blockAnimation
	 * @param walkAnimation
	 * @param runAnimation
	 * @param turn180Animation
	 * @param turn90cwAnimation
	 * @param turn90ccwAnimation
	 */
	public Animations() {
		this.idleAnimation = -1;
		this.blockAnimation = -1;
		this.walkAnimation = -1;
		this.runAnimation = -1;
		this.turn180Animation = -1;
		this.turn90CWAnimation = -1;
		this.turn90CCWAnimation = -1;
	}

	/**
	 * @param idleAnimation
	 * @param blockAnimation
	 * @param walkAnimation
	 * @param runAnimation
	 * @param turn180Animation
	 * @param turn90cwAnimation
	 * @param turn90ccwAnimation
	 */
	public Animations(int idleAnimation, int blockAnimation, int walkAnimation, int runAnimation, int turn180Animation, int turn90cwAnimation, int turn90ccwAnimation) {
		this.idleAnimation = idleAnimation;
		this.blockAnimation = blockAnimation;
		this.walkAnimation = walkAnimation;
		this.runAnimation = runAnimation;
		this.turn180Animation = turn180Animation;
		this.turn90CWAnimation = turn90cwAnimation;
		this.turn90CCWAnimation = turn90ccwAnimation;
	}

	/**
	 * @return the animations
	 */
	public static Map<Integer, Animations> getAnimations() {
		return animations;
	}

	/**
	 * @return the idleAnimation
	 */
	public int getIdleAnimation() {
		return idleAnimation;
	}

	/**
	 * @return the blockAnimation
	 */
	public int getBlockAnimation() {
		return blockAnimation;
	}

	/**
	 * @return the walkAnimation
	 */
	public int getWalkAnimation() {
		return walkAnimation;
	}

	/**
	 * @return the runAnimation
	 */
	public int getRunAnimation() {
		return runAnimation;
	}

	/**
	 * @return the turn180Animation
	 */
	public int getTurn180Animation() {
		return turn180Animation;
	}

	/**
	 * @return the turn90CWAnimation
	 */
	public int getTurn90CWAnimation() {
		return turn90CWAnimation;
	}

	/**
	 * @return the turn90CCWAnimation
	 */
	public int getTurn90CCWAnimation() {
		return turn90CCWAnimation;
	}

	public static Animations get(int i) {
		return animations.get(i);
	}
	
}