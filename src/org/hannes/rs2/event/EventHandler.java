package org.hannes.rs2.event;


/**
 * 
 * @author red
 *
 * @param <T>
 */
public interface EventHandler<T> {

	/**
	 * 
	 * @param event
	 * @param player
	 * @throws Exception
	 */
	public abstract void handleEvent(T event) throws Exception;

}