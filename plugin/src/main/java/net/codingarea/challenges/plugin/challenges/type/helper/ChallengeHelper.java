package net.codingarea.challenges.plugin.challenges.type.helper;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;

import javax.annotation.Nonnull;

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

	@Nonnull
	public static String getColoredChallengeName(@Nonnull AbstractChallenge challenge) {
		ItemBuilder item = challenge.createDisplayItem();
		if (item == null) return Message.NULL_MESSAGE;
		ItemDescription description = item.getBuiltByItemDescription();
		if (description == null) return Message.NULL_MESSAGE;
		return description.getOriginalName();
	}

}
