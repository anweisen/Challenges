package net.codingarea.challengesplugin.manager.loader;

import net.codingarea.challengesplugin.challengetypes.AbstractChallenge;
import net.codingarea.challengesplugin.manager.ChallengeManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * @author anweisen & KxmischesDomi
 * Challenges developed on 11.08.2020
 * https://www.github.com/anweisen
 * https://www.github.com/KxmischesDomi
 */

public class ChallengeLoader {

	private static final ArrayList<AbstractChallenge> queue = new ArrayList<>();

	public static void register(AbstractChallenge challenge, JavaPlugin plugin, String... commands) {

		if (!isRegistered(challenge)) {

			queue.add(challenge);

			if (challenge instanceof CommandExecutor) {
				for (String currentCommand : commands) {
					plugin.getCommand(currentCommand).setExecutor((CommandExecutor) challenge);
				}
			}

		}

	}

	public static boolean isRegistered(AbstractChallenge challenge) {
		for (AbstractChallenge currentChallenge : queue) {
			if (currentChallenge.equals(challenge)) return true;
		}
		return false;
	}

	public static void load(ChallengeManager manager) {
		manager.load(queue);
	}

}
