package org.hannes.rs2.container;

import java.util.ArrayList;
import java.util.List;

import org.hannes.observer.Observable;
import org.hannes.observer.Observer;

/**
 * A Container holds several item objects that are used in-game. They are
 * sent to the client n stuff yo
 * 
 * @author red
 */
public class Container {

	/**
	 * The maximum item amount in a single stack
	 * 
	 * TODO: Make this editable in the configuration
	 */
	private static final int MAXIMUM_ITEM_AMOUNT = 999_999_999;

	/**
	 * The observable for the container events
	 */
	private final Observable<ContainerEvent> observable = new Observable<>();

	/**
	 * The capacity of this container
	 */
	private final int size;

	/**
	 * The items in this container
	 */
	private final Item[] items;
	
	/**
	 * The stacking policy
	 */
	private final StackingPolicy policy;
	
	/**
	 * Indicates the container should refresh after minor changes or not
	 */
	private boolean fireUpdate;

	/**
	 * 
	 * @param size
	 * @param policy
	 */
	public Container(int size, StackingPolicy policy) {
		this.size = size;
		this.policy = policy;
		this.items = new Item[size];
		this.fireUpdate = true;
	}

	/**
	 * Sets the item on a given slot
	 * 
	 * @param slot
	 * @param item
	 */
	public synchronized void set(int slot, Item item) {
		items[slot] = item;
		refresh();
	}
	
	/**
	 * Attempts to add an item to the container
	 * 
	 * @param item
	 * @return true if the items have been added succesfully
	 */
	public boolean add(Item item) {
		try {
			if (item == null || item.getId() == -1) {
				return false;
			}
			if ((item.getDefinition().isStackable() || policy == StackingPolicy.ALWAYS) && policy != StackingPolicy.NEVER) {
				int slot = getSlotById(item.getId());
				if (slot != -1) {
					int amount = items[slot].getAmount() + item.getAmount();
					if (amount > MAXIMUM_ITEM_AMOUNT) {
						amount = MAXIMUM_ITEM_AMOUNT;
					}
					set(slot, new Item(item.getId(), amount));
					return true;
				}
				int freeSlot = freeSlot();
				if (freeSlot != -1) {
					set(freeSlot, new Item(item.getId(), item.getAmount()));
					return true;
				}
				return false;
			} else {
				for (int i = 0; i < item.getAmount(); i++) {
					int slot = freeSlot();
					if (slot == -1) {
						return false;
					}
					set(slot, new Item(item.getId(), 1));
				}
				return true;
			}
		} finally {
			pushUpdate(EventType.ADD);
		}
	}

	public boolean add(int id, int count) {
		return add(new Item(id, count));
	}

	/**
	 * Remove an item from the container.
	 * 
	 * @param item
	 * @return
	 */
	public int remove(Item item) {
		int amount = item.getAmount();
		if (item.getDefinition().isStackable() && policy != StackingPolicy.NEVER) {
			return remove(getSlotById(item.getId()), item);
		} else {
			for (int i = 0; i < amount; i++) {
				int slot = getSlotById(item.getId());
				if (slot == -1) {
					return item.getAmount();
				}
				remove(slot, new Item(item.getId(), 1));
				item.setAmount(item.getAmount() - 1);
			}
			return item.getAmount();
		}
	}

	/**
	 * 
	 * @param id
	 * @param amount
	 * @param slot
	 * @return
	 */
	public int remove(int id, int amount, int slot) {
		return remove (slot, new Item(id, amount));
	}

	/**
	 * 
	 * @param id
	 * @param amount
	 * @return
	 */
	public int remove(int id, int amount) {
		return remove (new Item(id, amount));
	}

	/**
	 * Attempts to delete an item on a given slot
	 * 
	 * @param slot
	 * @param item
	 * @return The amount of items that have not been removed
	 */
	public int remove(int slot, Item item) {
		try {
			if (items[slot] != null && item != null && items[slot].getId() == item.getId()) {
				if (items[slot].getAmount() > item.getAmount()) {
					items[slot].setAmount(items[slot].getAmount() - item.getAmount());
					set(slot, items[slot]);
					return 0;
				} else {
					int amount = items[slot].getAmount();
					set(slot, null);
					return item.getAmount() - amount;
				}
			}
			return -1;
		} finally {
			pushUpdate(EventType.REMOVE);
		}
	}

	/**
	 * Removes an item on a given slot
	 * 
	 * @param slot
	 */
	public void remove(int slot) {
		set(slot, null);
		pushUpdate(EventType.REMOVE);
	}

	/**
	 * Replaces the item on a given slot with another
	 * 
	 * @param original
	 * @param replacement
	 * @param slot
	 */
	public void replace(Item original, Item replacement, int slot) {
		if (items[slot] != null && original != null && items[slot].getId() == original.getId()) {
			set(slot, replacement);
		}
		pushUpdate(EventType.ADD);
	}

	/**
	 * Swaps two items
	 * 
	 * @param from
	 * @param to
	 */
	public void swap(int from, int to) {
		Item temp = items[from];
		set(from, items[to]);
		set(to, temp);
		refresh();
	}

	/**
	 * Inserts an item to a given slot and shifts all the other items to the right
	 * 
	 * @param from
	 * @param to
	 */
	public void insert(int from, int to) {
		if (from >= 0 && from < size && to >= 0 && to < size) {
			Item item = items[from];
			set(from, null);
			if (from > to) {
				int shiftFrom = to;
				int shiftTo = from;
				for(int i = (to + 1); i < from; i++) {
					if(items[i] == null) {
						shiftTo = i;
						break;
					}
				}
				Item[] slice = new Item[shiftTo - shiftFrom];
				System.arraycopy(items, shiftFrom, slice, 0, slice.length);
				System.arraycopy(slice, 0, items, shiftFrom + 1, slice.length);
			} else if (from < to) {
				int sliceStart = from + 1;
				int sliceEnd = to;
				for(int i = (sliceEnd - 1); i >= sliceStart; i--) {
					if(items[i] == null) {
						sliceStart = i;
						break;
					}
				}
				Item[] slice = new Item[sliceEnd - sliceStart + 1];
				System.arraycopy(items, sliceStart, slice, 0, slice.length);
				System.arraycopy(slice, 0, items, sliceStart - 1, slice.length);
			}
			set(to, item);
		}
		refresh();
	}

	public void shift() {
		List<Item> list = new ArrayList<Item>();
		for (int index = 0; index < size; index++) {
			if (items[index] != null) {
				list.add(items[index]);
			}
			items[index] = null;
		}
		list.toArray(items);
		refresh();
	}

	/**
	 * Refreshes the container. Used for sending the updated container to the player.
	 */
	public void refresh() {
		pushUpdate(EventType.REFRESH);
	}

	/**
	 * Removes all items from the container and refreshes
	 */
	public void clear() {
		for (int i = 0; i < items.length; i++) {
			set(i, null);
		}
		pushUpdate(EventType.REMOVE);
		refresh();
	}

	/**
	 * Sees if an item in the container has the same id as the given item
	 * 
	 * @param item
	 * @return
	 */
	public boolean contains(Item item) {
		return contains(item.getId());
	}

	/**
	 * Checks to see if there is an item in the container that has the given
	 * item id
	 * 
	 * @param itemId
	 * @return
	 */
	public boolean contains(int itemId) {
		return getSlotById(itemId) != -1;
	}

	/**
	 * The amount of empty slots available
	 * 
	 * @return
	 */
	public int available() {
		int freeSlots = 0;
		for(int i = 0; i < items.length; i++) {
			if(items[i] == null || items[i].getId() == -1) {
				freeSlots++;
			}
		}
		return freeSlots;
	}

	/**
	 * Returns the items as an array
	 * 
	 * @return
	 */
	public Item[] toArray() {
		return items;
	}

	/**
	 * Gets the size of the container
	 * 
	 * @return
	 */
	public int size() {
		return size;
	}

	/**
	 * Get the item at a given slot
	 * 
	 * @param slot
	 * @return
	 */
	public Item get(int slot) {
		return items[slot];
	}

	/**
	 * Search for the first occurrence of a given item
	 * 
	 * @param itemId
	 * @return
	 */
	private int getSlotById(int itemId) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].getId() == itemId) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Searches for the first free slot
	 * 
	 * @return
	 */
	public int freeSlot() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null || items[i].getId() == -1) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @param observer
	 * @see org.hannes.observer.Observable#register(org.hannes.observer.Observer)
	 */
	public void register(Observer<ContainerEvent> observer) {
		observable.register(observer);
	}

	/**
	 * @param observer
	 * @see org.hannes.observer.Observable#unregister(org.hannes.observer.Observer)
	 */
	public void unregister(Observer<ContainerEvent> observer) {
		observable.unregister(observer);
	}

	/**
	 * pushes an update
	 * @param type
	 */
	private void pushUpdate(EventType type) {
		if (fireUpdate) {
			observable.pushUpdate(new ContainerEvent(type, this));
		}
	}

	/**
	 * 
	 * @param itemId
	 * @return
	 */
	public int count(int itemId) {
		int amount = 0;
		for (Item item : items) {
			if (item != null && item.getId() == itemId) {
				amount += item.getAmount();
			}
		}
		return amount;
	}

	public boolean isFireUpdate() {
		return fireUpdate;
	}

	public void setFireUpdate(boolean fireUpdate) {
		this.fireUpdate = fireUpdate;
	}

}