package org.hannes.rs2.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.Element;
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
	 * The logger
	 */
	private static final Logger logger = Logger.getLogger(EventHub.class.getName());
	
	/**
	 * The system class loader
	 */
	private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

	/**
	 * The event handlers
	 */
	private final Map<Class<?>, List<EventHandler<?>>> map = new HashMap<>();

	/**
	 * TODO: Holy shit this is bad
	 * 
	 * @throws Exception
	 */
	public void initialize(Document document) throws Exception {
		/*
		 * The root element
		 */
		Element root = document.getRootElement();
		
		/*
		 * Parse all packet information
		 */
		for (Iterator<Element> iterator = root.elementIterator("handler"); iterator.hasNext(); ) {
			Element element = iterator.next();
			
			String className = element.attributeValue("class");
			String handlerName = element.getText();
			
			add((Class<?>) classLoader.loadClass(className), (EventHandler<?>) classLoader.loadClass(handlerName).newInstance());
			logger.info("eventhandler [" + handlerName + "] added for " + className);
		}
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