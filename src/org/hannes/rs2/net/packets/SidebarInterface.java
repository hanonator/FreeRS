package org.hannes.rs2.net.packets;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.MessageBuilder;
import org.hannes.rs2.net.Serializable;

/**
 * A sidebar interface is an interface that goes on the left 
 * of the player's screen e.g. the player's inventory
 * 
 * @author red
 */
public class SidebarInterface implements Serializable {

	/**
	 * The tab index
	 */
	private final int index;
	
	/**
	 * The interface index
	 */
	private final int interfaceId;

	public SidebarInterface(int index, int interfaceId) {
		this.index = index;
		this.interfaceId = interfaceId;
	}
	@Override
	public Message serialize(Connection connection) {
		return new MessageBuilder(71).putShort((short) interfaceId)
				.put((byte) index).build();
	}

	public int getIndex() {
		return index;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * The weapon tab index
	 */
	public static final int WEAPON_INDEX = 0;
	
	/**
	 * The quest tab index
	 */
	public static final int QUES_INDEX = 1;
	
	/**
	 * The skill tab index
	 */
	public static final int SKILL_INDEX = 2;
	
	/**
	 * The inventory tab index
	 */
	public static final int INVENTORY_INDEX = 3;
	
	/**
	 * The equipment tab index
	 */
	public static final int EQUIPMENT_INDEX = 4;
	
	/**
	 * The prayer tab index
	 */
	public static final int PRAYER_INDEX = 5;
	
	/**
	 * The magic tab index
	 */
	public static final int MAGIC_INDEX = 6;
	
	/**
	 * The null tab index
	 */
	public static final int NULL_INDEX = 7;
	
	/**
	 * The friends tab index
	 */
	public static final int FRIENDS_INDEX = 8;
	
	/**
	 * The ignore tab index
	 */
	public static final int IGNORE_INDEX = 9;
	
	/**
	 * The logout tab index
	 */
	public static final int LOGOUT_INDEX = 10;
	
	/**
	 * The settings tab index
	 */
	public static final int SETTINGS_INDEX = 11;
	
	/**
	 * The emote tab index
	 */
	public static final int EMOTE_INDEX = 12;
	
	/**
	 * The music tab index
	 */
	public static final int MUSIC_INDEX = 13;
	
	/**
	 * The unarmed weapon sidebar interface
	 */
	public static final SidebarInterface WEAPON_UNARMED = new SidebarInterface(0, 5855);
	
	/**
	 * The quest sidebar interface
	 */
	public static final SidebarInterface QUEST_JOURNAL = new SidebarInterface(1, 638);
	
	/**
	 * The skill sidebar interface
	 */
	public static final SidebarInterface SKILL = new SidebarInterface(2, 3917);
	
	/**
	 * The inventory sidebar interface
	 */
	public static final SidebarInterface INVENTORY = new SidebarInterface(3, 3213);
	
	/**
	 * The equipment sidebar interface
	 */
	public static final SidebarInterface EQUIPMENT = new SidebarInterface(4, 1644);
	
	/**
	 * The prayer sidebar interface
	 */
	public static final SidebarInterface PRAYER = new SidebarInterface(5, 5608);
	
	/**
	 * The regular magic sidebar interface
	 */
	public static final SidebarInterface REGULAR_MAGIC = new SidebarInterface(6, 1151);
	
	/**
	 * The ancient magic sidebar interface
	 */
	public static final SidebarInterface ANCIENT_MAGIC = new SidebarInterface(6, 12855);
	
	/**
	 * The friends list sidebar interface
	 */
	public static final SidebarInterface FRIENDS_LIST = new SidebarInterface(8, 5065);
	
	/**
	 * The ignore sidebar interface
	 */
	public static final SidebarInterface IGNORE_LIST = new SidebarInterface(9, 5715);
	
	/**
	 * The logout sidebar interface
	 */
	public static final SidebarInterface LOG_OUT = new SidebarInterface(10, 2449);
	
	/**
	 * The settings sidebar interface
	 */
	public static final SidebarInterface SETTINGS = new SidebarInterface(11, 904);
	
	/**
	 * The emote sidebar interface
	 */
	public static final SidebarInterface EMOTE = new SidebarInterface(12, -1);
	
	/**
	 * The sidebar for music that shows all the songs
	 */
	public static final SidebarInterface HIGH_MEMORY_MUSIC = new SidebarInterface(13, 962);
	
	/**
	 * The sidebar for music that tells the user that sounds are disabled
	 */
	public static final SidebarInterface LOW_MEMORY_MUSIC = new SidebarInterface(13, 6299);

}