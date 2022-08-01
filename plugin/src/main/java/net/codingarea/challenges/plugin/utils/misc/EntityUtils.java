package net.codingarea.challenges.plugin.utils.misc;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public final class EntityUtils {

	private EntityUtils() {
	}

	public static Vector getSucceedingVelocity(@Nonnull Vector vector) {
		return new Vector(vector.getX(), getSucceedingVelocity(vector.getY()), vector.getX());
	}

	public static double getSucceedingVelocity(double currentYVelocity) {
		return 0.98 * ((currentYVelocity) - 0.08);
	}

	public static boolean isStandingOnBlock(@Nonnull Entity entity, Material block) {
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				for (int y = -1; y <= 1; y++) {
					Material type = entity.getLocation().add(x, y, z).getBlock().getType();
					if (type == block) return true;
				}
			}
		}
		return false;
	}

}
