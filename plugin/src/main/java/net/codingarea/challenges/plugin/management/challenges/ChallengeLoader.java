package net.codingarea.challenges.plugin.management.challenges;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.implementation.setting.*;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;

import javax.annotation.Nonnull;

/**
 * This class loads all challenges of this plugin.
 * In pre2 versions, this class was known as PluginChallengeLoader.
 *
 * @author anweisen | https://github.com/anweisen
 * @author KxmischesDomi | https://github.com/kxmischesdomi
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
