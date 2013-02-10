package org.hannes;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.dom4j.io.SAXReader;
import org.hannes.rs2.World;
import org.hannes.rs2.event.EventHub;
import org.hannes.rs2.net.RS2PipelineFactory;
import org.hannes.rs2.tasks.ConnectionFlushTask;
import org.hannes.rs2.tasks.EntitySynchronizationTask;
import org.hannes.rs2.util.ItemDefinition;
import org.hannes.rs2.util.WeaponInterface;
import org.hannes.util.GameEngine;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class Main {

	/**
	 * The logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Main.class.getName());
	
	/**
	 * The game engine
	 */
	private static final GameEngine engine = new GameEngine(600);
	
	/**
	 * The event hub
	 */
	private static final EventHub eventHub = new EventHub();

	public static void main(String[] args) throws Exception {
		logger.info("Starting RS2E");
		
		/*
		 * Register some cool tasks
		 */
		engine.submit(new EntitySynchronizationTask());
		engine.submit(new ConnectionFlushTask());
		
		/*
		 * Create a new SAX reader
		 */
		SAXReader reader = new SAXReader();
		
		/*
		 * Initialize the event hub
		 */
		eventHub.initialize(reader.read("event-handlers.xml"));
		
		/*
		 * Initialize the content
		 * TODO: No hardcode pls
		 */
		ItemDefinition.init();
		World.getWorld().initialize(reader.read("data/spawn-areas.xml"));
		WeaponInterface.initialize(reader.read(new File("data/weapon-interfaces.xml")));
		
		/*
		 * Create the server boostrap
		 */
		ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		
		/*
		 * Configure
		 */
		bootstrap.setOption("keepAlive", true);
		bootstrap.setOption("reuseAddress", true);
		bootstrap.setOption("child.tcpNoDelay", true);
		
		/*
		 * Set the pipeline factory
		 */
		bootstrap.setPipelineFactory(new RS2PipelineFactory());
		
		/*
		 * Bind to localhost on port 43594 
		 */
		bootstrap.bind(new InetSocketAddress("127.0.0.1", 43594));
		
		/*
		 * Ready to go
		 */
		logger.info("OK");
	}

	public static GameEngine getEngine() {
		return engine;
	}

	public static EventHub getEventhub() {
		return eventHub;
	}

}