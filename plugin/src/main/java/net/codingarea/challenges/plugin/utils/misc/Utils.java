package net.codingarea.challenges.plugin.utils.misc;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.common.IOUtils;
import net.codingarea.challenges.plugin.utils.config.document.GsonDocument;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

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

	@Nonnull
	public static UUID fetchUUID(@Nonnull String name) throws IOException {
		String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
		String content = IOUtils.toString(new URL(url));
		GsonDocument document = new GsonDocument(content);
		return Optional.ofNullable(matchUUID(document.getString("id"))).orElseThrow(() -> new IOException());
	}

	@Nullable
	private static UUID matchUUID(@Nullable String uuid) {
		if (uuid == null) return null;
		Pattern pattern = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
		return UUID.fromString(pattern.matcher(uuid).replaceAll("$1-$2-$3-$4-$5"));
	}

}
