package net.codingarea.challenges.plugin.management.menu;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.bukkit.utils.menu.MenuClickInfo;
import net.anweisen.utilities.bukkit.utils.menu.MenuPosition;
import net.anweisen.utilities.commons.version.Version;
import net.anweisen.utilities.commons.version.VersionInfo;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.info.ChallengeMenuClickInfo;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
public final class SettingsMenu {

	public static final int[] SLOTS = { 10, 11, 12, 13, 14, 15, 16 };
	public static final int[] NAVIGATION_SLOTS = { 27, 35 };
	public static final int SIZE = 4*9;

	private final ArrayList<IChallenge> challenges = new ArrayList<>();
	private final List<Inventory> inventories = new ArrayList<>();

	private final MenuType menu;
	private final boolean newSuffix;

	public SettingsMenu(@Nonnull MenuType menu) {
		this.menu = menu;
		newSuffix = Challenges.getInstance().getConfigDocument().getBoolean("new-suffix");
	}

	void addChallengeCache(@Nonnull IChallenge challenge) {
		if (isNew(challenge) && Challenges.getInstance().getMenuManager().isDisplayNewInFront()) {
			challenges.add(countNewChallenges(), challenge);
		} else {
			challenges.add(challenge);
		}
	}
	void resetChallengesCache() {
		challenges.clear();
	}

	private int countNewChallenges() {
		return (int) challenges.stream().filter(this::isNew).count();
	}

	public void generateInventories() {

		inventories.clear();

		int page = 0;
		int index = 0;
		for (IChallenge challenge : challenges) {

			Inventory inventory;
			if (inventories.isEmpty()) { // This is the first challenge
				inventory = createNewInventory(page);
			} else if (index >= SLOTS.length) { // Current page is full
				index = 0;
				inventory = createNewInventory(++page);
			} else {
				inventory = inventories.get(page);
			}

			inventory.setItem(SLOTS[index], getDisplayItem(challenge));
			inventory.setItem(SLOTS[index] + 9, getSettingsItem(challenge));

			index++;
		}

		InventoryUtils.setNavigationItemsToInventory(inventories, NAVIGATION_SLOTS);

	}

	public void updateItem(@Nonnull IChallenge challenge) {

		int index = challenges.indexOf(challenge);
		if (index == -1) return; // Challenge not registered or menus not loaded

		int page = index / SLOTS.length;
		if (page >= inventories.size()) return; // This should never happen

		int slot = index - SLOTS.length * page;

		Inventory inventory = inventories.get(page);
		inventory.setItem(SLOTS[slot], getDisplayItem(challenge));
		inventory.setItem(SLOTS[slot] + 9, getSettingsItem(challenge));

	}

	private ItemStack getDisplayItem(@Nonnull IChallenge challenge) {
		try {
			ItemBuilder item = new ItemBuilder(challenge.getDisplayItem()).hideAttributes();
			if (newSuffix && isNew(challenge)) {
				return item.appendName(" " + Message.forName("new-challenge")).build();
			} else {
				return item.build();
			}
		} catch (Exception ex) {
			Logger.error("Error while generating challenge display item for challenge {}", challenge.getClass().getSimpleName(), ex);
			return new ItemBuilder().build();
		}
	}

	private ItemStack getSettingsItem(@Nonnull IChallenge challenge) {
		try {
			ItemBuilder item = new ItemBuilder(challenge.getSettingsItem()).hideAttributes();
			return item.build();
		} catch (Exception ex) {
			Logger.error("Error while generating challenge settings item for challenge {}", challenge.getClass().getSimpleName(), ex);
			return new ItemBuilder().build();
		}
	}

	private boolean isNew(@Nonnull IChallenge challenge) {
		Version version = Challenges.getInstance().getVersion();
		Version since   = Version.getAnnotatedSince(challenge);
		return since.isNewerOrEqualThan(version);
	}

	@Nonnull
	private Inventory createNewInventory(int page) {
		Inventory inventory = Bukkit.createInventory(MenuPosition.HOLDER, SIZE, InventoryTitleManager.getTitle(menu, page));
		InventoryUtils.fillInventory(inventory, ItemBuilder.FILL_ITEM);
		inventories.add(inventory);
		return inventory;
	}

	public void open(@Nonnull Player player, int page) {
		if (inventories.isEmpty()) return; // This will only happen, when there are no challenges registered to this MenuType
		if (page >= inventories.size()) page = inventories.size() - 1;
		Inventory inventory = inventories.get(page);
		MenuPosition.set(player, new SubMenuPosition(page));
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
		public void handleClick(@Nonnull MenuClickInfo info) {

			if (info.getSlot() == NAVIGATION_SLOTS[0]) {
				SoundSample.CLICK.play(info.getPlayer());
				if (page == 0 || info.isShiftClick()) {
					Challenges.getInstance().getMenuManager().openGUIInstantly(info.getPlayer());
				} else {
					open(info.getPlayer(), page - 1);
				}
				return;
			} else if (info.getSlot() == NAVIGATION_SLOTS[1]) {
				SoundSample.CLICK.play(info.getPlayer());
				if (page < (inventories.size() - 1))
					open(info.getPlayer(), page + 1);
				return;
			}

			boolean upperItem = true;
			int index = 0;
			for (int i : SLOTS) {
				if (i == info.getSlot()) break;
				if ((i + 9 ) == info.getSlot()) {
					upperItem = false;
					break;
				}
				index++;
			}

			if (index == SLOTS.length) { // No possible bound slot was clicked
				SoundSample.CLICK.play(info.getPlayer());
				return;
			}

			int offset = page * SLOTS.length;
			index += offset;

			if (index >= challenges.size()) { // No bound slot was clicked
				SoundSample.CLICK.play(info.getPlayer());
				return;
			}

			IChallenge challenge = challenges.get(index);

			if (playNoPermissionsEffect(info.getPlayer())) return;

			try {
				challenge.handleClick(new ChallengeMenuClickInfo(info, upperItem));
			} catch (Exception ex) {
				Logger.error("An exception occurred while handling click on {}", challenge.getClass().getName(), ex);
			}

		}

		private boolean playNoPermissionsEffect(@Nonnull Player player) {
			MenuManager menuManager = Challenges.getInstance().getMenuManager();
			if (!menuManager.permissionToManageGUI()) return false;
			if (mayManageSettings(player)) return false;
			menuManager.playNoPermissionsEffect(player);
			return true;
		}

		private boolean mayManageSettings(@Nonnull Player player) {
			return player.hasPermission("challenges.manage");
		}

	}

}
