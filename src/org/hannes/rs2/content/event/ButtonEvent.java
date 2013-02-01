package org.hannes.rs2.content.event;

import org.hannes.rs2.entity.Player;

/**
 * Before every day comes the night
 * 
 * @author red
 */
public class ButtonEvent {

	/**
	 * The player who clicked the button
	 */
	private final Player player;
	
	/**
	 * Id of the button
	 */
	private final int button;

	public ButtonEvent(Player player, int button) {
		this.player = player;
		this.button = button;
	}

	public Player getPlayer() {
		return player;
	}

	public int getButton() {
		return button;
	}

}