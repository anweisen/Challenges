package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.commons.annotations.Since;
import net.anweisen.utilities.commons.common.IRandom;
import net.codingarea.challenges.plugin.challenges.type.TimedChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.RandomizeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@Since("2.0")
public class InvertHealthChallenge extends TimedChallenge {

	private final IRandom random = IRandom.create();

	public InvertHealthChallenge() {
		super(MenuType.CHALLENGES, 1, 10, 5, false);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.POPPY, Message.forName("item-invert-health-challenge"));
	}

	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return Message.forName("item-time-seconds-range-description").asArray(getValue() * 60 - 20, getValue() * 60 + 20);
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChallengeSecondsRangeValueChangeTitle(this, getValue() * 60 - 20, getValue() * 60 + 20);
	}

	@Override
	protected int getSecondsUntilNextActivation() {
		return random.around(getValue() * 60, 20);
	}

	@Override
	protected void onTimeActivation() {
		SoundSample.PLOP.broadcast();
		Message.forName("health-inverted").broadcast(Prefix.CHALLENGES);
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (ignorePlayer(player)) continue;
			double health = player.getMaxHealth() - player.getHealth();
			player.setHealth(health);
		}
		restartTimer();
	}

}
