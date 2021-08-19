package net.codingarea.challenges.plugin.management.menu.generator;

import net.anweisen.utilities.bukkit.utils.menu.MenuPosition;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public abstract class MenuGenerator {

	private MenuType menuType;

	public abstract void generateInventories();
	public abstract List<Inventory> getInventories();
	public abstract MenuPosition getMenuPosition(@Nonnegative int page);

	public void open(@Nonnull Player player, @Nonnegative int page) {
		List<Inventory> inventories = getInventories();
		if (inventories == null || inventories.isEmpty()) generateInventories();
		if (inventories == null || inventories.isEmpty()) return;
		if (page >= inventories.size()) page = inventories.size() - 1;
		Inventory inventory = inventories.get(page);
		MenuPosition.set(player, getMenuPosition(page));
		player.openInventory(inventory);
	}

	public MenuType getMenuType() {
		return menuType;
	}

	// ONLY MODIFY IF YOU KNOW WHAT YOU ARE DOING
	public void setMenuType(MenuType menuType) {
		this.menuType = menuType;
	}

}
