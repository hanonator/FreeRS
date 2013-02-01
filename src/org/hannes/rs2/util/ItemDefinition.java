package org.hannes.rs2.util;


/**
 * TODO: This is hyperion and has to go ASAP, but I'm using it cuz I'm lazy
 * 
 * @author Hyperion
 */
public class ItemDefinition {
	
	/**
	 * The default item definition
	 */
	private static final ItemDefinition DEFAULT_DEFINITION = new ItemDefinition(-1, "Unarmed", "", false, false, false, 0, 0, false, 0, 0, 0, new int[5], new int[5], 0, 0, 0, 0, -1, false);
	
	/**
	 * The definition array.
	 */
	private static ItemDefinition[] definitions;
	
	/**
	 * Gets a definition for the specified id.
	 * @param id The id.
	 * @return The definition.
	 */
	public static ItemDefinition forId(int id) {
		return id >= 0 && id < definitions.length ? definitions[id] : DEFAULT_DEFINITION;
	}
	
	/**
	 * Id.
	 */
	private final int id;
	
	/**
	 * Name.
	 */
	private final String name;
	
	/**
	 * Description.
	 */
	private final String examine;
	
	/**
	 * Noted flag.
	 */
	private final boolean noted;
	
	/**
	 * Noteable flag.
	 */
	private final boolean noteable;
	
	/**
	 * Stackable flag.
	 */
	private final boolean stackable;
	
	/**
	 * Non-noted id.
	 */
	private final int parentId;
	
	/**
	 * Noted id.
	 */
	private final int notedId;
	
	/**
	 * Members flag.
	 */
	private final boolean members;
	
	/**
	 * Shop value.
	 */
	private final int shopValue;
	
	/**
	 * High alc value.
	 */
	private final int highAlcValue;
	
	/**
	 * Low alc value.
	 */
	private final int lowAlcValue;
	
	/**
	 * The defence bonuses
	 */
	private final int defenceBonus[];
	
	/**
	 * The attack bonuses
	 */
	private final int attackBonus[];
	
	/**
	 * The range strength
	 */
	private final int rangeStrength;
	
	/**
	 * The strength bonus
	 */
	private final int strength;
	
	/**
	 * The prayer bonus
	 */
	private final int prayer;
	
	/**
	 * Indicates
	 */
	private final int mask;

	/**
	 * The slot the item is equiped
	 */
	private int equipmentSlot;

	/**
	 * Indicates the item is held in two hands
	 */
	private final boolean twoHanded;
	
	/**
	 * Creates the item definition.
	 * @param id The id.
	 * @param name The name.
	 * @param examine The description.
	 * @param noted The noted flag.
	 * @param noteable The noteable flag.
	 * @param stackable The stackable flag.
	 * @param parentId The non-noted id.
	 * @param notedId The noted id.
	 * @param members The members flag.
	 * @param shopValue The shop price.
	 * @param highAlcValue The high alc value.
	 * @param lowAlcValue The low alc value.
	 */
	public ItemDefinition(int id, String name, String examine, boolean noted, boolean noteable, boolean stackable, int parentId, int notedId, boolean members, int shopValue, int highAlcValue, int lowAlcValue,  int attackBonus[], int defenceBonus[], int rangeStrength, int strength, int prayer, int mask, int equipmentSlot, boolean twoHanded) {
		this.id = id;
		this.name = name;
		this.examine = examine;
		this.noted = noted;
		this.noteable = noteable;
		this.stackable = stackable;
		this.parentId = parentId;
		this.notedId = notedId;
		this.members = members;
		this.shopValue = shopValue;
		this.highAlcValue = highAlcValue;
		this.lowAlcValue = lowAlcValue;
		this.defenceBonus = defenceBonus;
		this.attackBonus = attackBonus;
		this.rangeStrength = rangeStrength;
		this.strength = strength;
		this.prayer = prayer;
		this.mask = mask;
		this.setEquipmentSlot(equipmentSlot);
		this.twoHanded = twoHanded;
	}
	
	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the description.
	 * @return The description.
	 */
	public String getDescription() {
		return examine;
	}
	
	/**
	 * Gets the noted flag.
	 * @return The noted flag.
	 */
	public boolean isNoted() {
		return noted;
	}
	
	/**
	 * Gets the noteable flag.
	 * @return The noteable flag.
	 */
	public boolean isNoteable() {
		return noteable;
	}
	
	/**
	 * Gets the stackable flag.
	 * @return The stackable flag.
	 */
	public boolean isStackable() {
		return stackable || noted;
	}
	
	/**
	 * Gets the normal id.
	 * @return The normal id.
	 */
	public int getNormalId() {
		return parentId;
	}
	
	/**
	 * Gets the noted id.
	 * @return The noted id.
	 */
	public int getNotedId() {
		return notedId;
	}
	
	/**
	 * Gets the members only flag.
	 * @return The members only flag.
	 */
	public boolean isMembersOnly() {
		return members;
	}
	
	/**
	 * Gets the value.
	 * @return The value.
	 */
	public int getValue() {
		return shopValue;
	}
	
	/**
	 * Gets the low alc value.
	 * @return The low alc value.
	 */
	public int getLowAlcValue() {
		return lowAlcValue;
	}
	
	/**
	 * Gets the high alc value.
	 * @return The high alc value.
	 */
	public int getHighAlcValue() {
		return highAlcValue;
	}

	/**
	 * @return the examine
	 */
	public String getExamine() {
		return examine;
	}

	/**
	 * @return the parentId
	 */
	public int getParentId() {
		return parentId;
	}

	/**
	 * @return the members
	 */
	public boolean isMembers() {
		return members;
	}

	/**
	 * @return the shopValue
	 */
	public int getShopValue() {
		return shopValue;
	}

	/**
	 * @return the defenceBonus
	 */
	public int[] getDefenceBonus() {
		return defenceBonus;
	}

	/**
	 * @return the attackBonus
	 */
	public int[] getAttackBonus() {
		return attackBonus;
	}

	/**
	 * @return the rangeStrength
	 */
	public int getRangeStrength() {
		return rangeStrength;
	}

	/**
	 * @return the strength
	 */
	public int getStrength() {
		return strength;
	}

	/**
	 * @return the prayer
	 */
	public int getPrayer() {
		return prayer;
	}

	public static void set(ItemDefinition[] definitions) {
		ItemDefinition.definitions = definitions;
	}

	public int getMask() {
		return mask;
	}

	public boolean isTwoHanded() {
		return twoHanded;
	}

	public int getEquipmentSlot() {
		return equipmentSlot;
	}

	public void setEquipmentSlot(int equipmentSlot) {
		this.equipmentSlot = equipmentSlot;
	}

}