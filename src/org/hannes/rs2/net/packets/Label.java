package org.hannes.rs2.net.packets;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.MessageBuilder;
import org.hannes.rs2.net.MessageLength;
import org.hannes.rs2.net.Serializable;

/**
 * Interface labels
 * 
 * @author red
 *
 */
public class Label implements Serializable {

	/**
	 * The index
	 */
	private final int index;

	/**
	 * The text
	 */
	private final String text;

	/**
	 * @param index
	 * @param text
	 */
	public Label(int index, String text) {
		this.index = index;
		this.text = text;
	}

	@Override
	public Message serialize(Connection connection) {
		return new MessageBuilder(126, MessageLength.VARIABLE_16_BIT)
				.putString(text).putShort((short) index).build();
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

}