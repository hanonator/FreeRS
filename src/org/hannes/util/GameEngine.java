package org.hannes.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The GameEngine is the class that handles most of the concurrent
 * task execution thingies.
 * 
 * @author red
 *
 */
public class GameEngine implements Runnable {

	/**
	 * The pool to schedule individual tasks
	 * 
	 * TODO: Enable parallel task execution
	 */
	private final ExecutorService service;
	
	/**
	 * The service that the GameEngine's thread will be submitted to and executed periodically
	 */
	private final ScheduledExecutorService scheduledService;
	
	/**
	 * The tasks that will be added to the active tasks the next available cycle
	 */
	private final Queue<Task> futureTasks = new LinkedList<Task>();
	
	/**
	 * The tasks that will be added to the active tasks the next available cycle
	 */
	private final Queue<Task> activeTasks = new LinkedList<Task>();

	/**
	 * The amount of server ticks past since the GameEngine has started
	 */
	private long clock;

	/**
	 * Creates a new GameEngine with a given ExecutorService
	 * 
	 * @param cycle_time
	 * @param service
	 * @param scheduledService
	 */
	public GameEngine(int cycle_time, ExecutorService service, ScheduledExecutorService scheduledService) {
		this.service = service;
		this.scheduledService = scheduledService;

		this.scheduledService.scheduleAtFixedRate(this, cycle_time, cycle_time, TimeUnit.MILLISECONDS);
	}

	/**
	 * Creates a new GameEngine with a fixed threadpool with size = n-processors - 1
	 * and a default single-threaded scheduled ExecutorService
	 * 
	 * @param cycle_time
	 */
	public GameEngine(int cycle_time) {
		this (cycle_time, Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()),
				Executors.newSingleThreadScheduledExecutor());
	}

	@Override
	public void run() {
		/*
		 * Increase the clock count
		 */
		clock++;
		
		/*
		 * Add the new tasks
		 */
		synchronized (futureTasks) {
			for (Iterator<Task> iterator = futureTasks.iterator(); iterator.hasNext(); ) {
				activeTasks.add(iterator.next());
				iterator.remove();
			}
		}
		
		/*
		 * Execute the active tasks
		 */
		for (Task task = activeTasks.poll(); task != null; task = activeTasks.poll()) {
			try {
				if (task != null && !task.execute(this)) {
					submit(task);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Submit a task to the scheduler
	 * 
	 * @param task
	 */
	public void submit(Task task) {
		synchronized (futureTasks) {
			futureTasks.add(task);
		}
	}

	/**
	 * Submit a java.lang.runnable to run once with normal priority
	 * 
	 * @param runnable
	 */
	public void submit(Runnable runnable) {
		this.submit(new RunnableTask(runnable));
	}

	/**
	 * Submit a collection of stuffz
	 * 
	 * @param <T>
	 * @param tasks
	 * @return
	 * @throws InterruptedException
	 */
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return service.invokeAll(tasks);
	}

	/**
	 * shuts everything down
	 */
	public void shutdown() {
		// service.shutdownNow();
		// scheduledService.shutdownNow();
	}

	public ExecutorService getService() {
		return service;
	}

	public long getClock() {
		return clock;
	}

	/**
	 * Enables support for java.lang.Runnable
	 * 
	 * @author red
	 */
	private static class RunnableTask implements Task {

		/**
		 * The runnable object that has to be executed
		 */
		private final Runnable runnable;

		/**
		 * 
		 * @param runnable
		 * @param priority
		 */
		public RunnableTask(Runnable runnable) {
			this.runnable = runnable;
		}

		@Override
		public boolean execute(GameEngine engine) throws Exception {
			/*
			 * Execute the runnable
			 */
			runnable.run();
			
			/*
			 * The runnable has to be run once
			 */
			return true;
		}
		
	}

}