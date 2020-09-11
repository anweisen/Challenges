package net.codingarea.challengesplugin.manager.lang;

import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.commons.Log;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author anweisen
 * Challenges developed on 07-29-2020
 * https://github.com/anweisen
 */

public enum Prefix implements CharSequence {

	CHALLENGES(fromDefault("§6Challenges")),
	TIMER(fromDefault("§5Timer")),
	MASTER(fromDefault("§eMaster")),
	POSITIONS(fromDefault("§9Positions")),
	BACKPACK(fromDefault("§aBackpack")),
	DAMAGE(fromDefault("§cDamage")),
	BINGO(fromDefault("§eBingo")),
	;

	public static final String TEMPLATE = "§8§l┃ %name% §8┃ §7";

	public static String fromDefault(String prefixBody) {
		return TEMPLATE.replace("%name%", prefixBody);
	}

	private final String defaultValue;
	private String currentValue;

	Prefix(String defaultValue) {
		this.defaultValue = defaultValue;
		this.currentValue = defaultValue;
	}

	@Override
	public int length() {
		return currentValue.length();
	}

	@Override
	public char charAt(int index) {
		return currentValue.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return currentValue.subSequence(start, end);
	}

	@Override
	public String toString() {
		return currentValue;
	}

	public String getDefault() {
		return defaultValue;
	}

	public String get() {
		return currentValue;
	}

	public static void load() {
		try {

			File file = new File(LanguageManager.folder() + "/prefix.properties");
			if (!file.exists()) {
				file.createNewFile();
				setDefaults(file);
				return;
			}

			Properties properties = Utils.readProperties(file);
			load(properties);

		} catch (IOException ex) {
			Log.severe("Could not load prefix :: " + ex.getMessage());
		}
	}

	private static void load(Properties properties) {

		for (Prefix currentPrefix : Prefix.values()) {
			try {

				String value = (String) properties.get(currentPrefix.name());
				if (value == null) continue;
				currentPrefix.currentValue = value;

			} catch (Exception ignored) { }
		}

	}

	private static void setDefaults(File file) throws IOException {

		Properties properties = Utils.readProperties(file);

		for (Prefix currentPrefix : Prefix.values()) {
			properties.put(currentPrefix.name(), currentPrefix.getDefault());
		}

		Utils.saveProperties(properties, file);

	}

}
