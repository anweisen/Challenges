package net.codingarea.challenges.plugin.management.menu;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.utils.animation.AnimatedInventory;
import net.codingarea.challenges.plugin.utils.animation.AnimationFrame;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class MenuManager {

	public static final int[] GUI_SLOTS = { 30, 32, 19, 25, 11, 15 };

	private final Map<MenuType, Menu> menus = new HashMap<>();
	private final Map<Player, MenuPosition> positions = new HashMap<>();
	private final boolean displayNewInFront;

	private TimerMenu timerMenu;
	private AnimatedInventory gui;

	private boolean generated = false;

	public MenuManager() {
		displayNewInFront = Challenges.getInstance().getConfigDocument().getBoolean("display-new-in-front");

		for (MenuType type : MenuType.values()) {
			if (!type.isUsable()) continue;
			menus.put(type, new Menu(type));
		}

		gui = new AnimatedInventory(TitleManager.getMainMenuTitle(), 5*9, MenuPosition.HOLDER)
				.setFrameSound(SoundSample.CLICK).setEndSound(SoundSample.OPEN);
		gui.addFrame(new AnimationFrame(5*9).fill(ItemBuilder.FILL_ITEM));
		gui.cloneAndAdd(0).setItem(39, ItemBuilder.FILL_ITEM_2).setItem(41, ItemBuilder.FILL_ITEM_2);
		gui.cloneAndAdd(1).setItem(38, ItemBuilder.FILL_ITEM_2).setItem(42, ItemBuilder.FILL_ITEM_2);
		gui.cloneAndAdd(2).setItem(37, ItemBuilder.FILL_ITEM_2).setItem(43, ItemBuilder.FILL_ITEM_2);
		gui.cloneAndAdd(3).setItem(28, ItemBuilder.FILL_ITEM_2).setItem(34, ItemBuilder.FILL_ITEM_2);
		gui.cloneAndAdd(4).setItem(27, ItemBuilder.FILL_ITEM_2).setItem(35, ItemBuilder.FILL_ITEM_2);
		gui.cloneAndAdd(5).setItem(18, ItemBuilder.FILL_ITEM_2).setItem(26, ItemBuilder.FILL_ITEM_2);
		gui.cloneAndAdd(6).setItem(9, ItemBuilder.FILL_ITEM_2).setItem(17, ItemBuilder.FILL_ITEM_2);
		gui.cloneAndAdd(7).setItem(10, ItemBuilder.FILL_ITEM_2).setItem(16, ItemBuilder.FILL_ITEM_2);
		gui.cloneAndAdd(8).setItem(1, ItemBuilder.FILL_ITEM_2).setItem(7, ItemBuilder.FILL_ITEM_2);
		gui.cloneAndAdd(9).setItem(2, ItemBuilder.FILL_ITEM_2).setItem(6, ItemBuilder.FILL_ITEM_2);

		gui.cloneAndAdd(10).setItem(GUI_SLOTS[MenuType.TIMER.ordinal()], new ItemBuilder(Material.CLOCK).setName("§6Timer").hideAttributes())
				.setItem(GUI_SLOTS[MenuType.GOAL.ordinal()], new ItemBuilder(Material.COMPASS).setName("§5Goal").hideAttributes());
		gui.cloneAndAdd(11).setItem(GUI_SLOTS[MenuType.DAMAGE.ordinal()], new ItemBuilder(Material.IRON_SWORD).setName("§7Damage").hideAttributes())
				.setItem(GUI_SLOTS[MenuType.ITEMS_BLOCKS.ordinal()], new ItemBuilder(Material.STICK).setName("§4Blocks & Items").hideAttributes());
		gui.cloneAndAdd(12).setItem(GUI_SLOTS[MenuType.CHALLENGES.ordinal()], new ItemBuilder(Material.BOOK).setName("§cChallenges").hideAttributes())
				.setItem(GUI_SLOTS[MenuType.SETTINGS.ordinal()], new ItemBuilder(Material.COMPARATOR).setName("§eSettings").hideAttributes());
	}

	public void generateMenus() {
		menus.values().forEach(Menu::resetChallengesCache);
		for (IChallenge challenge : Challenges.getInstance().getChallengeManager().getChallenges()) {
			MenuType type = challenge.getType();
			Menu menu = menus.get(type);
			if (menu == null) continue; // This should only happen if !type.isUsable()
			menu.addChallengeCache(challenge);
		}
		menus.values().forEach(Menu::generateInventories);

		timerMenu = new TimerMenu();

		generated = true;
	}

	public void updateTimerMenu() {
		if (timerMenu != null)
			timerMenu.updateInventories();
	}

	public void openGUI(@Nonnull Player player) {
		SoundSample.PLOP.play(player);
		setPostion(player, new MainMenuPosition());
		gui.open(player, Challenges.getInstance());
	}

	public void openGUIInstantly(@Nonnull Player player) {
		setPostion(player, new MainMenuPosition());
		gui.openNotAnimated(player, true);
	}

	/**
	 * @return If the specified menu page could be opened.
	 *         The menu may not be opened, when there are no challenges registered to that menu or the languages are not loaded
	 */
	public boolean openMenu(@Nonnull Player player, @Nonnull MenuType type, int page) {
		if (!generated) {
			SoundSample.BASS_OFF.play(player);
			player.sendMessage(Prefix.CHALLENGES + "§cCould not open gui, languages are not loaded");
			player.sendMessage(Prefix.CHALLENGES + "§cIs the plugin set up correctly?");
			return false;
		}

		if (type == MenuType.TIMER) {
			timerMenu.open(player, page);
		} else {
			Menu menu = getMenu(type);
			if (menu.getInventories().isEmpty()) return false;
			menu.open(player, page);
		}
		return true;
	}

	public void close() {
		menus.values().forEach(menu -> InventoryUtils.close(menu.getInventories()));
		if (timerMenu != null)
			InventoryUtils.close(timerMenu.getInventories());
	}

	@Nonnull
	public Menu getMenu(@Nonnull MenuType type) {
		return menus.get(type);
	}

	@Nullable
	public MenuPosition getPosition(@Nonnull Player player) {
		return positions.get(player);
	}

	public void setPostion(@Nonnull Player player, @Nullable MenuPosition position) {
		if (position == null) positions.remove(player);
		else positions.put(player, position);
	}

	public boolean isDisplayNewInFront() {
		return displayNewInFront;
	}

	private class MainMenuPosition implements MenuPosition {

		@Override
		public void handleClick(@Nonnull Player player, int slot, @Nonnull Inventory inventory, @Nonnull InventoryClickEvent event) {

			for (int i = 0; i < GUI_SLOTS.length; i++) {
				int current = GUI_SLOTS[i];
				if (current == slot) {
					MenuType type = MenuType.values()[i];
					if (openMenu(player, type, 0))
						SoundSample.CLICK.play(player);
					return;
				}
			}

			SoundSample.CLICK.play(player);

		}

	}

}
