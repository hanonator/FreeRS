package org.hannes.rs2.entity;

import org.hannes.rs2.action.ActionQueue;
import org.hannes.rs2.container.impl.Equipment;
import org.hannes.rs2.container.impl.Inventory;
import org.hannes.rs2.entity.sync.UpdateFlags.UpdateFlag;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.util.Animations;
import org.hannes.rs2.util.MemoryUsage;

public class Player extends Character {

	/**
	 * This player's connection
	 */
	private final Connection connection;
	
	/**
	 * The player's username
	 */
	private String username;
	
	/**
	 * The clien type
	 */
	private MemoryUsage clientType;
	
	/**
	 * The player's uid
	 */
	private int uid;
	
	/**
	 * The player's inventory
	 */
	private final Inventory inventory = new Inventory(this);
	
	/**
	 * The player's equipment
	 */
	private final Equipment equipment = new Equipment(this);
	
	/**
	 * The player's animations
	 */
	private Animations animations;
	
	/**
	 * The attack index
	 */
	private int attackIndex;
	
	/**
	 * The action queue
	 */
	private final ActionQueue actionQueue = new ActionQueue();

	public Player(int index, Connection connection) {
		super(index);
		this.connection = connection;
		super.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	@Override
	public int getAcquaintanceIndex() {
		return super.getIndex() + 32768;
	}

	public Connection getConnection() {
		return connection;
	}

	public String getUsername() {
		return username;
	}

	public MemoryUsage getClientType() {
		return clientType;
	}

	public int getUid() {
		return uid;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setClientType(MemoryUsage clientType) {
		this.clientType = clientType;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Animations getAnimations() {
		return animations;
	}

	public void setAnimations(Animations animations) {
		this.animations = animations;
	}

	public int getAttackIndex() {
		return attackIndex;
	}

	public void setAttackIndex(int attackIndex) {
		this.attackIndex = attackIndex;
	}

	public ActionQueue getActionQueue() {
		return actionQueue;
	}

}