package org.hannes.rs2.content.misc;

import org.hannes.rs2.content.event.UserValueEvent;
import org.hannes.rs2.event.EventHandler;
import org.hannes.rs2.util.Windex;

public class MakeXEventHandler implements EventHandler<UserValueEvent> {

	@Override
	public void handleEvent(UserValueEvent event) throws Exception {
		MakeAction action = event.getPlayer().getMakeAction();
		action.setAmount(event.getValue());
		event.getPlayer().getConnection().write(new Windex());
		event.getPlayer().getActionQueue().offer(action);
	}

}