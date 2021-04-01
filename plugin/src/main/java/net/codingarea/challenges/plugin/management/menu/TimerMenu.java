package net.codingarea.challenges.plugin.management.menu;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.info.MenuClickInfo;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.PotionBuilder;
import net.codingarea.challenges.plugin.utils.item.MaterialWrapper;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
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
public final class TimerMenu {

	public static final int SIZE = 5*9;
	public static final int[] NAVIGATION_SLOTS = { 36, 44 };
	public static final int START_SLOT = 21;
	public static final int MODE_SLOT = 23;
	public static final int[] HOUR_SLOTS = { 11, 20, 29 };
	public static final int[] MINUTE_SLOTS = { 13, 22, 31 };
	public static final int[] SECOND_SLOTS = { 15, 24, 33 };

	private final List<Inventory> inventories = new ArrayList<>();

	public TimerMenu() {
		createNewInventory(0);
		createNewInventory(1);
		setNavigation();
		updateInventories();
	}

	public void updateInventories() {
		updateFirstPage();
		updateSecondPage();
	}

	private void updateFirstPage() {
		Inventory inventory = inventories.get(0);
		inventory.setItem(START_SLOT, Challenges.getInstance().getChallengeTimer().isStarted() ?
				new ItemBuilder(Material.LIME_DYE).setName(Message.forName("timer-is-running")).build() :
				new ItemBuilder(MaterialWrapper.RED_DYE).setName(Message.forName("timer-is-paused")).build());
		inventory.setItem(MODE_SLOT, Challenges.getInstance().getChallengeTimer().isCountingUp() ?
				new PotionBuilder(Material.TIPPED_ARROW).setColor(Color.LIME).setName(Message.forName("timer-counting-up")).hideAttributes().build() :
				new PotionBuilder(Material.TIPPED_ARROW).setColor(Color.RED).setName(Message.forName("timer-counting-down")).hideAttributes().build());
	}

	private void updateSecondPage() {
		setTimeNavigation(HOUR_SLOTS, Message.forName("hour"),Message.forName("hours"));
		setTimeNavigation(MINUTE_SLOTS, Message.forName("minute"), Message.forName("minutes"));
		setTimeNavigation(SECOND_SLOTS, Message.forName("second"), Message.forName("seconds"));

		long seconds = Challenges.getInstance().getChallengeTimer().getTime();
		long minutes = seconds / 60;
		long hours = minutes / 60;
		seconds %= 60;
		minutes %= 60;

		Inventory inventory = inventories.get(1);
		inventory.setItem(HOUR_SLOTS[1], getTimeItem(hours, Message.forName("hour"),Message.forName("hours")));
		inventory.setItem(MINUTE_SLOTS[1], getTimeItem(minutes, Message.forName("minute"), Message.forName("minutes")));
		inventory.setItem(SECOND_SLOTS[1], getTimeItem(seconds, Message.forName("second"), Message.forName("seconds")));
	}
	private void setTimeNavigation(@Nonnull int[] slots, @Nonnull Message singular, @Nonnull Message plural) {
		Inventory inventory = inventories.get(1);
		inventory.setItem(slots[0], getNavigationItem(true, singular, plural));
		inventory.setItem(slots[2], getNavigationItem(false, singular, plural));
	}

	@Nonnull
	private ItemStack getNavigationItem(boolean up, @Nonnull Message singular, @Nonnull Message plural) {
		Material material = up ? Material.DARK_OAK_BUTTON : Material.STONE_BUTTON;
		String[] lore = {
				"§7§o[Click] §8» §" + (up ? "a+" : "c-") + "1 " + singular,
				"§7§o[Shift-Click] §8» §" + (up ? "a+" : "c-") + "10 " + plural,
				" "
		};
		return new ItemBuilder(material).setName(" ").setLore(lore).hideAttributes().build();
	}
	@Nonnull
	private ItemStack getTimeItem(long value, @Nonnull Message singular, @Nonnull Message plural) {
		long amount = value < 1 ? 1 : Math.min(value, 64);
		String[] lore = {
			" ",
			"§7§o[Click] §8» §cReset §7timer",
			" "
		};
		return new ItemBuilder(Material.CLOCK).setName("§8» " + (value == 1 ? singular : plural) + ": §e" + value).setLore(lore).hideAttributes().setAmount((int) amount).build();
	}

	@Nonnull
	private Inventory createNewInventory(int page) {
		Inventory inventory = Bukkit.createInventory(MenuPosition.HOLDER, SIZE, InventoryTitleManager.getTitle(MenuType.TIMER, page));
		InventoryUtils.fillInventory(inventory, ItemBuilder.FILL_ITEM);
		for (int i : new int[] { 1, 2, 6, 7, 9, 10, 16, 17, 27, 28, 34, 35, 37, 38, 39, 41, 42, 43 }) {
			inventory.setItem(i, ItemBuilder.FILL_ITEM_2);
		}
		inventories.add(inventory);
		return inventory;
	}

	private void setNavigation() {
		InventoryUtils.setNavigationItemsToInventory(inventories, NAVIGATION_SLOTS);
	}

	public void open(@Nonnull Player player, int page) {
		if (inventories.isEmpty()) return; // This should normally never happen
		if (page >= inventories.size()) page = inventories.size() - 1;
		Inventory inventory = inventories.get(page);
		Challenges.getInstance().getMenuManager().setPostion(player, new TimerMenuPosition(page));
		player.openInventory(inventory);
	}

	@Nonnull
	List<Inventory> getInventories() {
		return inventories;
	}

	private class TimerMenuPosition implements MenuPosition {

		private final int page;

		public TimerMenuPosition(@Nonnegative int page) {
			this.page = page;
		}

		@Override
		public void handleClick(@Nonnull MenuClickInfo info) {

			if (info.getSlot() == NAVIGATION_SLOTS[0]) {
				SoundSample.CLICK.play(info.getPlayer());
				if (page == 0) {
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

			if (page == 0) {
				if (info.getSlot() == START_SLOT) {
					if (Challenges.getInstance().getChallengeTimer().isStarted()) {
						Challenges.getInstance().getChallengeTimer().pause(true);
					} else {
						Challenges.getInstance().getChallengeTimer().resume();
					}
					return;
				} else if (info.getSlot() == MODE_SLOT) {
					Challenges.getInstance().getChallengeTimer().setCountingUp(!Challenges.getInstance().getChallengeTimer().isCountingUp());
					return;
				}
			} else if (page == 1) {
				for (int[] slots : new int[][] {HOUR_SLOTS, MINUTE_SLOTS, SECOND_SLOTS}) {
					for (int i = 0; i < 3; i++) {

						if (info.getSlot() != slots[i]) continue;

						if (i == 1) {
							Challenges.getInstance().getChallengeTimer().reset();
							SoundSample.BASS_OFF.play(info.getPlayer());
							updateInventories();
							Challenges.getInstance().getChallengeTimer().updateActionbar();
							return;
						}

						int amount = (slots == HOUR_SLOTS ? 60*60 : (slots == MINUTE_SLOTS ? 60 : 1));
						if (info.isShiftClick()) amount *= 10;

						boolean plus = i == 0;

						Challenges.getInstance().getChallengeTimer().addSeconds(plus ? +amount : -amount);
						updateInventories();

						SoundSample.PLOP.play(info.getPlayer());
						return;
					}
				}
			}

			SoundSample.CLICK.play(info.getPlayer());

		}

	}

}
