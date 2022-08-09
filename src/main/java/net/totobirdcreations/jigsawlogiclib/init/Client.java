package net.totobirdcreations.jigsawlogiclib.init;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.totobirdcreations.jigsawlogiclib.logic.LogicScreen;


@Environment(EnvType.CLIENT)
public class Client implements ClientModInitializer {

	static {
		HandledScreens.register(
				Main.SCREEN_HANDLER,
				LogicScreen::new
		);
	}

	@Override
	public void onInitializeClient() {
	}

}
