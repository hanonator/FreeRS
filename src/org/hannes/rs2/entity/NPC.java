package org.hannes.rs2.entity;

public class NPC extends Character {

	/**
	 * The type of NPC
	 */
	private int type;

	public NPC(int index) {
		super(index);
	}

	@Override
	public int getAcquaintanceIndex() {
		return super.getIndex();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}