package net.codingarea.challenges.plugin.management.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.anweisen.utilities.bukkit.utils.logging.Logger;
import net.anweisen.utilities.common.config.Document;
import net.anweisen.utilities.common.config.FileDocument;
import net.anweisen.utilities.common.config.document.GsonDocument;
import net.anweisen.utilities.common.misc.FileUtils;
import net.codingarea.challenges.plugin.Challenges;
import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public final class ConfigManager {

	private final List<String> missingConfigSettings;
	private FileDocument sessionConfig, gamestateConfig, settingsConfig, customChallengesConfig;

	public ConfigManager() {
		missingConfigSettings = new LinkedList<>();
		GsonDocument.setCleanupEmptyObjects(true);
		GsonDocument.setCleanupEmptyObjects(true);
	}

	public void loadConfigs() {
		sessionConfig   = load("internal/session.json");
		gamestateConfig = load("internal/gamestate.json");
		settingsConfig  = load("internal/settings.json");
		customChallengesConfig  = load("internal/custom_challenges.json");

		Challenges plugin = Challenges.getInstance();
		Document config = plugin.getConfigDocument();

		try {
			// Create Temp File for loading the yaml configuration
			File defaultConfigTempFile = new File("defaultConfig.yml");
			// Create a output stream from that file to write into it
			FileOutputStream stream = new FileOutputStream(defaultConfigTempFile);
			// Copy the default config inside the temp file
			IOUtils.copy(plugin.getResource("config.yml"), stream);
			// Load the File as a yaml config
			// Spigot Config Implementation because the Document Library does not contain deep-keys.
			YamlConfiguration defaultConfig = new YamlConfiguration();
			defaultConfig.load(defaultConfigTempFile);

			for (String key : defaultConfig.getKeys(true)) {
				if (!config.contains(key)) {
					missingConfigSettings.add(key);
				}
			}
		} catch (IOException | NullPointerException | InvalidConfigurationException e) {
			plugin.getLogger().severe("Error while checking missing keys in the current config");
			e.printStackTrace();
		}

	}

	@Nullable
	private FileDocument load(@Nonnull String filename) {
		try {
			File file = Challenges.getInstance().getDataFile(filename);
			FileUtils.createFilesIfNecessary(file);
			return FileDocument.readJsonFile(file);
		} catch (Exception ex) {
			Logger.error("Could not load config '{}': {}", filename, ex);
			return null;
		}
	}

	@Nonnull
	public FileDocument getSessionConfig() {
		return sessionConfig;
	}

	@Nonnull
	public FileDocument getGameStateConfig() {
		return gamestateConfig;
	}

	@Nonnull
	public FileDocument getSettingsConfig() {
		return settingsConfig;
	}

	@Nonnull
	public List<String> getMissingConfigSettings() {
		return new LinkedList<>(missingConfigSettings);
	}

	public FileDocument getCustomChallengesConfig() {
		return customChallengesConfig;
	}

}
