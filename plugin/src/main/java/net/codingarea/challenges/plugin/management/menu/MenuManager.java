package net.codingarea.challenges.plugin.management.menu;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.language.loader.LanguageLoader;
import net.codingarea.challenges.plugin.management.menu.info.MenuClickInfo;
import net.codingarea.challenges.plugin.utils.animation.AnimatedInventory;
import net.codingarea.challenges.plugin.utils.animation.AnimationFrame;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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

	private final Map<MenuType, SettingsMenu> menus = new HashMap<>();
	private final Map<Player, MenuPosition> positions = new HashMap<>();
	private final AnimatedInventory gui;
	private final boolean displayNewInFront;
	private final boolean permissionToManageGUI;

	private TimerMenu timerMenu;
	private boolean generated = false;

	public MenuManager() {
		ChallengeAPI.subscribeLoader(LanguageLoader.class, this::generateMenus);
		displayNewInFront = Challenges.getInstance().getConfigDocument().getBoolean("display-new-in-front");
		permissionToManageGUI = Challenges.getInstance().getConfigDocument().getBoolean("manage-settings-permission");

		for (MenuType type : MenuType.values()) {
			if (!type.isUsable()) continue;
			menus.put(type, new SettingsMenu(type));
		}

		gui = new AnimatedInventory(InventoryTitleManager.getMainMenuTitle(), 5*9, MenuPosition.HOLDER);
		gui.addFrame(new AnimationFrame(5*9).fill(ItemBuilder.FILL_ITEM));
		gui.cloneLastAndAdd().setItem(39, ItemBuilder.FILL_ITEM_2).setItem(41, ItemBuilder.FILL_ITEM_2);
		gui.cloneLastAndAdd().setItem(38, ItemBuilder.FILL_ITEM_2).setItem(42, ItemBuilder.FILL_ITEM_2);
		gui.cloneLastAndAdd().setItem(37, ItemBuilder.FILL_ITEM_2).setItem(43, ItemBuilder.FILL_ITEM_2);
		gui.cloneLastAndAdd().setItem(28, ItemBuilder.FILL_ITEM_2).setItem(34, ItemBuilder.FILL_ITEM_2);
		gui.cloneLastAndAdd().setItem(27, ItemBuilder.FILL_ITEM_2).setItem(35, ItemBuilder.FILL_ITEM_2);
		gui.cloneLastAndAdd().setItem(18, ItemBuilder.FILL_ITEM_2).setItem(26, ItemBuilder.FILL_ITEM_2);
		gui.cloneLastAndAdd().setItem(9, ItemBuilder.FILL_ITEM_2).setItem(17, ItemBuilder.FILL_ITEM_2);
		gui.cloneLastAndAdd().setItem(10, ItemBuilder.FILL_ITEM_2).setItem(16, ItemBuilder.FILL_ITEM_2);
		gui.cloneLastAndAdd().setItem(1, ItemBuilder.FILL_ITEM_2).setItem(7, ItemBuilder.FILL_ITEM_2);
		gui.cloneLastAndAdd().setItem(2, ItemBuilder.FILL_ITEM_2).setItem(6, ItemBuilder.FILL_ITEM_2);

		gui.cloneLastAndAdd().setItem(GUI_SLOTS[MenuType.TIMER.ordinal()],          new ItemBuilder(Material.CLOCK).name("§8» §6Timer").hideAttributes())
							 .setItem(GUI_SLOTS[MenuType.GOAL.ordinal()],           new ItemBuilder(Material.COMPASS).name("§8» §5Goal").hideAttributes());
		gui.cloneLastAndAdd().setItem(GUI_SLOTS[MenuType.DAMAGE.ordinal()],         new ItemBuilder(Material.IRON_SWORD).name("§8» §7Damage").hideAttributes())
							 .setItem(GUI_SLOTS[MenuType.ITEMS_BLOCKS.ordinal()],   new ItemBuilder(Material.STICK).name("§8» §4Blocks & Items").hideAttributes());
		gui.cloneLastAndAdd().setItem(GUI_SLOTS[MenuType.CHALLENGES.ordinal()],     new ItemBuilder(Material.BOOK).name("§8» §cChallenges").hideAttributes())
							 .setItem(GUI_SLOTS[MenuType.SETTINGS.ordinal()],       new ItemBuilder(Material.COMPARATOR).name("§8» §eSettings").hideAttributes());
	}

	public void generateMenus() {
		menus.values().forEach(SettingsMenu::resetChallengesCache);
		for (IChallenge challenge : Challenges.getInstance().getChallengeManager().getChallenges()) {
			MenuType type = challenge.getType();
			SettingsMenu menu = menus.get(type);
			if (menu == null) continue; // Menu is disabled
			menu.addChallengeCache(challenge);
		}
		menus.values().forEach(SettingsMenu::generateInventories);

		timerMenu = new TimerMenu();

		generated = true;
	}

	public void openGUI(@Nonnull Player player) {
		SoundSample.PLOP.play(player);
		MenuPosition.set(player, new MainMenuPosition());
		gui.open(player, Challenges.getInstance());
	}

	public void openGUIInstantly(@Nonnull Player player) {
		MenuPosition.set(player, new MainMenuPosition());
		gui.openNotAnimated(player, true, Challenges.getInstance());
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
			SettingsMenu menu = getMenu(type);
			if (menu.getInventories().isEmpty()) return false;
			menu.open(player, page);
		}
		return true;
	}

	@Nonnull
	public SettingsMenu getMenu(@Nonnull MenuType type) {
		return menus.get(type);
	}

	@Nullable
	public MenuPosition getPosition(@Nonnull Player player) {
		return positions.get(player);
	}

	public synchronized void setPosition(@Nonnull Player player, @Nullable MenuPosition position) {
		if (position == null) positions.remove(player);
		else positions.put(player, position);
	}

	public boolean isDisplayNewInFront() {
		return displayNewInFront;
	}

	private class MainMenuPosition implements MenuPosition {

		@Override
		public void handleClick(@Nonnull MenuClickInfo info) {

			for (int i = 0; i < GUI_SLOTS.length; i++) {
				int current = GUI_SLOTS[i];
				if (current == info.getSlot()) {
					MenuType type = MenuType.values()[i];
					if (openMenu(info.getPlayer(), type, 0))
						SoundSample.CLICK.play(info.getPlayer());
					return;
				}
			}

			SoundSample.CLICK.play(info.getPlayer());

		}

	}

	public void playNoPermissionsEffect(@Nonnull Player player) {
		SoundSample.BASS_OFF.play(player);
		Message.forName("no-permission").send(player, Prefix.CHALLENGES);
	}

	public boolean permissionToManageGUI() {
		return permissionToManageGUI;
	}

}
