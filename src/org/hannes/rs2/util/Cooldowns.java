package org.hannes.rs2.util;

import java.util.HashMap;
import java.util.Map;

import org.hannes.Main;
import org.hannes.util.GameEngine;
import org.hannes.util.Timer;

public class Cooldowns {
	
	/**
	 * The collection of cooldowns
	 */
	private final Map<Cooldown, FixedTimer> cooldowns = new HashMap<>();

	public void clock(Cooldown cooldown) {
		FixedTimer timer = cooldowns.get(cooldown);
		if (timer == null) {
			timer = new FixedTimer(Main.getEngine());
			cooldowns.put(cooldown, timer);
		}
		timer.clock();
	}

	public void set(Cooldown cooldown, long time) {
		clock(cooldown);
		cooldowns.get(cooldown).delay = time;
	}

	public boolean check(Cooldown cooldown) {
		FixedTimer timer = cooldowns.get(cooldown);
		return timer == null || timer.check();
	}

	private static class FixedTimer extends Timer {

		/**
		 * The delay
		 */
		private long delay;

		public FixedTimer(GameEngine engine) {
			this(engine, 1);
		}

		public FixedTimer(GameEngine engine, int delay) {
			super(engine);
		}
		
		public boolean check() {
			return super.check(delay);
		}
		
	}

	public static enum Cooldown {
		MOVEMENT
	}

}