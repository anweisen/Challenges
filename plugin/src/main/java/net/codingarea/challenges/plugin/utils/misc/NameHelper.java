package net.codingarea.challenges.plugin.utils.misc;

import net.codingarea.challenges.plugin.Challenges;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class NameHelper {

	private NameHelper() {
	}

	@Nonnull
	public static String getName(@Nonnull Player player) {
		if (Challenges.getInstance().getCloudNetHelper().isNameSupport()) {
			return Challenges.getInstance().getCloudNetHelper().getColoredName(player);
		}

		return player.getName();
	}

}
