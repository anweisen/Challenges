package net.codingarea.challenges.plugin.utils.misc;

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

}
