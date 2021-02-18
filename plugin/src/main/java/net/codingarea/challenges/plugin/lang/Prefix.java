package net.codingarea.challenges.plugin.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public enum Prefix {

	CHALLENGES("§6Challenges");

	private final String defaultValue;
	private String value;

	Prefix(@Nonnull String defaultValue) {
		this.defaultValue = "§8§l┃ " + defaultValue + " §8┃ §7";
	}

	public void setValue(@Nullable String value) {
		this.value = value;
	}

	@Nonnull
	public String toString() {
		return value == null ? defaultValue : value;
	}

}
