package org.hannes.rs2.container;


/**
 * A container event
 * 
 * @author red
 *
 */
public class ContainerEvent {

	/**
	 * The type of event
	 */
	private final EventType eventType;

	/**
	 * The container
	 */
	private final Container container;

	public ContainerEvent(EventType eventType, Container container) {
		this.eventType = eventType;
		this.container = container;
	}

	/**
	 * @return the eventType
	 */
	public EventType getEventType() {
		return eventType;
	}

	/**
	 * @return the container
	 */
	public Container getContainer() {
		return container;
	}

}