package net.codingarea.challenges.plugin.management.files;

import net.anweisen.utilities.bukkit.utils.logging.Logger;
import net.anweisen.utilities.common.config.FileDocument;
import net.anweisen.utilities.common.config.document.GsonDocument;
import net.anweisen.utilities.common.misc.FileUtils;
import net.codingarea.challenges.plugin.Challenges;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public final class ConfigManager {

	private FileDocument sessionConfig, gamestateConfig, settingsConfig, customChallengesConfig;

	public ConfigManager() {
		GsonDocument.setCleanupEmptyObjects(true);
		GsonDocument.setCleanupEmptyObjects(true);
	}

	public void loadConfigs() {
		sessionConfig   = load("internal/session.json");
		gamestateConfig = load("internal/gamestate.json");
		settingsConfig  = load("internal/settings.json");
		customChallengesConfig  = load("internal/custom-challenges.json");
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

	public FileDocument getCustomChallengesConfig() {
		return customChallengesConfig;
	}

}
