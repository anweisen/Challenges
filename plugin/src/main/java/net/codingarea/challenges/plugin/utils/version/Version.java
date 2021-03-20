package net.codingarea.challenges.plugin.utils.version;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface Version {

	@Nonnegative
	int getMajor();

	@Nonnegative
	int getMinor();

	@Nonnegative
	int getRevision();


	default boolean isNewerThan(@Nonnull Version other) {
		if (this.getMajor() > other.getMajor()) return true; // 2.?.? > 1.?.?
		if (this.getMajor() == other.getMajor()
		 && this.getMinor() > other.getMinor()) return true; // 2.1.? > 2.0.?
		if (this.getMajor() == other.getMajor()
		 && this.getMinor() == other.getMinor()
		 && this.getRevision() > other.getRevision()) return true; // 2.0.1 > 2.0.0
		return false;
	}

	default boolean isNewerOrEqualThan(@Nonnull Version other) {
		return equals(other) || isNewerThan(other);
	}

	default boolean isOlderThan(@Nonnull Version other) {
		if (this.getMajor() < other.getMajor()) return true; // 1.?.? < 2.?.?
		if (this.getMajor() == other.getMajor()
		 && this.getMinor() < other.getMinor()) return true; // 1.0.? < 1.0.?
		if (this.getMajor() == other.getMajor()
	 	 && this.getMinor() == other.getMinor()
		 && this.getRevision() < other.getRevision()) return true; // 1.0.0 < 1.0.1
		return false;
	}

	default boolean isOlderOrEqualThan(@Nonnull Version other) {
		return equals(other) || isOlderThan(other);
	}

	default boolean equals(@Nonnull Version other) {
		return this.getMajor()    == other.getMajor() &&
			   this.getMinor()    == other.getMinor() &&
			   this.getRevision() == other.getRevision();
	}

	@Nonnull
	default String format() {
		int revision = getRevision();
		return revision > 0 ? String.format("%s.%s.%s", getMajor(), getMinor(), revision)
							: String.format("%s.%s",    getMajor(), getMinor());
	}

}
