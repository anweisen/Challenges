package net.codingarea.challenges.plugin.management.menu;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class InventoryTitleManager {

	private InventoryTitleManager() {}

	@Nonnull
	public static String getTitle(@Nonnull String name) {
		return "§8» " + name;
	}

	@Nonnull
	public static String getMainMenuTitle() {
		return getTitle("§9Menu");
	}

	@Nonnull
	public static String getTitle(@Nonnull MenuType menu, int page) {
		return getTitle(menu.getName(), page);
	}

	@Nonnull
	public static String getTitle(@Nonnull String menu, int page) {
		return getTitle("§9" + menu + " §8┃ §9" + (page + 1));
	}

	@Nonnull
	public static String getMenuSettingTitle(@Nonnull MenuType menu, @Nonnull String name, int page, boolean showPages) {
		return getTitle("§9" + menu.getName() + " §8┃ §9" + name + (showPages && false /* temporarily disabled */ ? " §8• §9" + (page + 1) : ""));
	}

	@Nonnull
	public static String getStatsTitle(@Nonnull String playerName) {
		return getTitle("§2Stats §8┃ §2" + playerName);
	}

	@Nonnull
	public static String getLeaderboardTitle() {
		return getTitle("§2Leaderboard");
	}

	@Nonnull
	public static String getLeaderboardTitle(@Nonnull String name, int page) {
		return getTitle("§2" + name + " §8┃ §2" + page);
	}

}
