package net.codingarea.challengesplugin.challengetypes;

import net.codingarea.challengesplugin.Challenges;
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
 * https://github.com/Traolian
 */

public abstract class Goal extends GeneralChallenge implements ITimerStatusExecutor {

	protected AnimationSound sound = AnimationSound.STANDARD_SOUND;
	protected boolean isCurrentGoal;
	protected ChallengeScoreboard scoreboard;
	protected String name;

	public abstract void onEnable(ChallengeEditEvent event);
	public abstract void onDisable(ChallengeEditEvent event);

	public abstract List<Player> getWinners();

	@Override
	public void handleClick(ChallengeEditEvent event) { }

	@Override
	public ItemStack getActivationItem() {
		return Challenges.getInstance().getItemManager().getActivationItem(isCurrentGoal);
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

		if (sound != null) {
			sound.play(event.getPlayer());
		}

		this.isCurrentGoal = currentGoal;

	}

	public boolean equals(Goal anotherGoal) {
		return this.name.equals(anotherGoal.name);
	}

	public ChallengeScoreboard getScoreboard() {
		return scoreboard;
	}

	public void showScoreboard() {

		if (scoreboard != null) {
			scoreboard.hide();
			scoreboard.destroyScoreboard();
		}

		scoreboard = Challenges.getInstance().getScoreboardManager().getNewScoreboard(name).setDisplayName(ChallengeScoreboard.STANDARD_DISPLAYNAME);
		scoreboard.show();

	}

	public void hideScoreboard() {

		if (scoreboard != null) {
			scoreboard.hide();
			scoreboard.destroyScoreboard();
		}

	}

}
