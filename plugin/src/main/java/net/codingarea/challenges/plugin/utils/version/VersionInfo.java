package net.codingarea.challenges.plugin.utils.version;

import net.codingarea.challenges.plugin.utils.logging.Logger;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class VersionInfo implements Version {

	private final int major, minor, revision;

	public VersionInfo() {
		this(1, 0, 0);
	}

	public VersionInfo(int major, int minor, int revision) {
		this.major = major;
		this.minor = minor;
		this.revision = revision;
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public int getRevision() {
		return revision;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		VersionInfo that = (VersionInfo) o;
		return major == that.major &&
			   minor == that.minor &&
			   revision == that.revision;
	}

	@Override
	public int hashCode() {
		return Objects.hash(major, minor, revision);
	}

	@Override
	public String toString() {
		return this.format();
	}

	public static VersionInfo parse(@Nullable String input) {
		try {
			String[] array = input.split("\\.");
			if (array.length == 0) throw new IllegalArgumentException("Version cannot be empty");
			int major = Integer.parseInt(array[0]);
			int minor = array.length >= 2 ? Integer.parseInt(array[1]) : 0;
			int revision = array.length >= 3 ? Integer.parseInt(array[2]) : 0;
			return new VersionInfo(major, minor, revision);
		} catch (Exception ex) {
			Logger.severe("Could not parse version for input '" + input + "': " + ex.getMessage());
			return new VersionInfo();
		}
	}

}
