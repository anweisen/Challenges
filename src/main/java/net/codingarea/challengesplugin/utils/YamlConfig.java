package net.codingarea.challengesplugin.utils;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class YamlConfig {

	private FileConfiguration fileConfiguration;

	@Getter private File configFile;

	public YamlConfig(String name) {

		if (!name.endsWith(".yml") && !name.endsWith(".yaml")) {
			name += ".yml";
		}

		configFile = new File("plugins/Challenges/" + name);

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

	public FileConfiguration getConfig() {
		return fileConfiguration;
	}
}
