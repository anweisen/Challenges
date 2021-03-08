package net.codingarea.challenges.plugin.management.menu;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.event.ChallengeMenuClickEvent;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import net.codingarea.challenges.plugin.utils.version.Version;
import net.codingarea.challenges.plugin.utils.version.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class Menu {

	public static final int[] SLOTS = { 10, 11, 12, 13, 14, 15, 16 };
	public static final int[] NAVIGATION_SLOTS = { 27, 35 };
	public static final int SIZE = 4*9;

	private final ArrayList<IChallenge> challenges = new ArrayList<>();
	private final List<Inventory> inventories = new ArrayList<>();

	private final MenuType menu;

	public Menu(@Nonnull MenuType menu) {
		this.menu = menu;
	}

	void addChallengeCache(@Nonnull IChallenge challenge) {
		if (isNew(challenge) && Challenges.getInstance().getMenuManager().isDisplayNewInFront()) {
			challenges.add(0, challenge);
		} else {
			challenges.add(challenge);
		}
	}
	void resetChallengesCache() {
		challenges.clear();
	}

	public void generateInventories() {

		InventoryUtils.close(inventories);
		inventories.clear();

		int page = 0;
		int index = 0;
		for (IChallenge challenge : challenges) {

			Inventory inventory;

			if (index >= SLOTS.length) { // Current page is full
				index = 0;
				inventory = createNewInventory(++page);
			} else if (inventories.isEmpty()) { // This is the first challenge
				inventory = createNewInventory(page);
			} else {
				inventory = inventories.get(page);
			}

			int displaySlot = SLOTS[index];
			int settingsSlot = displaySlot + 9;

			inventory.setItem(displaySlot, getDisplayItem(challenge));
			inventory.setItem(settingsSlot, challenge.getSettingsItem());

			index++;
		}

		InventoryUtils.setNavigationItems(inventories, NAVIGATION_SLOTS);

	}

	public void updateItem(@Nonnull IChallenge challenge) {

		int index = challenges.indexOf(challenge);
		if (index == -1) return; // Challenge not registered or menus not loaded

		int page = index / SLOTS.length;
		if (page >= inventories.size()) return; // This should never happen

		int slot = index - SLOTS.length * page;

		Inventory inventory = inventories.get(page);
		inventory.setItem(SLOTS[slot], getDisplayItem(challenge));
		inventory.setItem(SLOTS[slot] + 9, challenge.getSettingsItem());

	}

	private ItemStack getDisplayItem(@Nonnull IChallenge challenge) {
		ItemBuilder item = new ItemBuilder(challenge.getDisplayItem()).hideAttributes();
		if (isNew(challenge)) {
			return item.appendName(" " + Message.NEW_CHALLENGE).build();
		} else {
			return item.build();
		}
	}

	private boolean isNew(@Nonnull IChallenge challenge) {
		Version version = Challenges.getInstance().getVersion();
		Version since   = VersionUtils.getSince(challenge);
		return since.isNewerOrEqualThan(version);
	}

	@Nonnull
	private Inventory createNewInventory(int page) {
		Inventory inventory = Bukkit.createInventory(MenuPosition.HOLDER, SIZE, TitleManager.getTitle(menu, page));
		inventory.setMaxStackSize(1000);
		InventoryUtils.fillInventory(inventory, ItemBuilder.FILL_ITEM);
		inventories.add(inventory);
		return inventory;
	}

	public void open(@Nonnull Player player, int page) {
		if (inventories.isEmpty()) return; // This will only happen, when there are no challenges registered to this MenuType
		if (page >= inventories.size()) page = inventories.size() - 1;
		Inventory inventory = inventories.get(page);
		Challenges.getInstance().getMenuManager().setPostion(player, new SubMenuPosition(page));
		player.openInventory(inventory);
	}

	@Nonnull
	public MenuType getMenu() {
		return menu;
	}

	@Nonnull
	List<Inventory> getInventories() {
		return inventories;
	}

	private class SubMenuPosition implements MenuPosition {

		private final int page;

		public SubMenuPosition(@Nonnegative int page) {
			this.page = page;
		}

		@Override
		public void handleClick(@Nonnull Player player, int slot, @Nonnull Inventory inventory, @Nonnull InventoryClickEvent event) {

			if (slot == NAVIGATION_SLOTS[0]) {
				SoundSample.CLICK.play(player);
				if (page == 0) {
					Challenges.getInstance().getMenuManager().openGUIInstantly(player);
				} else {
					open(player, page - 1);
				}
				return;
			} else if (slot == NAVIGATION_SLOTS[1]) {
				SoundSample.CLICK.play(player);
				if (page < (inventories.size() - 1))
					open(player, page + 1);
				return;
			}

			boolean upperItem = true;
			int index = 0;
			for (int i : SLOTS) {
				if (i == slot) break;
				if ((i + 9 ) == slot) {
					upperItem = false;
					break;
				}
				index++;
			}

			if (index == SLOTS.length) { // No possible bound slot was clicked
				SoundSample.CLICK.play(player);
				return;
			}

			int offset = page * SLOTS.length;
			index += offset;

			if (index >= challenges.size()) { // No bound slot was clicked
				SoundSample.CLICK.play(player);
				return;
			}

			IChallenge challenge = challenges.get(index);

			try {
				challenge.handleClick(new ChallengeMenuClickEvent(player, inventory, event.isRightClick(), event.isShiftClick(), upperItem));
			} catch (Exception ex) {
				Logger.severe("An exception occurred while handling click on " + challenge.getClass().getName(), ex);
			}

		}

	}

}
