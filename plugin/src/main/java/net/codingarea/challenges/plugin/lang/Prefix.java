package net.codingarea.challenges.plugin.lang;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public enum Prefix {

	CHALLENGES("§6Challenges");

	private final String value;

	Prefix(@Nonnull String value) {
		this.value = value;
	}

	@Nonnull
	public String toString() {
		return "§8§l┃ " + value + " §8┃ §7";
	}

}
