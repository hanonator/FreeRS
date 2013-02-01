package org.hannes.util;

/**
 * A task that will be executed after a certain amount of ticks
 * 
 * @author goku
 */
public abstract class TimedTask implements Task {
	
	/**
	 * The time after which the task should be executed
	 */
	private final long time;

	/**
	 * The timer of this scheduled task
	 */
	private Timer timer;

	/**
	 * 
	 * @param unit
	 * @param time
	 * @param priority
	 */
	public TimedTask(long time) {
		this.time = time;
	}

	/**
	 * 
	 * 
	 * @param engine
	 * @return
	 * @throws Exception
	 */
	public abstract boolean cycle(GameEngine engine) throws Exception;

	@Override
	public boolean execute(GameEngine engine) throws Exception {
		/*
		 * If the timer is null, create a new instance and clock the current time
		 */
		if (timer == null) {
			timer = new Timer(engine);
			timer.clock();
		}
		/*
		 * If the task is not 
		 */
		if (!timer.check(time)) {
			return false;
		}
		
		/*
		 * clock the timer in case a second execution is necessary
		 */
		timer.clock();
		
		/*
		 * Execute this cycle
		 */
		return cycle(engine);
	}

}