package net.codingarea.challenges.plugin.management.menu.generator;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.bukkit.utils.menu.MenuClickInfo;
import net.anweisen.utilities.bukkit.utils.menu.MenuPosition;
import net.codingarea.challenges.plugin.management.menu.InventoryTitleManager;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.custom.MainMenuGenerator;
import net.codingarea.challenges.plugin.management.menu.position.GeneratorMenuPosition;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public abstract class ChooseItemGenerator extends MultiPageMenuGenerator {

	private final LinkedHashMap<String, ItemStack> items;

	public ChooseItemGenerator(LinkedHashMap<String, ItemStack> items) {
		this.items = items;
	}

	@Override
	public MenuPosition getMenuPosition(int page) {
		return new GeneratorMenuPosition(this, page) {

			@Override
			public void handleClick(@Nonnull MenuClickInfo info) {

				if (InventoryUtils.handleNavigationClicking(generator, getNavigationSlots(page), page, info)) {
					onBackToMenuItemClick(info.getPlayer());
					return;
				}

				int slot = info.getSlot();
				int index = getItemsPerPage() * page + slot - 10;

				if (slot > 18) index -= 2;
				if (slot > 27) index -= 2;

				if (index >= items.size()) {
					SoundSample.CLICK.play(info.getPlayer());
					return;
				}

				String[] array = items.keySet().toArray(new String[0]);

				if (index >= array.length || isSideSlot(slot) || isTopOrBottomSlot(slot)) {
					SoundSample.CLICK.play(info.getPlayer());
					return;
				}

				String itemKey = array[index];

				onItemClick(info.getPlayer(), itemKey);
				SoundSample.LEVEL_UP.play(info.getPlayer());

			}
		};
	}

	@Override
	public int getSize() {
		return 5*9;
	}

	@Override
	public int getPagesCount() {
		return (items.size() / getItemsPerPage()) + 1;
	}

	public int getItemsPerPage() {
		return 3*7;
	}

	@Override
	public void generatePage(@Nonnull Inventory inventory, int page) {

		int lastSlot = 10;
		int startIndex = getItemsPerPage() * page;
		for (int i = startIndex; i < startIndex + getItemsPerPage() && i < items.size(); i++) {
			String key = items.keySet().toArray(new String[0])[i];
			ItemStack itemStack = items.get(key);
			lastSlot = getNextMiddleSlot(lastSlot);
			inventory.setItem(lastSlot, itemStack);
			lastSlot++;
		}

	}

	private static int getNextMiddleSlot(@Nonnegative int currentSlot) {
		if (currentSlot >= 53) return currentSlot;
		if (isSideSlot(currentSlot)) return getNextMiddleSlot(currentSlot + 1);
		return currentSlot;
	}

	private static boolean isSideSlot(@Nonnegative int slot) {
		return slot % 9 == 0 || slot % 9 == 8;
	}

	private static boolean isTopOrBottomSlot(@Nonnegative int slot) {
		return slot < 9 || slot > 35;
	}

	@Override
	public int[] getNavigationSlots(int page) {
		return MainMenuGenerator.NAVIGATION_SLOTS;
	}

	@Override
	protected String getTitle(int page) {
		LinkedList<String> list = new LinkedList<>(Arrays.asList(getSubTitles(page)));
		list.add(String.valueOf(page+1));

		return InventoryTitleManager.getTitle(MenuType.CUSTOM, list.toArray(new String[0]));
	}

	public abstract String[] getSubTitles(int page);
	public abstract void onItemClick(Player player, String itemKey);
	public abstract void onBackToMenuItemClick(Player player);

}
