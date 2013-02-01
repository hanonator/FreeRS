package org.hannes.rs2.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hannes.rs2.content.RegisterEventHandler;
import org.hannes.rs2.content.SpawnEventHandler;
import org.hannes.rs2.content.event.RegisterEvent;
import org.hannes.rs2.content.event.SpawnEvent;

/**
 * FIXME: I have absolutely no control over what handler is attempting to be cast to another.
 * This needs to be fixed when I feel like fixing it. So probably never.
 * 
 * @author red
 *
 */
public class EventHub {

	/**
	 * The event handlers
	 */
	private final Map<Class<?>, List<EventHandler<?>>> map = new HashMap<>();

	/**
	 * Adds the event handlers
	 */
	public void initialize() {
		add(SpawnEvent.class, new SpawnEventHandler());
		add(RegisterEvent.class, new RegisterEventHandler());
	}

	/**
	 * Add a {@link EventHandler}
	 * @param handler
	 */
	public void add(Class<?> c, EventHandler<?> handler) {
		List<EventHandler<?>> list = map.get(c);
		if (list == null) {
			list = new ArrayList<>();
			map.put(c, list);
		}
		list.add(handler);
	}

	/**
	 * 
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	public <T> void offer(T event) throws Exception {
		if (event == null) {
			throw new NullPointerException("event is null");
		}
		
		List<EventHandler<?>> list = map.get(event.getClass());
		if (list == null) {
			return;
		}
		
		for (Iterator<EventHandler<?>> iterator = list.iterator(); iterator.hasNext(); ) {
			EventHandler<T> handler = (EventHandler<T>) iterator.next();
			handler.handleEvent(event);
		}
	}

}