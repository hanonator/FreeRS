package org.hannes.rs2.net.codec.decoder;

import org.hannes.rs2.net.Connection;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.codec.Decoder;

/**
 * Does nothing
 * 
 * @author red
 */
public class SilentDecoder implements Decoder {

	@Override
	public void decode(Message message, Connection connection) throws Exception {
		
	}

}