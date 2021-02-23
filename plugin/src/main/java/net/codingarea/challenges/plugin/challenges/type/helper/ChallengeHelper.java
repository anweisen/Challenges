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

}
