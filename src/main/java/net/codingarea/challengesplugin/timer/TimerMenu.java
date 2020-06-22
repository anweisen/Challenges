package net.codingarea.challengesplugin.timer;

import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.timer.ChallengeTimer.TimerMode;
import net.codingarea.challengesplugin.utils.AnimationUtil.AnimationSound;
import net.codingarea.challengesplugin.utils.Utils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-08-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public class TimerMenu {

	private ChallengeTimer timer;

	@Getter private TimerMenuItemManager itemManager;

	private Inventory mainPage,
					  timePage;

	private byte startSlot = 21;
	private byte modeSlot = 23;
	private byte[] hours = { 11, 20, 29 };
	private byte[] minutes = { 13, 22, 31 };
	private byte[] seconds = { 15, 24, 33 };

	public TimerMenu(ChallengeTimer timer) {

		this.timer = timer;

		itemManager = new TimerMenuItemManager();

		createInventory();
		generateInventory();
		updateInventories();

	}

	public void createInventory() {
		mainPage = Bukkit.createInventory(null, 5*9, timer.getPlugin().getStringManager().TIMER_TITLE);
		timePage = Bukkit.createInventory(null, 5*9, timer.getPlugin().getStringManager().TIMER_TITLE);
	}

	public void generateInventory() {

		setDefaults(mainPage);
		mainPage.setItem(36, timer.getPlugin().getItemManager().getBackMainMenuItem());
		mainPage.setItem(44, timer.getPlugin().getItemManager().getNextPageItem());

		setDefaults(timePage);
		timePage.setItem(36, timer.getPlugin().getItemManager().getBackPageItem());

	}

	public void updateInventories() {
		updateMainMenu();
		updateTimeMenu();
	}

	private void updateMainMenu() {
		mainPage.setItem(startSlot, timer.isPaused() ? itemManager.getTimerStoppedItem() : itemManager.getTimerStartedItem());
		mainPage.setItem(modeSlot, timer.getMode() == TimerMode.UP ? itemManager.getTimerModeUpItem() : itemManager.getTimerModeDownItem());
	}

	private void updateTimeMenu() {

		timePage.setItem(hours[0], getNavigationItem(true, Translation.HOURS));
		timePage.setItem(hours[2], getNavigationItem(false, Translation.HOURS));

		timePage.setItem(minutes[0], getNavigationItem(true, Translation.MINUTES));
		timePage.setItem(minutes[2], getNavigationItem(false, Translation.MINUTES));

		timePage.setItem(seconds[0], getNavigationItem(true, Translation.SECONDS));
		timePage.setItem(seconds[2], getNavigationItem(false, Translation.SECONDS));

		updateTime();

	}

	public void updateTime() {

		int seconds = timer.getSeconds();
		int minutes = seconds / 60;
		int hours = minutes / 60;

		seconds %= 60;
		minutes %= 60;

		timePage.setItem(this.hours[1], getTimeItem(hours, Translation.HOURS));
		timePage.setItem(this.minutes[1], getTimeItem(minutes, Translation.MINUTES));
		timePage.setItem(this.seconds[1], getTimeItem(seconds, Translation.SECONDS));

	}

	private void setDefaults(Inventory inventory) {

		byte[] blackSlots = { 1, 2, 6, 7, 9, 10, 16, 17, 27, 28, 34, 35, 37, 38, 42, 43 };

		Utils.fillInventory(inventory, ItemBuilder.FILL_ITEM, null);

		for (byte currentSlot : blackSlots) {
			inventory.setItem(currentSlot, ItemBuilder.FILL_ITEM_2);
		}

	}

	public void openMainMenu(Player player) {
		player.openInventory(mainPage);
	}

	public void openTimeMenu(Player player) {
		player.openInventory(timePage);
	}

	public void handleClick(InventoryClickEvent event) {

		if (event.getClickedInventory() == null) return;
		if (event.getCurrentItem() == null) return;
		if (!(event.getWhoClicked() instanceof Player)) return;
		if (!event.getView().getTitle().equals(timer.getPlugin().getStringManager().TIMER_TITLE)) return;

		Player player = (Player) event.getWhoClicked();

		if (event.getCurrentItem().isSimilar(ItemBuilder.FILL_ITEM)
				|| event.getCurrentItem().isSimilar(ItemBuilder.FILL_ITEM_2)) {
			AnimationSound.STANDARD_SOUND.play(player);
			return;
		}

		if (event.getSlot() == startSlot) {
			if (timer.isPaused()) {
				timer.resume(player);
			} else {
				timer.stopTimer(player, true);
			}
			updateMainMenu();
			return;
		} else if (event.getSlot() == modeSlot) {
			timer.setMode(timer.getMode().reverse(), player);
			updateMainMenu();
			return;
		}

		if (event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
			try {
				String owner = ((SkullMeta) event.getCurrentItem().getItemMeta()).getOwner();
				if (owner.equals("MHF_ArrowLeft")) {
					openMainMenu(player);
				} else if (owner.equals("MHF_ArrowRight")) {
					openTimeMenu(player);
				}
				AnimationSound.STANDARD_SOUND.play(player);
				return;
			} catch (Exception ignored) { }
		} else if (event.getCurrentItem().getType() == timer.getPlugin().getItemManager().getBackMainMenuItem().getType()) {
			timer.getPlugin().getMenuManager().getMainMenu().openLastFrame(player, true);
			return;
		}

		if (event.getCurrentItem().getType() == Material.STONE_BUTTON) {
			if (event.getSlot() == this.seconds[2]) {
				addSeconds(-(1), event);
			} else if (event.getSlot() == minutes[2]) {
				addSeconds(-(60), event);
			} else if (event.getSlot() == hours[2]) {
				addSeconds(-(60*60), event);
			}
			updateTime();
			AnimationSound.PLOP_SOUND.play(player);
		} else if (event.getCurrentItem().getType() == Material.DARK_OAK_BUTTON) {
			if (event.getSlot() == this.seconds[0]) {
				addSeconds(1, event);
			} else if (event.getSlot() == minutes[0]) {
				addSeconds(60, event);
			} else if (event.getSlot() == hours[0]) {
				addSeconds(60*60, event);
			}
			updateTime();
			AnimationSound.PLOP_SOUND.play(player);
		} else if (event.getCurrentItem().getType() == Material.CLOCK) {
			timer.setMaxSeconds(0);
			updateTime();
			AnimationSound.ON_SOUND.play(player);
		}

	}

	private void addSeconds(int seconds, InventoryClickEvent event) {

		if (event.isShiftClick()) seconds *= 10;

		if (timer.getMode() == TimerMode.UP) {
			timer.addSeconds(seconds);
		} else {
			timer.addMaxSeconds(seconds);
		}

		timer.sendActionbar();

	}

	public static ItemStack getTimeItem(int value, Translation translation) {

		int amount = value;

		if (amount > 64) {
			amount = 64;
		} else if (amount < 1) {
			amount = 1;
		}

		String[] lore = {
			" ",
			"§7§o[Click] §8» §cReset §7time",
			" "
		};

		return new ItemBuilder(Material.CLOCK,
				"§7" + Utils.getEnumName(translation.get().substring(2)) + ": §e" + value,
				lore).hideAttributes().setAmount(amount).build();

	}

	public ItemStack getNavigationItem(boolean up, Translation translation) {

		Material material = up ? Material.DARK_OAK_BUTTON : Material.STONE_BUTTON;
		String[] lore = {
			"§7§o[Click] §8» §" + (up ? "a+" : "c-") + "1 " + translation.get().substring(0, translation.get().length() - 1),
			"§7§o[Shift-Click] §8» §" + (up ? "a+" : "c-") + "10 " + translation.get(),
			" "
		};

		return new ItemBuilder(material, " ", lore).build();

	}

}
