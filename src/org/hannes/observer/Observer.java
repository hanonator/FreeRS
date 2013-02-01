package org.hannes.observer;

/**
 * Observer
 * 
 * @author goku
 *
 * @param <T>
 */
public interface Observer<T> {

	/**
	 * Called when an update has been received
	 * 
	 * @param observable
	 * @param object
	 */
	public abstract void update(Observable<T> observable, T object) throws Exception;

	/**
	 * Called when an exception has been caught in the observer
	 * 
	 * @param observable
	 * @param exception
	 */
	public abstract void exceptionCaught(Observable<T> observable, Throwable exception);

}