package org.hannes.rs2.entity.sync;

/**
 * A player's appearance
 * 
 * @author red
 *
 */
public class Appearance {

	/**
	 * The icon above the player's head for prayer
	 */
	private int prayerIcon;
	
	/**
	 * The arrow above a player when he is targeted
	 */
	private int hintIcon;
	
	/**
	 * The miscellaneous icon above the player's head (bounty hunter, skull icon, ...)
	 */
	private int miscIcon;
	
	/**
	 * The player's head model
	 */
	private int head;
	
	/**
	 * The player's skin colour
	 */
	private int skinColour;
	
	/**
	 * The player's torso model
	 */
	private int torso;
	
	/**
	 * The player's torso colour
	 */
	private int torsoColour;
	
	/**
	 * The player's arms model
	 */
	private int arms;
	
	/**
	 * The player's arms colour
	 */
	private int armsColour;
	
	/**
	 * The player's hands model (wristbands)
	 */
	private int hands;
	
	/**
	 * The player's hands colour
	 */
	private int handsColour;
	
	/**
	 * The player's legs model
	 */
	private int legs;
	
	/**
	 * The player's legs colour
	 */
	private int legsColour;
	
	/**
	 * The player's feet model
	 */
	private int feet;
	
	/**
	 * The player's feet colour
	 */
	private int feetColour;
	
	/**
	 * The player's gender
	 */
	private int gender;
	
	/**
	 * The player's beard
	 */
	private int beard;
	
	/**
	 * The player's beard colour
	 */
	private int beardColour;

	/**
	 * Default appearance
	 */
	public Appearance() {
		gender = 0;
		head = 7;
		torso = 25;
		arms = 29;
		hands = 35;
		legs = 39;
		feet = 44;
		beard = 14;
		prayerIcon = -1;
		hintIcon = -1;
		miscIcon = -1;
	}

	public int getPrayerIcon() {
		return prayerIcon;
	}

	public void setPrayerIcon(int prayerIcon) {
		this.prayerIcon = prayerIcon;
	}

	public int getHead() {
		return head;
	}

	public void setHead(int head) {
		this.head = head;
	}

	public int getSkinColour() {
		return skinColour;
	}

	public void setSkinColour(int skinColour) {
		this.skinColour = skinColour;
	}

	public int getTorso() {
		return torso;
	}

	public void setTorso(int torso) {
		this.torso = torso;
	}

	public int getTorsoColour() {
		return torsoColour;
	}

	public void setTorsoColour(int torsoColour) {
		this.torsoColour = torsoColour;
	}

	public int getArms() {
		return arms;
	}

	public void setArms(int arms) {
		this.arms = arms;
	}

	public int getArmsColour() {
		return armsColour;
	}

	public void setArmsColour(int armsColour) {
		this.armsColour = armsColour;
	}

	public int getHands() {
		return hands;
	}

	public void setHands(int hands) {
		this.hands = hands;
	}

	public int getHandsColour() {
		return handsColour;
	}

	public void setHandsColour(int handsColour) {
		this.handsColour = handsColour;
	}

	public int getLegs() {
		return legs;
	}

	public void setLegs(int legs) {
		this.legs = legs;
	}

	public int getLegsColour() {
		return legsColour;
	}

	public void setLegsColour(int legsColour) {
		this.legsColour = legsColour;
	}

	public int getFeet() {
		return feet;
	}

	public void setFeet(int feet) {
		this.feet = feet;
	}

	public int getFeetColour() {
		return feetColour;
	}

	public void setFeetColour(int feetColour) {
		this.feetColour = feetColour;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getBeard() {
		return beard;
	}

	public void setBeard(int beard) {
		this.beard = beard;
	}

	public int getBeardColour() {
		return beardColour;
	}

	public void setBeardColour(int beardColour) {
		this.beardColour = beardColour;
	}

	public int getHintIcon() {
		return hintIcon;
	}

	public void setHintIcon(int hintIcon) {
		this.hintIcon = hintIcon;
	}

	public int getMiscIcon() {
		return miscIcon;
	}

	public void setMiscIcon(int miscIcon) {
		this.miscIcon = miscIcon;
	}

}