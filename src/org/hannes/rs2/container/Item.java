package org.hannes.rs2.container;

import org.hannes.rs2.util.ItemDefinition;

/**
 * 
 * @author red
 *
 */
public class Item {

	/**
	 * The item id
	 */
	private int id;
	
	/**
	 * The amount of this item
	 */
	private int amount;

	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	public Item(Item item) {
		this.id = item.getId();
		this.amount = item.getAmount();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public ItemDefinition getDefinition() {
		return ItemDefinition.forId(id);
	}

}