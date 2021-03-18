package net.codingarea.challenges.plugin.management.challenges;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.implementation.challenge.*;
import net.codingarea.challenges.plugin.challenges.implementation.goal.*;
import net.codingarea.challenges.plugin.challenges.implementation.setting.*;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;

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
		// Settings
		register(DamageDisplaySetting.class);
		register(OneLifeSetting.class);
		register(RespawnSetting.class);
		register(DeathMessageModifier.class);
		register(MaxHealthModifier.class);
		register(DamageMultiplierModifier.class);
		register(SoupSetting.class);
		registerWithCommand(PositionSetting.class, new String[]{"position"});
		registerWithCommand(BackpackSetting.class, new String[]{"backpack"});
		register(PvPSetting.class);
		register(CutCleanSetting.class);

		// Challenges
		register(InvertHealthChallenge.class);
		register(TrafficLightChallenge.class);
		register(JumpAndRunChallenge.class);
		register(BlockRandomizerChallenge.class);
		register(CraftingRandomizerChallenge.class);

		// Goal
		register(KillEnderDragonGoal.class);
		register(KillWitherGoal.class);
		register(LastManStandingGoal.class);
		register(CollectMostDeathsGoal.class);
		register(CollectMostItemsGoal.class);
		register(MineMostBlocksGoal.class);
	}

	private void registerWithCommand(@Nonnull Class<? extends IChallenge> classOfChallenge, @Nonnull String[] commandNames, @Nonnull Object... arguments) {
		try {

			Class[] classes = new Class[arguments.length];
			for (int i = 0; i < arguments.length; i++) {
				classes[i] = arguments.getClass();
			}

			Constructor<? extends IChallenge> constructor = classOfChallenge.getDeclaredConstructor(classes);
			IChallenge challenge = constructor.newInstance(arguments);

			Challenges.getInstance().getChallengeManager().register(challenge);

			if (challenge instanceof CommandExecutor) {
				Challenges.getInstance().registerCommand((CommandExecutor) challenge, commandNames);
			}
			if (challenge instanceof Listener) {
				Challenges.getInstance().registerListener((Listener) challenge);
			}

		} catch (Throwable ex) {
			Logger.severe("Could not register challenge " + classOfChallenge.getSimpleName(), ex);
		}
	}

	private void register(@Nonnull Class<? extends IChallenge> classOfChallenge, @Nonnull Object... arguments) {
		registerWithCommand(classOfChallenge, new String[0], arguments);
	}

}
