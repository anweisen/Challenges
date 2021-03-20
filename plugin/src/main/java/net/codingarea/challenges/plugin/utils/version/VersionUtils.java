package net.codingarea.challenges.plugin.utils.version;

import net.codingarea.challenges.plugin.utils.annotations.Since;

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
		if (!object.getClass().isAnnotationPresent(Since.class)) return new VersionInfo(1, 0, 0);
		return VersionInfo.parse(object.getClass().getAnnotation(Since.class).value());
	}

}
