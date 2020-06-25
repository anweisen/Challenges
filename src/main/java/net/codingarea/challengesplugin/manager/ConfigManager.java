package net.codingarea.challengesplugin.manager;

import net.codingarea.challengesplugin.utils.YamlConfig;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-01-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class ConfigManager {

	private YamlConfig backpackConfig;
	private YamlConfig positionConfig;
	private YamlConfig internalConfig;

	public YamlConfig getBackpackConfig() {
		return backpackConfig;
	}

	public YamlConfig getInternalConfig() {
		return internalConfig;
	}

	public YamlConfig getPositionConfig() {
		return positionConfig;
	}

	public void setBackpackConfig(YamlConfig backpackConfig) {
		this.backpackConfig = backpackConfig;
	}

	public void setInternalConfig(YamlConfig internalConfig) {
		this.internalConfig = internalConfig;
	}

	public void setPositionConfig(YamlConfig positionConfig) {
		this.positionConfig = positionConfig;
	}

}
