package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.*;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@Since("2.0")
public class RandomChallengeChallenge extends TimedChallenge {

	private final Random random = new Random();

	private IChallenge lastUsed;

	public RandomChallengeChallenge() {
		super(MenuType.CHALLENGES, 30, 600, 60, false);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.REDSTONE, Message.forName("item-random-challenge-challenge"));
	}

	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return Message.forName("item-time-seconds-description").asArray(getValue());
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChallengeSecondsValueChangeTitle(this, getValue());
	}

	@Override
	protected void onDisable() {
		if (lastUsed != null) {
			setEnabled(lastUsed, false);
			lastUsed = null;
		}
	}

	@Override
	protected int getSecondsUntilNextActivation() {
		return getValue();
	}

	@Override
	protected void onTimeActivation() {
		restartTimer();

		if (lastUsed != null) {
			setEnabled(lastUsed, false);
			lastUsed = null;
		}

		List<IChallenge> challenges = new ArrayList<>(Challenges.getInstance().getChallengeManager().getChallenges());
		challenges.remove(this);
		challenges.removeIf(challenge -> challenge.getType() != MenuType.CHALLENGES);
		challenges.removeIf(IChallenge::isEnabled);
		if (challenges.isEmpty()) return;

		IChallenge challenge = challenges.get(random.nextInt(challenges.size()));
		if (challenge instanceof AbstractChallenge) {
			AbstractChallenge abstractChallenge = (AbstractChallenge) challenge;
			String name = ChallengeHelper.getColoredChallengeName(abstractChallenge);
			Message.forName("random-challenge-enabled").broadcast(Prefix.CHALLENGES, name);
		}

		setEnabled(challenge, true);
		lastUsed = challenge;

	}

	private void setEnabled(@Nonnull IChallenge challenge, boolean enabled) {
		if (challenge instanceof Setting) {
			Setting setting = (Setting) challenge;
			setting.setEnabled(enabled);
		}
		if (challenge instanceof SettingModifier) {
			SettingModifier setting = (SettingModifier) challenge;
			setting.setEnabled(enabled);
		}

		if (challenge instanceof TimedChallenge) {
			TimedChallenge timedChallenge = (TimedChallenge) challenge;
			timedChallenge.executeTimeActivation();
		}
	}

}