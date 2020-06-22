package net.codingarea.challengesplugin.utils;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import lombok.Getter;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-7-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class PositionUtil {

	public static class ChallengePosition {

		@Getter private String setter;
		@Getter private Location location;
		@Getter private String name;

		public ChallengePosition(Location location, String name, String setter) {
			this.location = location;
			this.name = name;
			this.setter = setter;
		}

		public String toString() {
			return "§7" + Utils.getWorldName(location.getWorld().getName()) + ": §eX: §7" + location.getBlockX() + " §eY: §7" + location.getBlockY() + " §eZ: §7" + location.getBlockZ();
		}

	}

	public static void savePosition(FileConfiguration config, ChallengePosition position) {

		if (config == null) throw new NullPointerException("Config cannot be null!");
		if (position == null) throw new NullPointerException("Position cannot be null!");

		savePosition(config, position.getSetter(), position.getLocation(), position.getName());

	}

	public static ChallengePosition loadPosition(FileConfiguration config, String name) {

		if (config == null) return null;
		if (name == null) return null;

		Location location = config.getLocation("position." + name.toLowerCase() + ".location");
		String displayName = config.getString("position." + name.toLowerCase() + ".name");
		String setter = config.getString("position." + name.toLowerCase() + ".setter");

		return new ChallengePosition(location, displayName, setter);

	}

	private static void savePosition(FileConfiguration config, String setter, Location location, String locationName) {

		if (config == null) throw new NullPointerException("Config cannot be null!");
		if (location == null) throw new NullPointerException("Location cannot be null!");
		if (locationName == null) throw new NullPointerException("Location name cannot be null!");

		config.set("position." + locationName.toLowerCase() + ".location", location);
		config.set("position." + locationName.toLowerCase() + ".name", locationName);
		config.set("position." + locationName.toLowerCase() + ".setter", setter);

	}

}
