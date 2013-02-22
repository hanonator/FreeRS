package org.hannes.util;

/**
 * An interface that represents a task
 * 
 * @author red
 */
public interface Task {

	/**
	 * Executes the task
	 * 
	 * @param engine
	 * @return False if the task has not been finished successfully and should be submitted again
	 * @throws Exception
	 */
	public abstract boolean execute(GameEngine engine) throws Exception;

}