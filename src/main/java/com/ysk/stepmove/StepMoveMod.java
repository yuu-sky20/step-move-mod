package com.ysk.stepmove;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StepMoveMod implements ModInitializer {
	public static final String MOD_ID = "stepmove";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Step Move Mod");
	}
}