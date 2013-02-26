package org.hannes.rs2.action;

import org.hannes.rs2.entity.Player;
import org.hannes.util.GameEngine;
import org.hannes.util.TimedTask;

public abstract class Action extends TimedTask {

	/**
	 * Indicates the action is finished
	 */
	private boolean finished;
	
	/**
	 * The player that is executing this task
	 */
	private final Player player;

	public Action(Player player) {
		this(1, player);
	}

	public Action(long time, Player player) {
		super(time);
		this.player = player;
	}
	
	public abstract boolean doAction(Player player) throws Exception;

	@Override
	public boolean cycle(GameEngine engine) throws Exception {
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