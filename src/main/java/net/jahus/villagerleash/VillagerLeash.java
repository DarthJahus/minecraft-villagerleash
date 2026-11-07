package net.jahus.villagerleash;

import net.jahus.villagerleash.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(VillagerLeash.MOD_ID)
public final class VillagerLeash {

	public static final String MOD_ID = "villagerleash";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public VillagerLeash() {
		ModConfig.load(FMLPaths.CONFIGDIR.get());
		LOGGER.info("Villager Leash (Forge) loaded");
	}
}
