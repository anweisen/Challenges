package net.codingarea.challenges.plugin.management.menu;

import net.codingarea.challenges.plugin.management.menu.generator.MenuGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.SettingsMenuGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.TimerMenuGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.custom.MainMenuGenerator;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public enum MenuType {

	TIMER("Timer", new TimerMenuGenerator(), false),
	GOAL("Goal", new SettingsMenuGenerator()),
	DAMAGE("Damage", new SettingsMenuGenerator()),
	ITEMS_BLOCKS("Items & Blocks", new SettingsMenuGenerator()),
	CHALLENGES("Challenges", new SettingsMenuGenerator()),
	SETTINGS("Settings", new SettingsMenuGenerator()),
	CUSTOM("Custom", new MainMenuGenerator());

	private final String name;
	private final MenuGenerator menuGenerator;
	private final boolean usable;

	MenuType(@Nonnull String name, MenuGenerator menuGenerator, boolean usable) {
		this.name = name;
		this.menuGenerator = menuGenerator;
		this.usable = usable;

		menuGenerator.setMenuType(this);
	}

	MenuType(@Nonnull String name, MenuGenerator menuGenerator) {
		this(name, menuGenerator, true);
	}

	@Nonnull
	public String getName() {
		return name;
	}

	public boolean isUsable() {
		return usable;
	}

	public MenuGenerator getMenuGenerator() {
		return menuGenerator;
	}

	@SuppressWarnings("unchecked")
	public <T extends MenuGenerator> void executeWithGenerator(Class<T> clazz, Consumer<T> action) {
		if (clazz.isAssignableFrom(menuGenerator.getClass())) {
			action.accept((T) menuGenerator);
		}
	}

}
