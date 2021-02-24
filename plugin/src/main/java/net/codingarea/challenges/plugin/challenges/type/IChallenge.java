package net.codingarea.challenges.plugin.challenges.type;

import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.event.MenuClickEvent;
import net.codingarea.challenges.plugin.utils.config.Document;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface IChallenge {

	boolean isEnabled();

	@Nonnull
	String getName();

	@Nonnull
	MenuType getType();

	@Nonnull
	ItemStack getDisplayItem();

	@Nonnull
	ItemStack getSettingsItem();

	void handleClick(@Nonnull MenuClickEvent event);

	void writeGameState(@Nonnull Document document);
	void loadGameState(@Nonnull Document document);

	void writeSettings(@Nonnull Document document);
	void loadSettings(@Nonnull Document document);

}