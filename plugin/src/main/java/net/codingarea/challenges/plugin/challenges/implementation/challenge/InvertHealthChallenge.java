package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.challenges.type.TimedChallenge;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.Scheduled;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.annotations.Since;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.RandomizeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@Since("2.0")
public class InvertHealthChallenge extends TimedChallenge {

	private final Random random = new Random();

	public InvertHealthChallenge() {
		super(MenuType.CHALLENGES, 1, 10, 5);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.POPPY, Message.forName("item-invert-health-challenge"));
	}

	@Override
	protected int getSecondsUntilNextActivation() {
		return RandomizeUtils.getAround(random, getValue() * 60, 20);
	}

	@Override
	@Scheduled(ticks = 20, async = false)
	public void onSecond() {
		super.onSecond();
	}

	@Override
	protected void onTimeActivation() {
		SoundSample.PLOP.broadcast();
		Message.forName("health-inverted").broadcast(Prefix.CHALLENGES);
		for (Player player : Bukkit.getOnlinePlayers()) {
			double health = player.getMaxHealth() - player.getHealth();
			player.setHealth(health);
		}
		restartTimer();
	}

}
