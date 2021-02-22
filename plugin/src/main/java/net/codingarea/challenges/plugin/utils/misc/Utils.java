package net.codingarea.challenges.plugin.utils.misc;

import net.codingarea.challenges.plugin.Challenges;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public final class Utils {

	private Utils() {
	}

	public static boolean isSpigot() {
		try {
			Bukkit.spigot();
			return true;
		} catch (NoSuchMethodError ex) {
			return false;
		}
	}

	public static void disablePlugin() {
		Bukkit.getPluginManager().disablePlugin(Challenges.getInstance());
	}

	@Nonnull
	public static List<String> filterRecommendations(@Nonnull String argument, @Nonnull String... recommendations) {
		argument = argument.toLowerCase();
		List<String> list = new ArrayList<>();
		for (String current : recommendations) {
			if (current.startsWith(argument))
				list.add(current);
		}
		Collections.sort(list);
		return list;
	}

}
