package net.codingarea.challenges.plugin.utils.misc;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.management.cloud.CloudSupportManager;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class NameHelper {

	private NameHelper() {}

	@Nonnull
	public static String getName(@Nonnull Player player) {
		CloudSupportManager cloudSupport = Challenges.getInstance().getCloudSupportManager();
		if (cloudSupport.isNameSupport() && cloudSupport.hasNameFor(player.getUniqueId())) {
			return cloudSupport.getColoredName(player);
		}

		return player.getName();
	}

}
