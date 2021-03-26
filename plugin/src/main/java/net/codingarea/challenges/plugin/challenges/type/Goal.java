package net.codingarea.challenges.plugin.challenges.type;

import net.codingarea.challenges.plugin.challenges.type.helper.GoalHelper;
import net.codingarea.challenges.plugin.management.challenges.ChallengeManager;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface Goal extends IChallenge {

	/**
	 * Enabled / disabled this goal.
	 * This must to call {@link ChallengeManager#setCurrentGoal(Goal)} with
	 * {@code this} if {@code enabled} is {@code true} or {@code null} if {@code enabled} is {@code false} and this goal is the current.
	 * This can be checked by comparing {@link ChallengeManager#getCurrentGoal()} to {@code this}.
	 * You may just call {@link GoalHelper#handleSetEnabled(Goal, boolean)} with {@code this}, {@code enabled}
	 */
	void setEnabled(boolean enabled);

	@Nonnull
	SoundSample getStartSound();

	void getWinnersOnEnd(@Nonnull List<Player> winners);

}
