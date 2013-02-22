package org.hannes.rs2.action;

import java.util.LinkedList;
import java.util.Queue;

import org.hannes.Main;

/**
 * Some stuff is "borrowed" from teh gramz0r_
 * @author red
 *
 */
public class ActionQueue {
	
	/**
	 * A queue of <code>Action</code> objects.
	 */
	private final Queue<Action> queuedActions = new LinkedList<Action>();
	
	/**
	 * The current action.
	 */
	private Action currentAction = null;

	public void offer(Action action) {
		queuedActions.add(action);
		processNextAction();
	}

	public void processNextAction() {
		if(currentAction != null) {
			if(!currentAction.finished()) {
				return;
			} else {
				currentAction = null;
			}
		}
		if(queuedActions.size() > 0) {
			currentAction = queuedActions.poll();
			Main.getEngine().submit(currentAction);
		}
	}

	public void clear() {
		for(Action actionEvent : queuedActions) {
			actionEvent.stop();
		}
		queuedActions.clear();
		if(currentAction != null)
			currentAction.stop();
		currentAction = null;
	}

}