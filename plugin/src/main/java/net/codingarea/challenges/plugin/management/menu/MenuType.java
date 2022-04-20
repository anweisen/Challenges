package net.codingarea.challenges.plugin.management.menu;

import net.codingarea.challenges.plugin.management.menu.generator.MenuGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.SettingsMenuGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.TimerMenuGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.custom.MainCustomMenuGenerator;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public enum MenuType {

	TIMER("Timer", "§6Timer", Material.CLOCK, new TimerMenuGenerator(), false),
	GOAL("Goal", "§5Goal", Material.COMPASS, new SettingsMenuGenerator()),
	DAMAGE("Damage", "§7Damage", Material.IRON_SWORD, new SettingsMenuGenerator()),
	ITEMS_BLOCKS("Items & Blocks", "§4Blocks & Items", Material.STICK, new SettingsMenuGenerator()),
	CHALLENGES("Challenges", "§cChallenges", Material.BOOK, new SettingsMenuGenerator()),
	SETTINGS("Settings", "§eSettings", Material.COMPARATOR, new SettingsMenuGenerator()),
	CUSTOM("Custom", "§aCustom", Material.WRITABLE_BOOK, new MainCustomMenuGenerator());

	private final String name;
	private final String displayName;
	private final Material displayItem;
	private final MenuGenerator menuGenerator;
	private final boolean usable;

	MenuType(@Nonnull String name, @Nonnull String displayName, @Nonnull Material displayItem, MenuGenerator menuGenerator, boolean usable) {
		this.name = name;
		this.displayName = displayName;
		this.displayItem = displayItem;
		this.menuGenerator = menuGenerator;
		this.usable = usable;

		menuGenerator.setMenuType(this);
	}

	MenuType(@Nonnull String name, @Nonnull String displayName, @Nonnull Material displayItem, MenuGenerator menuGenerator) {
		this(name, displayName, displayItem, menuGenerator, true);
	}

	@Nonnull
	public String getName() {
		return name;
	}

	@Nonnull
	public String getDisplayName() {
		return displayName;
	}

	public Material getDisplayItem() {
		return displayItem;
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
