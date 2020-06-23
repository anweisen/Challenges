package net.codingarea.challengesplugin.manager;

import net.codingarea.challengesplugin.utils.YamlConfig;
import lombok.Getter;
import lombok.Setter;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-01-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class ConfigManager {

	@Getter @Setter private YamlConfig backpackConfig;
	@Getter @Setter private YamlConfig positionConfig;
	@Getter @Setter private YamlConfig internalConfig;

}
