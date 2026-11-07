package net.jahus.villagerleash.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.jahus.villagerleash.VillagerLeash;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public final class ModConfig {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private static Path path;
	private static Data data = new Data();

	public static final class Data {
		/** Master switch. If false, villagers stay unleashable (vanilla). */
		public boolean enable = true;
		/** Damage applied every second while leashed. 0 = off. */
		public float damage_per_second = 0.25f;
		/** Chance each second that the leash breaks. 0 = off. */
		public float break_chance_per_second = 0.02f;
		/** When true, the villager treats the holder as an attacker on attach. */
		public boolean anger_on_leash = true;
		/**
		 * When true, re-apply anger every {@link #anger_renew_interval_seconds}
		 * while still leashed (keeps golems / memory interested).
		 */
		public boolean renew_anger = true;
		/** Seconds between anger renewals when {@link #renew_anger} is true. */
		public int anger_renew_interval_seconds = 10;
	}

	private ModConfig() {}

	public static void load(Path configDir) {
		path = configDir.resolve("villagerleash.json");
		if (Files.isRegularFile(path)) {
			try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
				Data loaded = GSON.fromJson(reader, Data.class);
				if (loaded != null) {
					data = loaded;
				}
			} catch (Exception e) {
				VillagerLeash.LOGGER.error("Failed to read config, using defaults", e);
				data = new Data();
			}
		}
		normalize();
		save();
		VillagerLeash.LOGGER.info(
				"Config enable={} damage/s={} break%/s={} anger={} renew_anger={} interval={}s",
				data.enable,
				data.damage_per_second,
				data.break_chance_per_second,
				data.anger_on_leash,
				data.renew_anger,
				data.anger_renew_interval_seconds
		);
	}

	private static void normalize() {
		if (data.damage_per_second < 0f) {
			data.damage_per_second = 0f;
		}
		if (data.break_chance_per_second < 0f) {
			data.break_chance_per_second = 0f;
		}
		if (data.break_chance_per_second > 1f) {
			data.break_chance_per_second = 1f;
		}
		if (data.anger_renew_interval_seconds < 1) {
			data.anger_renew_interval_seconds = 1;
		}
	}

	public static void save() {
		if (path == null) return;
		try {
			Files.createDirectories(path.getParent());
			try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
				GSON.toJson(data, writer);
			}
		} catch (IOException e) {
			VillagerLeash.LOGGER.error("Failed to write config", e);
		}
	}

	public static boolean enable() {
		return data.enable;
	}

	public static float damagePerSecond() {
		return data.damage_per_second;
	}

	public static float breakChancePerSecond() {
		return data.break_chance_per_second;
	}

	public static boolean angerOnLeash() {
		return data.anger_on_leash;
	}

	public static boolean renewAnger() {
		return data.renew_anger;
	}

	public static int angerRenewIntervalSeconds() {
		return data.anger_renew_interval_seconds;
	}
}
