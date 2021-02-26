package net.codingarea.challenges.plugin.management.challenges;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.Goal;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;
import org.bukkit.event.Listener;

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
		Challenges.getInstance().getScheduler().register(challenge);
		challenges.add(challenge);
	}

	public void clear() {
		challenges.clear();
		currentGoal = null;
	}

	public void enable() {
		for (IChallenge challenge : challenges) {
			if (challenge instanceof Listener)
				Challenges.getInstance().registerListener((Listener) challenge);
		}
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
