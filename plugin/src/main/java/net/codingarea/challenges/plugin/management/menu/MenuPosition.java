package net.codingarea.challenges.plugin.management.menu;

import net.codingarea.challenges.plugin.Challenges;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@FunctionalInterface
public interface MenuPosition {

	InventoryHolder HOLDER = () -> null;

	void handleClick(@Nonnull Player player, int slot, @Nonnull Inventory inventory, @Nonnull InventoryClickEvent event);

	static void set(@Nonnull Player player, @Nullable MenuPosition position) {
		Challenges.getInstance().getMenuManager().setPostion(player, position);
	}

	@Nullable
	static MenuPosition get(@Nonnull Player player) {
		return Challenges.getInstance().getMenuManager().getPosition(player);
	}

}
