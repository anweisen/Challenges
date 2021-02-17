package net.codingarea.challenges.plugin.utils.misc;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class InventoryUtils {

	public static void fillInventory(@Nonnull Inventory inventory, @Nullable ItemStack item) {
		for (int i = 0; i < inventory.getSize(); i++) {
			inventory.setItem(i, item);
		}
	}

	public static void close(@Nonnull Inventory inventory) {
		for (HumanEntity viewer : inventory.getViewers().toArray(new HumanEntity[0])) {
			viewer.closeInventory();
		}
	}

	public static void close(@Nonnull Iterable<Inventory> inventories) {
		for (Inventory inventory : inventories) {
			close(inventory);
		}
	}

}
