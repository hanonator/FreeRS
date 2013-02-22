package org.hannes.rs2.content.misc;

import org.hannes.rs2.content.event.MakeEvent;
import org.hannes.rs2.event.EventHandler;
import org.hannes.rs2.util.ValuePrompt;
import org.hannes.rs2.util.Windex;

public class MakeEventHandler implements EventHandler<MakeEvent> {

	@Override
	public void handleEvent(MakeEvent event) throws Exception {
		MakeAction action = event.getPlayer().getMakeAction();
		action.setSlot(event.getSlot());
		action.setAmount(event.getAmount());
		if (action.getAmount() == -1) {
			event.getPlayer().getConnection().write(new ValuePrompt());
		} else {
			event.getPlayer().getConnection().write(new Windex());
			event.getPlayer().getActionQueue().offer(action);
		}
	}

}