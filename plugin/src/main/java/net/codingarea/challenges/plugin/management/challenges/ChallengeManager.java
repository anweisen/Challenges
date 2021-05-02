package net.codingarea.challenges.plugin.management.challenges;

import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.commons.config.FileDocument;
import net.anweisen.utilities.commons.config.document.GsonDocument;
import net.anweisen.utilities.database.exceptions.DatabaseException;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.Goal;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ChallengeManager {

	private final List<IChallenge> challenges = new LinkedList<>();

	private Goal currentGoal;

	@Nonnull
	public List<IChallenge> getChallenges() {
		return Collections.unmodifiableList(challenges);
	}

	public void register(@Nonnull IChallenge challenge) {
		if (!challenge.getType().isUsable()) throw new IllegalArgumentException("Invalid MenuType");
		challenges.add(challenge);
	}

	public void shutdownChallenges() {
		for (IChallenge challenge : challenges) {
			try {
				challenge.handleShutdown();
			} catch (Exception ex) {
				Logger.error("Could not handle shutdown for {}", challenge.getClass().getSimpleName(), ex);
			}
		}
	}

	public void clearChallengeCache() {
		challenges.clear();
		currentGoal = null;
	}

	public void restoreDefaults() {
		Logger.debug("Restoring default settings..");
		for (IChallenge challenge : challenges) {
			try {
				challenge.restoreDefaults();
			} catch (Exception ex) {
				Logger.error("Could not restore defaults for {}", challenge.getClass().getSimpleName(), ex);
			}
		}
	}

	public void saveSettings(@Nonnull Player player) throws DatabaseException {
		Document document = new GsonDocument();
		saveSettingsInto(document);
		Challenges.getInstance().getDatabaseManager().getDatabase()
				.insertOrUpdate("challenges")
				.where("uuid", player.getUniqueId())
				.set("config", document)
				.execute();
	}

	public void enable() {
		loadGamestate(Challenges.getInstance().getConfigManager().getGameStateConfig().readonly());
		loadSettings(Challenges.getInstance().getConfigManager().getSettingsConfig().readonly());
	}

	public synchronized void loadSettings(@Nonnull Document config) {
		for (IChallenge challenge : challenges) {
			String name = challenge.getName();
			if (!config.contains(name)) continue;
			try {
				Document document = config.getDocument(name);
				challenge.loadSettings(document);
			} catch (Exception ex) {
				Logger.error("Could not load setting for challenge {}", challenge.getClass().getSimpleName(), ex);
			}
		}
	}

	public synchronized void loadGamestate(@Nonnull Document config) {
		for (IChallenge challenge : challenges) {
			String name = challenge.getName();
			if (!config.contains(name)) continue;
			try {
				Document document = config.getDocument(name);
				challenge.loadGameState(document);
			} catch (Exception ex) {
				Logger.error("Could not load gamestate for {}", challenge.getClass().getSimpleName(), ex);
			}
		}
	}

	public void saveGameStateInto(@Nonnull Document config) {
		for (IChallenge challenge : challenges) {
			try {
				Document document = config.getDocument(challenge.getName());
				challenge.writeGameState(document);
			} catch (Exception ex) {
				Logger.error("Could not write gamestate of {}", challenge.getClass().getSimpleName(), ex);
			}
		}
	}

	public synchronized void saveGamestate(boolean async) {
		FileDocument config = Challenges.getInstance().getConfigManager().getGameStateConfig();
		saveGameStateInto(config);
		config.save(async);
	}

	public void saveSettingsInto(@Nonnull Document config) {
		for (IChallenge challenge : challenges) {
			try {
				Document document = config.getDocument(challenge.getName());
				challenge.writeSettings(document);
			} catch (Exception ex) {
				Logger.error("Could not write settings of {}", challenge.getClass().getSimpleName(), ex);
			}
		}
	}

	public synchronized void saveLocalSettings(boolean async) {
		FileDocument config = Challenges.getInstance().getConfigManager().getSettingsConfig();
		saveSettingsInto(config);
		config.save(async);
	}

	@Nullable
	public Goal getCurrentGoal() {
		return currentGoal;
	}

	public void setCurrentGoal(@Nullable Goal goal) {

		Goal oldGoal = currentGoal;
		currentGoal = goal;

		if (oldGoal != null)
			oldGoal.setEnabled(false);

		if (goal != null)
			goal.setEnabled(true);

	}

}
