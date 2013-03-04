package org.hannes.rs2.content.event;

import org.hannes.rs2.entity.NPC;
import org.hannes.rs2.entity.Player;

public class NPCInteractionEvent {

	/**
	 * The NPC
	 */
	private NPC npc;
	
	/**
	 * The player
	 */
	private Player player;
	
	/**
	 * The option
	 */
	private int option;

	public NPCInteractionEvent(Player player, int option) {
		this.player = player;
		this.option = option;
	}

	public NPC getNpc() {
		return npc;
	}

	public Player getPlayer() {
		return player;
	}

	public int getOption() {
		return option;
	}

	public void setNpc(NPC npc) {
		this.npc = npc;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setOption(int option) {
		this.option = option;
	}

}