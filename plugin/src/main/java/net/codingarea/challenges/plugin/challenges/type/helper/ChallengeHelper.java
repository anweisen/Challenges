package net.codingarea.challenges.plugin.challenges.type.helper;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.AbstractChallenge;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;
import net.codingarea.challenges.plugin.challenges.type.Modifier;
import net.codingarea.challenges.plugin.lang.ItemDescription;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.info.ChallengeMenuClickInfo;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ChallengeHelper {

	private ChallengeHelper() {
	}

	public static void updateItems(@Nonnull IChallenge challenge) {
		Challenges.getInstance().getMenuManager().getMenu(challenge.getType()).updateItem(challenge);
	}

	public static void handleModifierClick(@Nonnull ChallengeMenuClickInfo info, @Nonnull Modifier modifier) {
		int newValue = modifier.getValue();
		int amount = info.isShiftClick() ? 10 : 1;
		if (info.isRightClick()) {
			newValue -= amount;
		} else {
			newValue += amount;
		}

		if (newValue > modifier.getMaxValue())
			newValue = modifier.getMinValue();
		if (newValue < modifier.getMinValue())
			newValue = modifier.getMaxValue();

		modifier.setValue(newValue);
		modifier.playValueChangeTitle();
		SoundSample.CLICK.play(info.getPlayer());
	}

	@Nonnull
	public static String getColoredChallengeName(@Nonnull AbstractChallenge challenge) {
		ItemBuilder item = challenge.createDisplayItem();
		if (item == null) return Message.NULL;
		ItemDescription description = item.getBuiltByItemDescription();
		if (description == null) return Message.NULL;
		return description.getOriginalName();
	}

	public static void playToggleChallengeTitle(@Nonnull AbstractChallenge challenge) {
		playToggleChallengeTitle(challenge, challenge.isEnabled());
	}

	public static void playToggleChallengeTitle(@Nonnull AbstractChallenge challenge, boolean enabled) {
		Challenges.getInstance().getTitleManager().sendChallengeStatusTitle(enabled ? Message.forName("title-challenge-enabled") : Message.forName("title-challenge-disabled"), getColoredChallengeName(challenge));
	}

	public static void playChangeChallengeValueTitle(@Nonnull Modifier modifier) {
		playChangeChallengeValueTitle(modifier, modifier.getValue());
	}

	public static void playChangeChallengeValueTitle(@Nonnull Modifier modifier, @Nullable Object value) {
		Challenges.getInstance().getTitleManager().sendChallengeStatusTitle(Message.forName("title-challenge-value-changed"), getColoredChallengeName(modifier), value);
	}

}
