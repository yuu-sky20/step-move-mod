package com.ysk.stepmove;

import com.ysk.stepmove.event.tracker.HoverTracker;
import com.ysk.stepmove.event.tracker.SneakTracker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ysk.stepmove.event.handler.UseBookItemHandler;

public class StepMoveMod implements ModInitializer {
	private static final String MOD_ID = "stepmove";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Step Move Mod");

		UseBookItemHandler.register();

		// サーバーのティックイベントを登録
		ServerTickEvents.END_WORLD_TICK.register(world -> {
			// サーバーがワールドを起動している間のみトラッキング
			if (!world.isClient) {
				// SneakTrackerのティックを実行
				SneakTracker.tick(world);
				// HoverTrackerのティックを実行
				HoverTracker.tick(world);
			}
		});

		LOGGER.info("Step Move Mod initialized successfully");
	}
}