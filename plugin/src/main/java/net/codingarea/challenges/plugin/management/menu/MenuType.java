package net.codingarea.challenges.plugin.management.menu;

import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.generator.MenuGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.categorised.CategorisedMenuGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.categorised.SmallCategorisedMenuGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.SettingsMenuGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.TimerMenuGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.custom.MainCustomMenuGenerator;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public enum MenuType {

	TIMER("timer", Material.CLOCK, new TimerMenuGenerator(), false),
	GOAL("goal", Material.COMPASS, new SmallCategorisedMenuGenerator()),
	DAMAGE("damage", Material.IRON_SWORD, new SettingsMenuGenerator()),
	ITEMS_BLOCKS("item_blocks", Material.STICK, new SettingsMenuGenerator()),
	CHALLENGES("challenges", Material.BOOK, new CategorisedMenuGenerator()),
	SETTINGS("settings", Material.COMPARATOR, new SettingsMenuGenerator()),
	CUSTOM("custom", Material.WRITABLE_BOOK, new MainCustomMenuGenerator());

	private final String key;
	private final Material displayItem;
	private final MenuGenerator menuGenerator;
	private final boolean usable;

	MenuType(@Nonnull String key, @Nonnull Material displayItem, MenuGenerator menuGenerator, boolean usable) {
		this.key = key;
		this.displayItem = displayItem;
		this.menuGenerator = menuGenerator;
		this.usable = usable;

		menuGenerator.setMenuType(this);
	}

	MenuType(@Nonnull String key, @Nonnull Material displayItem, MenuGenerator menuGenerator) {
		this(key, displayItem, menuGenerator, true);
	}

	@Nonnull
	public String getName() {
		return ChatColor.stripColor(getDisplayName());
	}

	@Nonnull
	public String getDisplayName() {
		return Message.forName("menu-" + key).asString();
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
