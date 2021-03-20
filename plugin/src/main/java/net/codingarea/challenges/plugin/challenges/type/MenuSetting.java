package net.codingarea.challenges.plugin.challenges.type;

import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.management.menu.InventoryTitleManager;
import net.codingarea.challenges.plugin.management.menu.Menu;
import net.codingarea.challenges.plugin.management.menu.MenuPosition;
import net.codingarea.challenges.plugin.management.menu.MenuPosition.EmptyMenuPosition;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.info.ChallengeMenuClickInfo;
import net.codingarea.challenges.plugin.management.menu.info.MenuClickInfo;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class MenuSetting extends Setting {

	private final Map<String, SubSetting> settings = new LinkedHashMap<>();
	private final List<Inventory> inventories = new ArrayList<>();
	private final String menuTitle;

	public MenuSetting(@Nonnull MenuType menu, @Nonnull String menuTitle) {
		super(menu);
		this.menuTitle = menuTitle;
	}

	protected final void generateInventories() {

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
		Inventory inventory = Bukkit.createInventory(MenuPosition.HOLDER, Menu.SIZE, InventoryTitleManager.getMenuSettingTitle(getType(), menuTitle, page, pagesAmount > 1));
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
		Challenges.getInstance().registerListener(setting);
	}

	protected final SubSetting getSetting(@Nonnull String name) {
		return settings.get(name);
	}

	@Override
	public void handleClick(@Nonnull ChallengeMenuClickInfo event) {
		if (event.isUpperItemClick()) {
			super.handleClick(event);
		} else if (isEnabled() && !event.isRightClick()) {
			openMenu(event);
		} else {
			super.handleClick(event);
		}
	}

	private void openMenu(@Nonnull ChallengeMenuClickInfo event) {
		MenuPosition position = Challenges.getInstance().getMenuManager().getPosition(event.getPlayer());
		if (position == null) position = new EmptyMenuPosition();
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
		MenuPosition.set(player, new SettingMenuPosition(position, inventory, page));
		player.openInventory(menu);
	}

	@Nonnull
	@Override
	public final ItemBuilder createSettingsItem() {
		return isEnabled() ? DefaultItem.customize() : DefaultItem.disabled();
	}

	@Override
	public void writeSettings(@Nonnull Document document) {
		document.set("enabled", isEnabled());
		for (Entry<String, SubSetting> entry : settings.entrySet()) {
			document.set(entry.getKey(), entry.getValue().getAsInt());
		}
	}

	@Override
	public void loadSettings(@Nonnull Document document) {
		setEnabled(document.getBoolean("enabled"));
		for (Entry<String, SubSetting> entry : settings.entrySet()) {
			int value = document.getInt(entry.getKey());
			entry.getValue().setValue(value);
		}
	}

	public abstract class SubSetting implements Listener {

		protected final Challenges plugin = Challenges.getInstance();

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

			inventory.setItem(slot, getDisplayItem().build());
			inventory.setItem(slot + 9, getSettingsItem().build());
		}

		@Nonnull
		public abstract ItemBuilder getDisplayItem();

		@Nonnull
		public abstract ItemBuilder getSettingsItem();

		@Nonnull
		public abstract SubSetting setValue(int value);

		public boolean isEnabled() {
			return MenuSetting.this.isEnabled() && getAsBoolean();
		}

		public abstract int getAsInt();

		public abstract boolean getAsBoolean();

		public abstract void handleClick(@Nonnull ChallengeMenuClickInfo info);

	}

	public class BooleanSubSetting extends SubSetting {

		private final Supplier<ItemBuilder> item;
		private boolean enabled;

		public BooleanSubSetting(@Nonnull Supplier<ItemBuilder> item) {
			this(item, false);
		}

		public BooleanSubSetting(@Nonnull Supplier<ItemBuilder> item, boolean enabledByDefault) {
			this.item = item;
			setEnabled(enabledByDefault);
		}

		@Nonnull
		@Override
		public ItemBuilder getDisplayItem() {
			return item.get();
		}

		@Nonnull
		@Override
		public ItemBuilder getSettingsItem() {
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
			return setValue(value > 0);
		}

		@Nonnull
		public BooleanSubSetting setValue(boolean enabled) {
			if (this.enabled == enabled) return this;
			this.enabled = enabled;
			if (enabled) onEnable();
			else onDisable();
			updateItems();
			return this;
		}

		@Override
		public final void handleClick(@Nonnull ChallengeMenuClickInfo info) {
			setValue(!enabled);
			SoundSample.playEnablingSound(info.getPlayer(), enabled);
		}

		public void onEnable() {
		}

		public void onDisable() {
		}

	}

	public class NumberSubSetting extends SubSetting {

		private final Supplier<ItemBuilder> item;
		private int max = 64, min = 1;
		private int value;

		public NumberSubSetting(@Nonnull Supplier<ItemBuilder> item) {
			this.item = item;
		}

		public NumberSubSetting(@Nonnull Supplier<ItemBuilder> item, int max) {
			if (max <= min) throw new IllegalArgumentException("max <= min");
			this.item = item;
			this.max = max;
		}

		public NumberSubSetting(@Nonnull Supplier<ItemBuilder> item, int max, int min) {
			if (max <= min) throw new IllegalArgumentException("max <= min");
			this.max = max;
			this.min = min;
			this.item = item;
		}

		@Nonnull
		@Override
		public ItemBuilder getDisplayItem() {
			return item.get();
		}

		@Nonnull
		@Override
		public ItemBuilder getSettingsItem() {
			return DefaultItem.value(value);
		}

		@Nonnull
		@Override
		public NumberSubSetting setValue(int value) {
			this.value = value;
			updateItems();
			return this;
		}

		public int getValue() {
			return value;
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
		public void handleClick(@Nonnull ChallengeMenuClickInfo info) {
			int amount = info.isShiftClick() ? 10 : 1;
			int newValue = value;
			if (info.isRightClick()) {
				newValue -= amount;
			} else {
				newValue += amount;
			}

			if (newValue > max)
				newValue = min;
			if (newValue < min)
				newValue = max;

			setValue(newValue);
			SoundSample.CLICK.play(info.getPlayer());
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
		public void handleClick(@Nonnull MenuClickInfo info) {

			if (info.getSlot() == Menu.NAVIGATION_SLOTS[0]) {
				if (page == 0) {
					Challenges.getInstance().getMenuManager().setPostion(info.getPlayer(), before);
					info.getPlayer().openInventory(inventoryBefore);
				} else {
					open(info.getPlayer(), inventoryBefore, before, page - 1);
				}
				SoundSample.CLICK.play(info.getPlayer());
				return;
			} else if (info.getSlot() == Menu.NAVIGATION_SLOTS[1]) {
				open(info.getPlayer(), inventoryBefore, before, page + 1);
				SoundSample.CLICK.play(info.getPlayer());
				return;
			}

			for (SubSetting setting : settings.values()) {
				if (setting.page != page) continue;
				if (info.getSlot() != setting.slot && info.getSlot() != setting.slot + 9) continue;

				setting.handleClick(new ChallengeMenuClickInfo(info, info.getSlot() == setting.slot));
				return;
			}

			SoundSample.CLICK.play(info.getPlayer());

		}

	}

}
