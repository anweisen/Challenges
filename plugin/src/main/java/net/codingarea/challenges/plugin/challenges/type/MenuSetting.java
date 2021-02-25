package net.codingarea.challenges.plugin.challenges.type;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.event.MenuClickEvent;
import net.codingarea.challenges.plugin.management.menu.Menu;
import net.codingarea.challenges.plugin.management.menu.MenuPosition;
import net.codingarea.challenges.plugin.management.menu.TitleManager;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class MenuSetting extends AbstractChallenge {

	private final Map<String, SubSetting> settings = new HashMap<>();
	private final List<Inventory> inventories = new ArrayList<>();
	private final String name;

	private boolean enabled = false;

	public MenuSetting(@Nonnull MenuType menu, @Nonnull String name) {
		super(menu);
		this.name = name;
	}

	protected final void generateInventories() {

		InventoryUtils.close(inventories);
		inventories.clear();

		int maxRowLength = 4;
		int page = 0;
		int index = 0;

		Collection<SubSetting> settings = this.settings.values();

		int pagesTotal = settings.size() / maxRowLength;
		if (settings.size() % maxRowLength != 0) pagesTotal++;

		for (SubSetting setting : settings) {

			if (index >= maxRowLength) {
				index = 0;
				createNewInventory(++page, pagesTotal);
			} else if (inventories.isEmpty()) {
				createNewInventory(page, pagesTotal);
			}

			int added = page * maxRowLength + index;
			int left = settings.size() - added;
			int fitOnThisPage = index + left;
			if (fitOnThisPage > 4) fitOnThisPage = 4;

			int[] slots = getSlots(fitOnThisPage);

			int displaySlot = slots[index];

			setting.setPage(page);
			setting.setSlot(displaySlot);
			setting.updateItems();

			index++;

		}

		InventoryUtils.setNavigationItems(inventories, Menu.NAVIGATION_SLOTS, false);

	}

	@Nonnull
	private Inventory createNewInventory(int page, int pagesAmount) {
		Inventory inventory = Bukkit.createInventory(MenuPosition.HOLDER, Menu.SIZE, TitleManager.getMenuSettingTitle(getType(), name, page, pagesAmount > 1));
		InventoryUtils.fillInventory(inventory, ItemBuilder.FILL_ITEM);
		inventories.add(inventory);
		return inventory;
	}

	@Nonnull
	private static int[] getSlots(int amount) {
		switch (amount) {
			default: return new int[0];
			case 1: return new int[] { 13 };
			case 2: return new int[] { 12, 14 };
			case 3: return new int[] { 11, 13, 15 };
			case 4: return new int[] { 10, 12, 14, 16 };
		}
	}

	protected final void registerSetting(@Nonnull String name, @Nonnull SubSetting setting) {
		if (name.equals("enabled")) throw new IllegalArgumentException();
		settings.put(name, setting);
	}

	protected final SubSetting getSetting(@Nonnull String name) {
		return settings.get(name);
	}

	@Override
	public final void handleClick(@Nonnull MenuClickEvent event) {
		if (event.isUpperItemClick()) {
			setEnabled(!enabled);
			updateItems();
			SoundSample.playEnablingSound(event.getPlayer(), enabled);
		} else if (enabled && !event.isRightClick()) {
			openMenu(event);
		} else {
			setEnabled(!enabled);
			updateItems();
			SoundSample.playEnablingSound(event.getPlayer(), enabled);
		}
	}

	private void setEnabled(boolean enabled) {
		if (this.enabled == enabled) return;
		this.enabled = enabled;
		if (enabled) onEnable();
		else onDisable();
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	protected void onEnable() {
	}

	protected void onDisable() {
	}

	private void openMenu(@Nonnull MenuClickEvent event) {
		MenuPosition position = Challenges.getInstance().getMenuManager().getPosition(event.getPlayer());
		Inventory inventory = event.getInventory();
		SoundSample.CLICK.play(event.getPlayer());
		open(event.getPlayer(), inventory, position, 0);
	}

	private void open(@Nonnull Player player, @Nonnull Inventory inventory, @Nonnull MenuPosition position, int page) {
		if (inventories.isEmpty()) generateInventories();
		if (inventories.isEmpty()) {
			SoundSample.BASS_OFF.play(player);
			return;
		}
		if (page >= inventories.size()) page = inventories.size() - 1;
		Inventory menu = inventories.get(page);
		Challenges.getInstance().getMenuManager().setPostion(player, new SettingMenuPosition(position, inventory, page));
		player.openInventory(menu);
	}

	@Nonnull
	@Override
	public final ItemStack getSettingsItem() {
		return enabled ? DefaultItem.customize() : DefaultItem.disabled();
	}

	@Override
	public void writeSettings(@Nonnull Document document) {
		document.set("enabled", enabled);
		for (Entry<String, SubSetting> entry : settings.entrySet()) {
			document.set(entry.getKey(), entry.getValue().getAsInt());
		}
	}

	@Override
	public void loadSettings(@Nonnull Document document) {
		enabled = document.getBoolean("enabled");
		for (Entry<String, SubSetting> entry : settings.entrySet()) {
			int value = document.getInt(entry.getKey());
			entry.getValue().setValue(value);
		}
	}

	public abstract class SubSetting {

		private int page = -1, slot = -1;

		private void setPage(int page) {
			this.page = page;
		}
		private void setSlot(int slot) {
			this.slot = slot;
		}

		public final void updateItems() {
			if (inventories.isEmpty()) return; // Nothing to update here
			if (page == -1 || slot == -1) return; // Invalid

			Inventory inventory = inventories.get(page);

			inventory.setItem(slot, getDisplayItem());
			inventory.setItem(slot + 9, getSettingsItem());
		}

		@Nonnull
		public abstract ItemStack getDisplayItem();

		@Nonnull
		public abstract ItemStack getSettingsItem();

		@Nonnull
		public abstract SubSetting setValue(int value);

		public abstract int getAsInt();

		public abstract boolean getAsBoolean();

		public abstract void handleClick(@Nonnull Player player, boolean shiftClick, boolean rightClick, boolean upperItem);

	}

	public class BooleanSubSetting extends SubSetting {

		private final ItemStack item;
		private boolean enabled;

		public BooleanSubSetting(@Nonnull ItemStack item) {
			this.item = item;
		}

		public BooleanSubSetting(@Nonnull ItemBuilder item) {
			this(item.build());
		}

		@Nonnull
		@Override
		public ItemStack getDisplayItem() {
			return item;
		}

		@Nonnull
		@Override
		public ItemStack getSettingsItem() {
			return DefaultItem.status(enabled);
		}

		@Override
		public int getAsInt() {
			return enabled ? 1 : 0;
		}

		@Override
		public boolean getAsBoolean() {
			return enabled;
		}

		@Nonnull
		@Override
		public BooleanSubSetting setValue(int value) {
			enabled = value > 0;
			updateItems();
			return this;
		}

		@Nonnull
		public BooleanSubSetting setValue(boolean enabled) {
			this.enabled = enabled;
			updateItems();
			return this;
		}

		@Override
		public void handleClick(@Nonnull Player player, boolean shiftClick, boolean rightClick, boolean upperItem) {

			enabled = !enabled;
			if (enabled) {
				SoundSample.BASS_ON.play(player);
			} else {
				SoundSample.BASS_OFF.play(player);
			}

			updateItems();

		}

	}

	public class NumberSubSetting extends SubSetting {

		private final ItemStack item;
		private int max = 64, min = 1;
		private int value;

		public NumberSubSetting(@Nonnull ItemStack item) {
			this.item = item;
		}

		public NumberSubSetting(@Nonnull ItemStack item, int max) {
			if (max <= min) throw new IllegalArgumentException("max <= min");
			this.item = item;
			this.max = max;
		}

		public NumberSubSetting(@Nonnull ItemStack item, int max, int min) {
			if (max <= min) throw new IllegalArgumentException("max <= min");
			this.max = max;
			this.min = min;
			this.item = item;
		}

		public NumberSubSetting(@Nonnull ItemBuilder item) {
			this(item.build());
		}

		public NumberSubSetting(@Nonnull ItemBuilder item, int max) {
			this(item.build(), max);
		}

		public NumberSubSetting(@Nonnull ItemBuilder item, int max, int min) {
			this(item.build(), max, min);
		}

		@Nonnull
		@Override
		public ItemStack getDisplayItem() {
			return item;
		}

		@Nonnull
		@Override
		public ItemStack getSettingsItem() {
			return DefaultItem.value(value);
		}

		@Nonnull
		@Override
		public NumberSubSetting setValue(int value) {
			this.value = value;
			updateItems();
			return this;
		}

		@Override
		public int getAsInt() {
			return value;
		}

		@Override
		public boolean getAsBoolean() {
			return value > 0;
		}

		@Override
		public void handleClick(@Nonnull Player player, boolean shiftClick, boolean rightClick, boolean upperItem) {

			int amount = shiftClick ? 10 : 1;
			if (rightClick) {
				value -= amount;
			} else {
				value += amount;
			}

			if (value > max)
				value = min;
			if (value < min)
				value = max;

			SoundSample.CLICK.play(player);
			updateItems();

		}

	}

	private class SettingMenuPosition implements MenuPosition {

		private final MenuPosition before;
		private final Inventory inventoryBefore;
		private final int page;

		public SettingMenuPosition(@Nonnull MenuPosition before, @Nonnull Inventory inventoryBefore, int page) {
			this.before = before;
			this.inventoryBefore = inventoryBefore;
			this.page = page;
		}

		@Override
		public void handleClick(@Nonnull Player player, int slot, @Nonnull Inventory inventory, @Nonnull InventoryClickEvent event) {

			if (slot == Menu.NAVIGATION_SLOTS[0]) {
				if (page == 0) {
					Challenges.getInstance().getMenuManager().setPostion(player, before);
					player.openInventory(inventoryBefore);
				} else {
					open(player, inventoryBefore, before, page - 1);
				}
				SoundSample.CLICK.play(player);
				return;
			} else if (slot == Menu.NAVIGATION_SLOTS[1]) {
				open(player, inventoryBefore, before, page + 1);
				SoundSample.CLICK.play(player);
				return;
			}

			for (SubSetting setting : settings.values()) {

				if (setting.page != page) continue;
				if (slot != setting.slot && slot != setting.slot + 9) continue;

				setting.handleClick(player, event.isShiftClick(), event.isRightClick(), slot == setting.slot);
				return;

			}

			SoundSample.CLICK.play(player);

		}

	}

}
