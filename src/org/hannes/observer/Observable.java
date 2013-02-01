package org.hannes.observer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author goku
 * 
 * @param <T>
 */
public class Observable<T> {

	/**
	 * The inactive observers
	 */
	protected final List<Observer<T>> canceled = new ArrayList<Observer<T>>();

	/**
	 * The active observers
	 */
	protected final List<Observer<T>> observers = new ArrayList<Observer<T>>();

	/**
	 * Send a message to all observers
	 * 
	 * @param object
	 */
	public void pushUpdate(T object) {
		/*
		 * Remove any inactive observers
		 */
		for (Iterator<Observer<T>> it$ = canceled.iterator(); it$.hasNext();) {
			observers.remove(it$.next());
			it$.remove();
		}
		
		/*
		 * Push the update
		 */
		for (Iterator<Observer<T>> it$ = observers.iterator(); it$.hasNext();) {
			Observer<T> observer = it$.next();
			
			try {
				observer.update(this, object);
			} catch (Exception ex) {
				observer.exceptionCaught(this, ex);
			}
		}
	}

	/**
	 * Remove all observers from this observable
	 */
	public void clear() {
		canceled.addAll(observers);
	}

	/**
	 * Register a new observer
	 * 
	 * @param observer
	 */
	public void register(Observer<T> observer) {
		observers.add(observer);
	}

	/**
	 * Unregister an observer
	 * 
	 * @param observer
	 */
	public void unregister(Observer<T> observer) {
		canceled.add(observer);
	}

}