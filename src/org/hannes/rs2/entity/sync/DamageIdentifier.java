package org.hannes.rs2.entity.sync;

/**
 * 
 * 
 * @author red
 *
 */
public class DamageIdentifier {

	/**
	 * The amount of damage
	 */
	private final int damage;
	
	/**
	 * The sprite that shows the kind of damage inflicted
	 */
	private final Sprite sprite;

	/**
	 * Creates a new identifier with red damage sprite 
	 * if the damage is > 0 or a blue if the damage = 0
	 * 
	 * @param damage
	 */
	public DamageIdentifier(int damage) {
		this(damage, damage == 0 ? Sprite.ZERO_DAMAGE : Sprite.REGULAR_DAMAGE);
	}

	/**
	 * 
	 * @param damage
	 * @param hitType
	 */
	public DamageIdentifier(int damage, Sprite hitType) {
		this.damage = damage;
		this.sprite = hitType;
	}

	public int getDamage() {
		return damage;
	}

	public Sprite getHitType() {
		return sprite;
	}

	/**
	 * The sprite in which the amount of damage is displayed on the character
	 * 
	 * @author red
	 *
	 */
	public static enum Sprite {
		/**
		 * A blue sprite, for when no damage is inflicted
		 */
		ZERO_DAMAGE,
		
		/**
		 * A red sprite, for when regular damage is inflicted
		 */
		REGULAR_DAMAGE,
		
		/**
		 * A green sprite for when poison damage is inflicted
		 */
		POISON_DAMAGE;
	}

}