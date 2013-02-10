package org.hannes.rs2.action;

import java.util.Deque;
import java.util.LinkedList;

import org.hannes.Main;

public class ActionQueue {

	/**
	 * The queue of actions in the actionqueue
	 */
	private final Deque<Action> actions = new LinkedList<>();

	/**
	 * The active action
	 */
	private Action active;

	public void offer(Action action) {
	}

	public void processNextAction() {
	}

	public void clear() {
	}

}