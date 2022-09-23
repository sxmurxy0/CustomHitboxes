package dev.sxmurxy.customhitboxes.utils;

import dev.sxmurxy.customhitboxes.Wrapper;
import dev.sxmurxy.customhitboxes.ui.UserInterface;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler implements Wrapper {

	@SubscribeEvent
	public void onPress(InputEvent.KeyInputEvent e) {
		if ((MC.screen == null || MC.screen instanceof UserInterface) && 
				e.getAction() == 1 && e.getKey() == MOD.settings.toggleInterfaceKey.getKey().getValue()) {
			MOD.userInterface.toggle();
		}
	}
	
}
