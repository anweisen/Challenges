package net.codingarea.challenges.plugin.utils.misc;

import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class InventoryUtils {

	private InventoryUtils() {
	}

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

	public static void setNavigationItems(@Nonnull List<Inventory> inventories, @Nonnull int[] navigationSlots) {
		setNavigationItems(inventories, navigationSlots, true);
	}

	public static void setNavigationItems(@Nonnull List<Inventory> inventories, @Nonnull int[] navigationSlots, boolean goBackExit) {
		for (int i = 0; i < inventories.size(); i++) {
			Inventory inventory = inventories.get(i);
			ItemStack left = i == 0 && goBackExit ? DefaultItem.navigateBackMainMenu() : DefaultItem.navigateBack();
			inventory.setItem(navigationSlots[0], left);
			if (i < (inventories.size() - 1))
				inventory.setItem(navigationSlots[1], DefaultItem.navigateNext());
		}
	}

}
