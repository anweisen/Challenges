package net.codingarea.challenges.plugin.management.challenges;

import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.commons.config.document.wrapper.FileDocumentWrapper;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.Goal;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;
import net.codingarea.challenges.plugin.utils.logging.Logger;

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

	public void clearChallengeCache() {
		challenges.clear();
		currentGoal = null;
	}

	public void enable() {
		loadGamestate(Challenges.getInstance().getConfigManager().getGamestateConfig());
		loadSettings(Challenges.getInstance().getConfigManager().getSettingsConfig());
	}

	public synchronized void loadSettings(@Nonnull Document config) {
		for (IChallenge challenge : challenges) {
			String name = challenge.getName();
			if (config.contains(name)) {
				try {
					Document document = config.getDocument(name);
					challenge.loadSettings(document);
				} catch (Exception ex) {
					Logger.severe("Could not load setting for challenge " + challenge.getClass().getSimpleName(), ex);
				}
			}
		}
	}

	public synchronized void loadGamestate(@Nonnull Document config) {
		for (IChallenge challenge : challenges) {
			String name = challenge.getName();
			if (config.contains(name)) {
				try {
					Document document = config.getDocument(name);
					challenge.loadGameState(document);
				} catch (Exception ex) {
					Logger.severe("Could not load gamestate for " + challenge.getClass().getSimpleName(), ex);
				}
			}
		}
	}

	public void saveGameStateInto(@Nonnull Document config) {
		for (IChallenge challenge : challenges) {
			try {
				Document document = config.getDocument(challenge.getName());
				challenge.writeGameState(document);
			} catch (Exception ex) {
				Logger.severe("Could not write gamestate of " + challenge.getClass().getSimpleName(), ex);
			}
		}
	}

	public synchronized void saveGamestate(boolean async) {
		FileDocumentWrapper config = Challenges.getInstance().getConfigManager().getGamestateConfig();
		saveGameStateInto(config);
		config.save(async);
	}

	public void saveSettingsInto(@Nonnull Document config) {
		for (IChallenge challenge : challenges) {
			try {
				Document document = config.getDocument(challenge.getName());
				challenge.writeSettings(document);
			} catch (Exception ex) {
				Logger.severe("Could not write settings of " + challenge.getClass().getSimpleName(), ex);
			}
		}
	}

	public synchronized void saveLocalSettings(boolean async) {
		FileDocumentWrapper config = Challenges.getInstance().getConfigManager().getSettingsConfig();
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
