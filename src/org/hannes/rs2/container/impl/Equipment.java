package org.hannes.rs2.container.impl;

import org.hannes.observer.Observable;
import org.hannes.observer.Observer;
import org.hannes.rs2.container.Container;
import org.hannes.rs2.container.ContainerContext;
import org.hannes.rs2.container.ContainerEvent;
import org.hannes.rs2.container.Item;
import org.hannes.rs2.container.StackingPolicy;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.entity.sync.UpdateFlags.UpdateFlag;
import org.hannes.rs2.net.packets.ClientConfig;
import org.hannes.rs2.net.packets.ItemModel;
import org.hannes.rs2.net.packets.Label;
import org.hannes.rs2.net.packets.SidebarInterface;
import org.hannes.rs2.net.packets.TextMessage;
import org.hannes.rs2.util.Animations;
import org.hannes.rs2.util.ItemDefinition;
import org.hannes.rs2.util.WeaponInterface;

/**
 * 
 * 
 * @author red
 *
 */
public class Equipment extends Container implements Observer<ContainerEvent> {

	/**
	 * The attack sidebar configuration, doesn't really belong here
	 * so this will be updated later
	 */
	public static final int ATTACK_CONFIG = 43;

	/**
	 * Size of the container
	 */
	public static final int SIZE = 14;
	
	/**
	 * Interface n stuff
	 */
	public static final int INTERFACE = 1688;
	
	/**
	 * The helmet slot.
	 */
	public static final int SLOT_HELM = 0;
	
	/**
	 * The cape slot.
	 */
	public static final int SLOT_CAPE = 1;
	
	/**
	 * The amulet slot.
	 */
	public static final int SLOT_AMULET = 2;
	
	/**
	 * The weapon slot.
	 */
	public static final int SLOT_WEAPON = 3;
	
	/**
	 * The chest slot.
	 */
	public static final int SLOT_CHEST = 4;
	
	/**
	 * The shield slot.
	 */
	public static final int SLOT_SHIELD = 5;
	
	/**
	 * The bottoms slot.
	 */
	public static final int SLOT_BOTTOMS = 7;
	
	/**
	 * The gloves slot.
	 */
	public static final int SLOT_GLOVES = 9;
	
	/**
	 * The boots slot.
	 */
	public static final int SLOT_BOOTS = 10;
	
	/**
	 * The rings slot.
	 */
	public static final int SLOT_RING = 12;
	
	/**
	 * The arrows slot.
	 */
	public static final int SLOT_ARROWS = 13;
	
	/**
	 * The player
	 */
	private final Player player;

	/**
	 * 
	 */
	public Equipment(Player player) {
		super(SIZE, StackingPolicy.WHEN_NECESSARY);
		
		this.player = player;
		this.register(this);
	}

	@Override
	public void set(int slot, Item item) {
		super.set(slot, item);
		
		/*
		 * Update sidebar interfaces + animations if slot = weapon
		 */
		if (slot == SLOT_WEAPON) {
			updateCharacter(slot, item);
		}
	}

	/**
	 * Updates character animation + sidebar interfaces
	 * 
	 * @param slot
	 * @param item
	 */
	private void updateCharacter(int slot, Item item) {
		/*
		 * The weapon id
		 */
		int weaponId = item == null ? -1  : item.getId();
		
		/*
		 * Get the animation
		 */
		player.setAnimations(Animations.get(weaponId));
		
		/*
		 * Get the weapon interface
		 */
		WeaponInterface weaponInterface = WeaponInterface.get(weaponId);
		
		/*
		 * Send the sidebar
		 */
		player.getConnection().write(new SidebarInterface(SidebarInterface.WEAPON_INDEX, weaponInterface.getInterfaceId()));
		
		/*
		 * Update the attack index
		 */
		player.setAttackIndex(player.getAttackIndex() % weaponInterface.getOptions().length);
		
		/*
		 * Send the config to the client
		 */
		player.getConnection().write(new ClientConfig(ATTACK_CONFIG, player.getAttackIndex()));
		
		/*
		 * Write the weapon name
		 */
		player.getConnection().write(new Label(weaponInterface.getTextId(), ItemDefinition.forId(weaponId).getName()));
		
		/*
		 * Write the item model
		 */
		if (weaponInterface.getModelId() > 0) {
			player.getConnection().write(new ItemModel(weaponId, 200, weaponInterface.getModelId()));
		}
	}

	/**
	 * Resets the equipment interface
	 */
	public void reset() {
		player.getConnection().write(new ContainerContext(this, INTERFACE));
	}

	/**
	 * Unequips an item on a given slot in the equipment
	 * interface
	 * 
	 * @param slot
	 * @return
	 */
	public boolean unequip(int slot) {
		try {
			if (slot >= 0 && slot < SIZE && super.get(slot) != null) {
				super.setFireUpdate(false);
				/*
				 * Get the item
				 */
				Item item = super.get(slot);
				
				/*
				 * See if the item stacks or not  
				 */
				boolean stacks = item.getDefinition().isStackable();
				
				/*
				 * Only add if the player has enough inventory space.
				 */
				if (player.getInventory().available() >= 1 || (stacks && player.getInventory().contains(item.getId()))) {
					/*
					 * Add the item to the player's inventory
					 */
					player.getInventory().add(new Item(item));
					
					/*
					 * Delete the item from the equipment
					 */
					super.remove(slot, item);
					
					/*
					 * item has been succesfully equiped
					 */
					return true;
				} else {
					player.getConnection().write(new TextMessage("You have not enough inventory space to do that."));
					return false;
				}
			}
			return false;
		} finally {
			/*
			 * Refresh the container
			 */
			super.setFireUpdate(true);
			super.refresh();
		}
	}

	/**
	 * 
	 * 
	 * @param slot
	 * @return
	 */
	public void equip(int interfaceId, int itemId, int slot) throws Exception {
		try {
			final Item item = player.getInventory().get(slot);
			
			if (slot >= 0 && slot < player.getInventory().size() && player.getInventory().get(slot) != null) {
				/*
				 * The item definition
				 */
				final ItemDefinition definition = item.getDefinition();
				
				/*
				 * Do not update until everything is complete
				 */
				super.setFireUpdate(false);

				/*
				 * Get the target slot
				 */
				int target = definition.getEquipmentSlot();
				
				/*
				 * -1 = unwearable item
				 */
				if (target == -1) {
					player.getConnection().write(new TextMessage("I can't wear that."));
					return;
				}
				
				/*
				 * Get the item definition of the held item
				 */
				ItemDefinition heldItem = ItemDefinition.forId(getId(SLOT_WEAPON));
				
				/*
				 * twohand inmpem 
				 */
				boolean twoHanded = (target == SLOT_WEAPON || target == SLOT_SHIELD) && (definition.isTwoHanded() || heldItem.isTwoHanded());
				
				/*
				 * If the player has no room for both the items, don't continue
				 */
				if (twoHanded && super.get(SLOT_WEAPON) != null && super.get(SLOT_SHIELD) != null && player.getInventory().available() <= 0) {
					player.getConnection().write(new TextMessage("You have not enough inventory space to do that."));
					return;
				}

				/*
				 * Replace the item from the inventory with the item in the
				 * equipment.
				 */
				replace(player.getInventory(), this, slot, target);

				/*
				 * If the target slot is the shield slot, this means the player
				 * is already wielding a two handed weapon and we just move it
				 * to the inventory
				 */
				if (twoHanded && target == SLOT_SHIELD && getId(SLOT_WEAPON) >= 0) {
					replace(player.getInventory(), this, slot, SLOT_WEAPON);
				}

				/*
				 * If the target slot is weapon, we move the shield to the first
				 * available free slot.
				 */
				if (twoHanded && target == SLOT_WEAPON && getId(SLOT_SHIELD) >= 0) {
					replace(player.getInventory(), this, player.getInventory().freeSlot(), SLOT_SHIELD);
				}
			}
		} finally {
			/*
			 * Refresh the container
			 */
			super.setFireUpdate(true);
			super.refresh();
		}
	}

	/**
	 * 
	 * @param inventory
	 * @param equipment
	 * @param inventorySlot
	 * @param equipmentSlot
	 */
	public void replace(Container inventory, Container equipment, int inventorySlot, int equipmentSlot) {
		Item inventoryItem = inventory.get(inventorySlot);
		Item equipmentItem = equipment.get(equipmentSlot);
		
		if (inventoryItem == null && equipmentItem == null) {
			return;
		} else if ((equipmentItem == null && inventoryItem != null) || (inventoryItem == null && equipmentItem != null)) {
			equipment.set(equipmentSlot, inventoryItem);
			inventory.set(inventorySlot, equipmentItem);
		} else {
			ItemDefinition def1 = inventoryItem.getDefinition();
			ItemDefinition def2 = equipmentItem.getDefinition();
			
			if (def2.isStackable() && inventoryItem.getId() != equipmentItem.getId() && inventory.contains(equipmentItem.getId())) {
				inventory.add(equipmentItem);
				equipment.set(equipmentSlot, inventoryItem);
			} else if (def1.isStackable() && inventoryItem.getId() == equipmentItem.getId()) {
				equipment.add(inventoryItem);
				inventory.set(inventorySlot, null);
			} else {
				equipment.set(equipmentSlot, inventoryItem);
				inventory.set(inventorySlot, equipmentItem);
			}
		}
	}

	@Override
	public void update(Observable<ContainerEvent> observable, ContainerEvent object) throws Exception {
		/*
		 * The player has to get his appearance update to show the updated character model
		 */
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		
		/*
		 * Send the items to the interface
		 */
		player.getConnection().write(new ContainerContext(this, INTERFACE));
	}

	@Override
	public void exceptionCaught(Observable<ContainerEvent> observable, Throwable exception) {
		exception.printStackTrace();
	}

	/**
	 * Gets the id of the equipment, -1 if nothing is equipped
	 * 
	 * @param index
	 * @return
	 */
	public int getId(int index) {
		return get(index) == null ? -1 : get(index).getId();
	}

}