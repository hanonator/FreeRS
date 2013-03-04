package org.hannes.rs2.entity;

import org.hannes.rs2.action.ActionQueue;
import org.hannes.rs2.container.impl.Equipment;
import org.hannes.rs2.container.impl.Inventory;
import org.hannes.rs2.content.channel.Channel;
import org.hannes.rs2.content.misc.MakeAction;
import org.hannes.rs2.entity.sync.Animation;
import org.hannes.rs2.entity.sync.ForcedMovement;
import org.hannes.rs2.entity.sync.UpdateFlags.UpdateFlag;
import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.Serializable;
import org.hannes.rs2.util.Animations;
import org.hannes.rs2.util.Cooldowns;
import org.hannes.rs2.util.Cooldowns.Cooldown;
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
	
	/**
	 * The player's cooldowns
	 */
	private final Cooldowns cooldowns = new Cooldowns();
	
	/**
	 * The make action
	 */
	private MakeAction makeAction;
	
	/**
	 * The character's forced movement
	 */
	private ForcedMovement forcedMovement;

	public Player(int index, Connection connection) {
		super(index);
		this.connection = connection;
		super.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	public void move(ForcedMovement movement, Animation animation) {
		this.forcedMovement = movement;
		this.forcedMovement.getSource().localize(this);
		this.forcedMovement.getDestination();
		
		/*
		 * Add to the player's walking queue
		 */
		super.getWalkingQueue().addStep(forcedMovement.getDestination().getX(),
				forcedMovement.getDestination().getY());
		
		/*
		 * Delocalize the destination
		 */
		forcedMovement.getDestination().localize(this);
		
		/*
		 * Calculate the amount of ticks the movement will last
		 */
		cooldowns.set(Cooldown.MOVEMENT, 1
				+ (movement.getSource().getX() - movement.getDestination().getX())
				+ (movement.getSource().getY() - movement.getDestination().getY()));
		
		/*
		 * Play animation if necessary
		 */
		if (animation != null) {
			setAnimation(animation);
		}
		
		/*
		 * Set the forced movement flag
		 */
		super.getUpdateFlags().flag(UpdateFlag.FORCE_WALK);
	}

	public void write(Message msg) {
		connection.write(msg);
	}

	public void write(Serializable pkt) {
		connection.write(pkt);
	}

	public void move(ForcedMovement movement) {
		this.move(movement, null);
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

	public MakeAction getMakeAction() {
		return makeAction;
	}

	public void setMakeAction(MakeAction makeAction) {
		this.makeAction = makeAction;
	}

	public ForcedMovement getForcedMovement() {
		return forcedMovement;
	}

	public void setForcedMovement(ForcedMovement forcedMovement) {
		this.forcedMovement = forcedMovement;
	}

	public Cooldowns getCooldowns() {
		return cooldowns;
	}

}