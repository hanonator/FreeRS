package org.hannes.rs2.container.impl;

import org.hannes.observer.Observable;
import org.hannes.observer.Observer;
import org.hannes.rs2.container.Container;
import org.hannes.rs2.container.ContainerContext;
import org.hannes.rs2.container.ContainerEvent;
import org.hannes.rs2.container.Item;
import org.hannes.rs2.container.StackingPolicy;
import org.hannes.rs2.entity.Player;

/**
 * 
 * @author red
 *
 */
public class Inventory extends Container implements Observer<ContainerEvent> {
	
	/**
	 * The size of the inventory
	 */
	public static final int SIZE = 28;
	
	/**
	 * The interface id
	 */
	public static final int INTERFACE_ID = 3214;
	
	/**
	 * The player
	 */
	private final Player player;

	public Inventory(Player player) {
		super (SIZE, StackingPolicy.WHEN_NECESSARY);
		
		this.player = player;
		this.register(this);
	}

	@Override
	public void set(int slot, Item item) {
		super.set(slot, item);
	}

	public void load(int slot, Item item) {
		super.set(slot, item);
	}

	/**
	 * Resets the equipment interface
	 */
	public void reset() {
		player.getConnection().write(new ContainerContext(this, INTERFACE_ID));
	}

	@Override
	public void update(Observable<ContainerEvent> observable, ContainerEvent object) throws Exception {
		switch (object.getEventType()) {
		default:
			player.getConnection().write(new ContainerContext(this, INTERFACE_ID));
			break;
		}
	}

	@Override
	public void exceptionCaught(Observable<ContainerEvent> observable, Throwable exception) {
		exception.printStackTrace();
	}
	
}