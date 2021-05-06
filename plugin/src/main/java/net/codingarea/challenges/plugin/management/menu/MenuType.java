package net.codingarea.challenges.plugin.management.menu;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public enum MenuType {

	TIMER("Timer", false),
	GOAL("Goal"),
	DAMAGE("Damage"),
	ITEMS_BLOCKS("Items & Blocks"),
	CHALLENGES("Challenges"),
	SETTINGS("Settings");

	private final String name;
	private final boolean usable;

	MenuType(@Nonnull String name, boolean usable) {
		this.name = name;
		this.usable = usable;
	}

	MenuType(@Nonnull String name) {
		this(name, true);
	}

	@Nonnull
	public String getName() {
		return name;
	}

	public boolean isUsable() {
		return usable;
	}

}
