package net.codingarea.challenges.plugin.utils.misc;

import org.bukkit.Location;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class BlockUtils {

	private BlockUtils() {}

	public static boolean isSameBlock(@Nonnull Location loc1, @Nonnull Location loc2) {
		return loc1.getBlockX() == loc2.getBlockX()
		 	&& loc1.getBlockY() == loc2.getBlockY()
			&& loc1.getBlockZ() == loc2.getBlockZ();
	}

	public static boolean isSameBlockIgnoreHeight(@Nonnull Location loc1, @Nonnull Location loc2) {
		return loc1.getBlockX() == loc2.getBlockX()
			&& loc1.getBlockZ() == loc2.getBlockZ();
	}

	public static boolean isSameLocation(@Nonnull Location loc1, @Nonnull Location loc2) {
		return loc1.distance(loc2) < 0.1;
	}

	public static boolean isSameLocationIgnoreHeight(@Nonnull Location loc1, @Nonnull Location loc2) {
		return loc1.getX() == loc2.getX()
			&& loc1.getZ() == loc2.getZ();
	}

}
