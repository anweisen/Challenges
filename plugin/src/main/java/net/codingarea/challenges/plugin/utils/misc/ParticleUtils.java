package net.codingarea.challenges.plugin.utils.misc;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

/**
 * This class contains util methods to create fancy particles.
 * Most of these methods were found in {@link Utils} previously (pre 2.0).
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ParticleUtils {

	private ParticleUtils() {
	}

	private static void spawnParticleCircle(@Nonnull Location location, int points, double radius, @Nonnull BiConsumer<World, Location> player) {
		World world = location.getWorld();
		if (world == null) return;

		for (int i = 0; i < points; i++) {
			double angle = 2 * Math.PI * i / points;
			Location point = location.clone().add(radius * Math.sin(angle), 0.0d, radius * Math.cos(angle));
			player.accept(world, point);
		}
	}

	public static void spawnParticleCircle(@Nonnull Location location, @Nonnull Effect particle, int points, double radius) {
		spawnParticleCircle(location, points, radius, (world, point) -> world.playEffect(point, particle, 1));
	}

	public static void spawnParticleCircle(@Nonnull Location location, @Nonnull Particle particle, int points, double radius) {
		spawnParticleCircle(location, points, radius, (world, point) -> world.spawnParticle(particle, point, 1));
	}

	private static void spawnUpGoingParticleCircle(@Nonnull JavaPlugin plugin, @Nonnull Location location, int points, double radius, double height, @Nonnull BiConsumer<World, Location> player) {
		for (double y = 0, i = 0; y < height; y += .25, i++) {
			final double Y = y; // Make final copy
			Bukkit.getScheduler().runTaskLater(plugin, () -> {
				spawnParticleCircle(location.clone().add(0, Y, 0), points, radius, player);
			}, (long) i);
		}
	}

	public static void spawnUpGoingParticleCircle(@Nonnull JavaPlugin plugin, @Nonnull Location location, @Nonnull Effect particle, int points, double radius, double height) {
		spawnUpGoingParticleCircle(plugin, location, points, radius, height, (world, point) -> world.playEffect(point, particle, 1));
	}

	public static void spawnUpGoingParticleCircle(@Nonnull JavaPlugin plugin, @Nonnull Location location, @Nonnull Particle particle, int points, double radius, double height) {
		spawnUpGoingParticleCircle(plugin, location, points, radius, height, (world, point) -> world.spawnParticle(particle, point, 1));
	}

}
