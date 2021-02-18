package net.codingarea.challenges.plugin.management.menu;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class TitleManager {

	private TitleManager() { }

	@Nonnull
	public static String getMainMenuTitle() {
		return "§8» §9Menu";
	}

	@Nonnull
	public static String getTitle(@Nonnull MenuType menu, int page) {
		return "§8» §9" + menu.getName() + " §8┃ §9" + (page + 1);
	}

	@Nonnull
	public static String getMenuSettingTitle(@Nonnull MenuType menu, @Nonnull String name, int page, boolean showPages) {
		return "§8» §9" + menu.getName() + " §8┃ §9" + name + (showPages && false /* temporarily disabled */ ? " §8• §9" + (page + 1) : "");
	}

}
