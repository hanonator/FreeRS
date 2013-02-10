package org.hannes.rs2.action;

import org.hannes.rs2.entity.Player;
import org.hannes.util.GameEngine;
import org.hannes.util.Task;

public abstract class Action implements Task {

	/**
	 * Indicates the action is finished
	 */
	private boolean finished;
	
	/**
	 * The player that is executing this task
	 */
	private final Player player;

	public Action(Player player) {
		this.player = player;
	}
	
	public abstract boolean doAction(Player player) throws Exception;

	@Override
	public boolean execute(GameEngine engine) throws Exception {
		if (!finished) {
			finished = doAction(player);
		}
		return finished;
	}

	public boolean finished() {
		return finished;
	}
	
	public void stop() {
		finished = true;
	}
	
}