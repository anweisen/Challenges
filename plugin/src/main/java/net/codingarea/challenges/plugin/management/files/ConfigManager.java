package net.codingarea.challenges.plugin.management.files;

import net.anweisen.utilities.commons.config.FileDocument;
import net.anweisen.utilities.commons.config.document.GsonDocument;
import net.anweisen.utilities.commons.config.document.wrapper.FileDocumentWrapper;
import net.anweisen.utilities.commons.misc.FileUtils;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public final class ConfigManager {

	private FileDocument sessionConfig;
	private FileDocument gamestateConfig;
	private FileDocument settingsConfig;

	public ConfigManager() {
		GsonDocument.setCleanupEmptyObjects(true);
		GsonDocument.setCleanupEmptyObjects(true);
	}

	public void loadConfigs() {
		sessionConfig   = load("internal/session.json");
		gamestateConfig = load("internal/gamestate.json");
		settingsConfig  = load("internal/settings.json");
	}

	@Nullable
	private FileDocument load(@Nonnull String filename) {
		try {
			File file = Challenges.getInstance().getDataFile(filename);
			FileUtils.createFilesIfNecessary(file);
			return FileDocument.read(GsonDocument.class, file);
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

}
