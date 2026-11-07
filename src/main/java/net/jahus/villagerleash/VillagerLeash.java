package net.jahus.villagerleash;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.jahus.villagerleash.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class VillagerLeash implements ModInitializer {

	public static final String MOD_ID = "villagerleash";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModConfig.load(FabricLoader.getInstance().getConfigDir());
		LOGGER.info("Villager Leash loaded");
	}
}
