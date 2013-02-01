package org.hannes.rs2.entity.sync;

import java.util.Deque;
import java.util.LinkedList;

import org.hannes.rs2.entity.Character;
import org.hannes.rs2.util.Direction;
import org.hannes.rs2.util.DirectionUtils;

/**
 * Walking queue for entities
 * 
 * @author goku
 * @author Graham Edgecombe
 */
public class WalkingQueue {

	/**
	 * The maximum size of the queue. If there are more points than this size,
	 * they are discarded.
	 */
	public static final int MAXIMUM_SIZE = 50;

	/**
	 * dunno
	 */
	public static final byte[] DIRECTION_DELTA_X = new byte[] { -1, 0, 1, -1,
			1, -1, 0, 1 };

	/**
	 * duno
	 */
	public static final byte[] DIRECTION_DELTA_Y = new byte[] { 1, 1, 1, 0, 0,
			-1, -1, -1 };

	/**
	 * The entity this walking queue belongs to
	 */
	private final Character character;

	/**
	 * The queue of waypoints.
	 */
	private Deque<Point> waypoints = new LinkedList<Point>();

	/**
	 * Run toggle (button in client).
	 */
	private boolean runToggled = false;

	/**
	 * Run for this queue (CTRL-CLICK) toggle.
	 */
	private boolean runQueue = false;

	/**
	 * 
	 * @param entity
	 */
	public WalkingQueue(Character entity) {
		this.character = entity;
	}

	/**
	 * Adds a single step to the walking queue, filling in the points to the
	 * previous point in the queue if necessary.
	 * 
	 * @param x
	 *            The local x coordinate.
	 * @param y
	 *            The local y coordinate.
	 */
	public void addStep(int x, int y) {
		/*
		 * The RuneScape client will not send all the points in the queue. It
		 * just sends places where the direction changes.
		 * 
		 * For instance, walking from a route like this:
		 * 
		 * <code> ***** * * ***** </code>
		 * 
		 * Only the places marked with X will be sent:
		 * 
		 * <code> X***X * * X***X </code>
		 * 
		 * This code will 'fill in' these points and then add them to the queue.
		 */

		/*
		 * We need to know the previous point to fill in the path.
		 */
		if (waypoints.size() == 0) {
			/*
			 * There is no last point, reset the queue to add the player's
			 * current location.
			 */
			reset();
		}

		/*
		 * We retrieve the previous point here.
		 */
		Point last = waypoints.peekLast();

		/*
		 * We now work out the difference between the points.
		 */
		int diffX = x - last.x;
		int diffY = y - last.y;

		/*
		 * And calculate the number of steps there is between the points.
		 */
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int i = 0; i < max; i++) {
			/*
			 * Keep lowering the differences until they reach 0 - when our route
			 * will be complete.
			 */
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++;
			} else if (diffY > 0) {
				diffY--;
			}

			/*
			 * Add this next step to the queue.
			 */
			addStepInternal(x - diffX, y - diffY);
		}
	}

	/**
	 * Adds a single step to the queue internally without counting gaps. This
	 * method is unsafe if used incorrectly so it is private to protect the
	 * queue.
	 * 
	 * @param x
	 *            The x coordinate of the step.
	 * @param y
	 *            The y coordinate of the step.
	 */
	private void addStepInternal(int x, int y) {
		/*
		 * Check if we are going to violate capacity restrictions.
		 */
		if (waypoints.size() >= MAXIMUM_SIZE) {
			/*
			 * If we are we'll just skip the point. The player won't get a
			 * complete route by large routes are not probable and are more
			 * likely sent by bots to crash servers.
			 */
			return;
		}

		/*
		 * We retrieve the previous point (this is to calculate the direction to
		 * move in).
		 */
		Point last = waypoints.peekLast();

		/*
		 * Now we work out the difference between these steps.
		 */
		int diffX = x - last.x;
		int diffY = y - last.y;

		/*
		 * And calculate the direction between them.
		 */
		int dir = DirectionUtils.direction(diffX, diffY);

		/*
		 * Check if we actually move anywhere.
		 */
		if (dir > -1) {
			/*
			 * We now have the information to add a point to the queue! We
			 * create the actual point object and add it.
			 */
			waypoints.add(new Point(x, y, dir));

		}
	}

	/**
	 * Processes the next player's movement.
	 */
	public void processNextMovement() {
		/*
		 * The points which we are walking to.
		 */
		Point walkPoint = null, runPoint = null;
		
		/*
		 * Check to see if the player is teleporting or not
		 */
		if(character.getTeleportTarget() != null) {
			/*
			 * After a teleport, the walking queue resets
			 */
			reset();
			
			/*
			 * Flag the player as teleporting
			 */
			character.setTeleporting(true);
			
			/*
			 * change the player's location to that of the teleport target
			 */
			character.getLocation().transform(character.getTeleportTarget());
			
			/*
			 * Reset the teleport target
			 */
			character.setTeleportTarget(null);
		} else {
			/*
			 * If the player isn't teleporting, they are walking (or standing
			 * still). We get the next direction of movement here.
			 */
			walkPoint = getNextPoint();

			/*
			 * Technically we should check for running here.
			 */
			if (runToggled || runQueue) {
				runPoint = getNextPoint();
			}

			/*
			 * Now set the sprites.
			 */
			int walkDir = walkPoint == null ? -1 : walkPoint.dir;
			int runDir = runPoint == null ? -1 : runPoint.dir;
			
			/*
			 * 
			 */
			character.getMovementFlags().setPrimaryDirection(walkDir == -1 ? null : Direction.values()[walkDir]);
			character.getMovementFlags().setSecondaryDirection(runDir == -1 ? null : Direction.values()[runDir]);
		}

		/*
		 * Calculate the distance between the two tiles
		 */
		int diff_x = character.getLocation().getX() - (character.getLastUpdatedRegion().getX()) * 8;
		int diff_y = character.getLocation().getY() - (character.getLastUpdatedRegion().getY()) * 8;
		
		/*
		 * Set the map region changed flag
		 */
		boolean regionUpdateRequired = diff_x < -32 || diff_x >= 40 || diff_y < -32 || diff_y >= 40;
		
		/*
		 * Indicate a region update is required
		 */
		if (regionUpdateRequired) {
			character.setMapRegionChanged(true);
			character.setLastUpdatedRegion(character.getLocation().toRegion());
		}
	}

	/**
	 * Gets the next point of movement.
	 * 
	 * @return The next point.
	 */
	private Point getNextPoint() {
		/*
		 * Take the next point from the queue.
		 */
		Point p = waypoints.poll();

		/*
		 * Checks if there are no more points.
		 */
		if (p == null || p.dir == -1) {
			/*
			 * Return <code>null</code> indicating no movement happened.
			 */
			return null;
		} else {
			/*
			 * Set the player's new location.
			 */
			int diffX = DIRECTION_DELTA_X[p.dir];
			int diffY = DIRECTION_DELTA_Y[p.dir];
			
			/*
			 * Transform the player's location
			 */
			character.getLocation().transform(diffX, diffY, 0);

			/*
			 * And return the direction.
			 */
			return p;
		}
	}

	/**
	 * Resets the walking queue so it contains no more steps.
	 */
	public void reset() {
		runQueue = false;
		waypoints.clear();
		waypoints.add(new Point(character.getLocation().getX(), character.getLocation().getY(), -1));
	}

	/**
	 * Checks if the queue is empty.
	 * 
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isEmpty() {
		return waypoints.isEmpty();
	}

	/**
	 * Removes the first waypoint which is only used for calculating directions.
	 * This means walking begins at the correct time.
	 */
	public void finish() {
		waypoints.removeFirst();
	}

	/**
	 * Sets the run toggled flag.
	 * 
	 * @param runToggled
	 *            The run toggled flag.
	 */
	public void setRunningToggled(boolean runToggled) {
		this.runToggled = runToggled;
	}

	/**
	 * Sets the run queue flag.
	 * 
	 * @param runQueue
	 *            The run queue flag.
	 */
	public void setRunningQueue(boolean runQueue) {
		this.runQueue = runQueue;
	}

	/**
	 * Gets the run toggled flag.
	 * 
	 * @return The run toggled flag.
	 */
	public boolean isRunningToggled() {
		return runToggled;
	}

	/**
	 * Gets the running queue flag.
	 * 
	 * @return The running queue flag.
	 */
	public boolean isRunningQueue() {
		return runQueue;
	}

	/**
	 * Checks if any running flag is set.
	 * 
	 * @return <code>true</code. if so, <code>false</code> if not.
	 */
	public boolean isRunning() {
		return runToggled || runQueue;
	}

	/**
	 * Represents a single point in the queue.
	 * 
	 * @author Graham Edgecombe
	 * 
	 */
	private static class Point {

		/**
		 * The x-coordinate.
		 */
		private final int x;

		/**
		 * The y-coordinate.
		 */
		private final int y;

		/**
		 * The direction to walk to this point.
		 */
		private final int dir;

		/**
		 * Creates a point.
		 * 
		 * @param x
		 *            X coord.
		 * @param y
		 *            Y coord.
		 * @param dir
		 *            Direction to walk to this point.
		 */
		public Point(int x, int y, int dir) {
			this.x = x;
			this.y = y;
			this.dir = dir;
		}

	}

}