package net.codingarea.challenges.plugin.management.challenges;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.implementation.setting.*;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ChallengeLoader {

	public void load() {
		register(new OneLifeSetting());
	}

	private void register(@Nonnull IChallenge challenge) {
		Challenges.getInstance().getChallengeManager().register(challenge);
	}

}
