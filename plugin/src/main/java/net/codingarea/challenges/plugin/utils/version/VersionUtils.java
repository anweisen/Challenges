package net.codingarea.challenges.plugin.utils.version;

import net.codingarea.challenges.plugin.utils.annotations.Since;
import net.codingarea.challenges.plugin.utils.logging.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class VersionUtils {

	private VersionUtils() {
	}

	@Nonnull
	public static <V extends Version> V findNearest(@Nonnull Version version, @Nonnull V[] versions) {
		return findNearest(version.getMajor(), version.getMinor(), version.getRevision(), versions);
	}

	public static <V extends Version> V findNearest(int major, int minor, int revision, @Nonnull V[] versionsArray) {
		List<V> versions = new ArrayList<>(Arrays.asList(versionsArray));
		Collections.reverse(versions);
		for (V version : versions) {
			if (version.getMajor() > major) continue;
			if (version.getMinor() > minor) continue;
			if (version.getRevision() > revision) continue;
			return version;
		}
		throw new IllegalArgumentException("No version found for '" + major + "." + minor + "." + revision + "'");
	}

	@Nonnull
	public static Version getSince(@Nonnull Object object) {
		if (!object.getClass().isAnnotationPresent(Since.class)) return new VersionInfo();
		return VersionInfo.parse(object.getClass().getAnnotation(Since.class).value());
	}

	@Nonnull
	public static Version parseFromCraftBukkit(@Nonnull Class<?> clazz) {

		String prefix = "org.bukkit.craftbukkit.";
		String name = clazz.getName();
		if (!name.startsWith(prefix)) {
			Logger.severe(clazz.getName() + " is not a craftbukkit class");
			return new VersionInfo();
		}

		String version = name.substring(prefix.length()); // v{major}_{minor}_R{revision}
		version = version.substring(0, version.indexOf("."));
		String[] split = version.split("_");
		if (split.length != 3) {
			Logger.severe(version + " does not match pattern of v{major}_{minor}_R{revision}");
			return new VersionInfo();
		}

		int major    = Integer.parseInt(split[0].substring(1));
		int minor    = Integer.parseInt(split[1]);
		int revision = Integer.parseInt(split[2].substring(1));

		return new VersionInfo(major, minor, revision);

	}

}
