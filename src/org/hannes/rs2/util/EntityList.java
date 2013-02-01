package org.hannes.rs2.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.hannes.rs2.entity.Entity;

/**
 * A list of entities where each entity has its own index which corresponds with
 * the client index
 * 
 * @author goku
 *
 * @param <T>
 */
public class EntityList<T extends Entity> implements List<T> {

	/**
	 * The array of entities
	 */
	private final Entity[] entities;

	/**
	 * The size of the lits
	 */
	private final int size;

	/**
	 * Creates an entitylist with a given size
	 * 
	 * @param size
	 */
	public EntityList(int size) {
		this.size = size;
		this.entities = new Entity[size];
	}

	@Override
	public boolean add(T element) {
		if (entities[element.getIndex()] == null) {
			entities[element.getIndex()] = element;
			return true;
		}
		return false;
	}

	@Override
	public void add(int index, T element) {
		if (index != element.getIndex()) {
			throw new IllegalArgumentException("entity with index " + element.getIndex() + " attempting to be inserted at index " + index);
		}
		add(element);
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		for (T entity : collection) {
			if (!add(entity)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return addAll(c);
	}

	@Override
	public void clear() {
		for (int i = 0; i < size; i++) {
			entities[i] = null;
		}
	}

	@Override
	public boolean contains(Object o) {
		if (o instanceof Integer) {
			return entities[(Integer) o] != null;
		}
		for (int i = 0; i < size; i++) {
			if (entities[i] == o) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (contains(o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T get(int index) {
		return (T) entities[index];
	}

	@Override
	public int indexOf(Object o) {
		for (Entity entity : entities) {
			if (entity == o) {
				return entity.getIndex();
			}
		}
		throw new IllegalArgumentException("entity not in this list");
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < size; i++) {
			if (entities[i] == null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Iterator<T> iterator() {
		List<T> list = asList();
		return list.iterator();
	}

	private List<T> asList() {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < size; i++) {
			T t = get(i);
			if (t != null) {
				list.add(t);
			}
		}
		return list;
	}

	@Override
	public int lastIndexOf(Object o) {
		return indexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		List<T> list = asList();
		return list.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return listIterator();
	}

	@Override
	public boolean remove(Object o) {
		if (o instanceof Entity) {
			Entity ent = (Entity) o;
			for (Entity entity : entities) {
				if (entity !=  null && entity.getIndex() == ent.getIndex()) {
					remove(entity.getIndex());
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T remove(int index) {
		Entity temp = entities[index];
		entities[index] = null;
		return (T) temp;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		for (Object obj : c) {
			remove(obj);
		}
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		for (Entity entity : entities) {
			if (!c.contains(entity)) {
				if (!remove(entity)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public T set(int index, T element) {
		if (index == element.getIndex()) {
			add(element);
		}
		return element;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object[] toArray() {
		return entities;
	}

	@Override
	public <K> K[] toArray(K[] a) {
		return null;
	}

}