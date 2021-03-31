package net.codingarea.challenges.plugin.utils.misc;

import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class InventoryUtils {

	private InventoryUtils() {}

	public static void fillInventory(@Nonnull Inventory inventory, @Nullable ItemStack item) {
		for (int i = 0; i < inventory.getSize(); i++) {
			inventory.setItem(i, item);
		}
	}

	public static void fillInventory(@Nonnull Inventory inventory, @Nullable ItemStack item, @Nonnull int... slots) {
		for (int i : slots) {
			inventory.setItem(i, item);
		}
	}

	public static void setNavigationItems(@Nonnull List<Inventory> inventories, @Nonnull int[] navigationSlots) {
		setNavigationItems(inventories, navigationSlots, true);
	}

	public static void setNavigationItems(@Nonnull List<Inventory> inventories, @Nonnull int[] navigationSlots, boolean goBackExit) {
		for (int i = 0; i < inventories.size(); i++) {
			Inventory inventory = inventories.get(i);
			ItemStack left = i == 0 && goBackExit ? DefaultItem.navigateBackMainMenu().build() : DefaultItem.navigateBack().build();
			inventory.setItem(navigationSlots[0], left);
			if (i < (inventories.size() - 1))
				inventory.setItem(navigationSlots[1], DefaultItem.navigateNext().build());
		}
	}

	public static boolean isEmpty(@Nonnull Inventory inventory) {
		for (ItemStack content : inventory.getContents()) {
			if (content != null) return false;
		}
		return true;
	}

	public static boolean inventoryContainsSequence(@Nonnull Inventory inventory, @Nonnull ItemStack[] sequence) {
		for (int i = 0; i < sequence.length && i < inventory.getSize(); i++) {
			ItemStack expected = sequence[i];
			ItemStack found = inventory.getItem(i);
			if (expected == null && found == null)  continue;
			if (expected == null)                   return false;
			if (found == null)                      return false;
			if (!expected.isSimilar(found))         return false;
		}
		return true;
	}

	public static int getRandomEmptySlot(@Nonnull Inventory inventory) {
		List<Integer> emptySlots = new ArrayList<>();

		for (int slot = 0; slot < inventory.getSize(); slot++) {
			if (inventory.getItem(slot) == null) {
				emptySlots.add(slot);
			}

		}

		if (emptySlots.isEmpty()) return -1;
		return emptySlots.get(new Random().nextInt(emptySlots.size()));
	}

	public static int getRandomFullSlot(@Nonnull Inventory inventory) {
		List<Integer> fullSlots = new ArrayList<>();

		for (int slot = 0; slot < inventory.getSize(); slot++) {
			if (inventory.getItem(slot) != null) {
				fullSlots.add(slot);
			}

		}

		if (fullSlots.isEmpty()) return -1;

		return fullSlots.get(new Random().nextInt(fullSlots.size()));
	}

	public static int getRandomSlot(@Nonnull Inventory inventory) {
		return new Random().nextInt(inventory.getSize());
	}

}
