package net.codingarea.challengesplugin.utils;

import net.codingarea.challengesplugin.Challenges;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class YamlConfig {

	private final FileConfiguration fileConfiguration;

	private final File configFile;

	public YamlConfig(String name) {

		if (!name.endsWith(".yml") && !name.endsWith(".yaml")) {
			name += ".yml";
		}

		configFile = new File(Challenges.getInstance().getDataFolder() + "/" + name);

		if (!configFile.exists()) {

			File folder = new File(configFile.getPath().substring(0, configFile.getPath().length() - configFile.getName().length()));

			if (!folder.exists()) {
				folder.mkdirs();
			}

			try {
				configFile.createNewFile();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}

		fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

	}

	public void save() {
		try {
			fileConfiguration.save(configFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public FileConfiguration toFileConfig() {
		return fileConfiguration;
	}

	public void clear() {
		clear(new String[0]);
	}

	public void clear(boolean save) {
		clear();
		if (save) save();
	}

	public void clear(boolean save, String... doNotClear) {
		clear(doNotClear);
		if (save) save();
	}

	public void clear(String... doNotClear) {
		List<String> leave = Arrays.asList(doNotClear);
		for (String currentKey : fileConfiguration.getKeys(false)) {
			if (leave.contains(currentKey)) continue;
			fileConfiguration.set(currentKey, null);
		}
	}

	public File getConfigFile() {
		return configFile;
	}

}
