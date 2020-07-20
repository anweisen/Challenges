package net.codingarea.challengesplugin.challengetypes;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.ItemManager;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.challengetypes.extra.ITimerStatusExecutor;
import net.codingarea.challengesplugin.manager.scoreboard.ChallengeScoreboard;
import net.codingarea.challengesplugin.utils.AnimationUtil.AnimationSound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public abstract class Goal extends AbstractChallenge {

	protected AnimationSound sound = AnimationSound.STANDARD_SOUND;
	protected boolean isCurrentGoal;
	protected ChallengeScoreboard scoreboard;

	public abstract void onEnable(ChallengeEditEvent event);
	public abstract void onDisable(ChallengeEditEvent event);

	public abstract List<Player> getWinners();

	@Override
	public void setValues(int value) {
		boolean before = isCurrentGoal;
		isCurrentGoal = value == 1;
		if (isCurrentGoal && !before) onEnable(new ChallengeEditEvent(null, null, null));
		else if (!isCurrentGoal && before) onDisable(new ChallengeEditEvent(null, null, null));
	}

	@Override
	public int toValue() {
		return isCurrentGoal ? 1 : 0;
	}

	@Override
	public void handleClick(ChallengeEditEvent event) { }

	@Override
	public ItemStack getActivationItem() {
		return ItemManager.getActivationItem(isCurrentGoal);
	}

	public boolean isCurrentGoal() {
		return isCurrentGoal;
	}

	public void setIsCurrentGoal(boolean currentGoal, ChallengeEditEvent event) {

		if (!currentGoal && isCurrentGoal) {
			onDisable(event);
		} else if (currentGoal && !isCurrentGoal) {
			onEnable(event);
		}

		this.isCurrentGoal = currentGoal;

	}

	public void onTimerStarted() {
		if (!isCurrentGoal) return;
		showScoreboard();
	}

	public boolean equals(Goal anotherGoal) {
		return anotherGoal != null && getChallengeName().equals(anotherGoal.getChallengeName());
	}

	public ChallengeScoreboard getScoreboard() {
		return scoreboard;
	}

	public void showScoreboard() {
		hideScoreboard();
		scoreboard = Challenges.getInstance().getScoreboardManager().getNewScoreboard(getChallengeName()).setDisplayName(ChallengeScoreboard.STANDARD_DISPLAYNAME);
		scoreboard.show();
	}

	public void hideScoreboard() {
		if (scoreboard != null) {
			scoreboard.hide();
			scoreboard.destroyScoreboard();
		}
	}

	public final AnimationSound getSound() {
		return sound;
	}
}
