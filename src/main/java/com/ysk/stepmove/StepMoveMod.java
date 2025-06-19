package com.ysk.stepmove;

import com.ysk.stepmove.event.tracker.PlayerTeleportTracker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ysk.stepmove.event.handler.PlayerTeleportUseItemBookHandler;

public class StepMoveMod implements ModInitializer {
	public static final String MOD_ID = "stepmove";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Step Move Mod");
		PlayerTeleportUseItemBookHandler.register();
		ServerTickEvents.END_WORLD_TICK.register(world -> {
			if (!world.isClient) {
				PlayerTeleportTracker.tick(world);
			}
		});
		LOGGER.info("Step Move Mod initialized successfully");
	}
}