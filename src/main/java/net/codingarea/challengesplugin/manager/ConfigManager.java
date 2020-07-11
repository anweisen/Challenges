package net.codingarea.challengesplugin.manager;

import net.codingarea.challengesplugin.utils.YamlConfig;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-01-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class ConfigManager {

	private YamlConfig internalConfig;
	private YamlConfig positionConfig;

	public YamlConfig getInternalConfig() {
		return internalConfig;
	}

	public void setInternalConfig(YamlConfig internalConfig) {
		this.internalConfig = internalConfig;
	}

	public YamlConfig getPositionConfig() {
		return positionConfig;
	}

	public void setPositionConfig(YamlConfig positionConfig) {
		this.positionConfig = positionConfig;
	}

	public void reset() {
		positionConfig.clear(true);
		internalConfig.clear(true, "level-name", "reset");
	}

}
