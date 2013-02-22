package org.hannes.rs2.content.misc;

import org.hannes.rs2.action.Action;
import org.hannes.rs2.entity.Player;

/**
 * Make action n shit
 * 
 * @author red
 *
 */
public abstract class MakeAction extends Action {
	
	/**
	 * The chosen amount
	 */
	private int amount;
	
	/**
	 * The chosen slot
	 */
	private int slot;

	public MakeAction(Player player) {
		super(player);
	}

	/**
	 * Do action for make actions
	 * 
	 * @param player
	 * @param slot
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public abstract boolean doAction(Player player, int slot, int amount) throws Exception;

	@Override
	public boolean doAction(Player player) throws Exception {
		return doAction(player, slot, amount);
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

}