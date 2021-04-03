package net.codingarea.challenges.plugin.spigot.command;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.InventoryTitleManager;
import net.codingarea.challenges.plugin.management.menu.MenuPosition;
import net.codingarea.challenges.plugin.management.menu.MenuPosition.EmptyMenuPosition;
import net.codingarea.challenges.plugin.management.menu.MenuPosition.SlottedMenuPosition;
import net.codingarea.challenges.plugin.management.stats.PlayerStats;
import net.codingarea.challenges.plugin.management.stats.Statistic;
import net.codingarea.challenges.plugin.utils.animation.AnimatedInventory;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.SkullBuilder;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import net.codingarea.challenges.plugin.utils.misc.StatsHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class LeaderboardCommand implements PlayerCommand {

	protected static final int[] slots = StatsHelper.getSlots(1);
	protected static final int[] navigationSlots = { 45, 53 };
	protected static final AnimatedInventory loadingInventory;
	static {
		loadingInventory = new AnimatedInventory(InventoryTitleManager.getLeaderboardTitle(), 6*9, MenuPosition.HOLDER).setEndSound(null).setFrameSound(null);
		loadingInventory.createAndAdd().fill(ItemBuilder.FILL_ITEM).setItem(31, new ItemBuilder(Material.BARRIER, "§8» §cLoading.."));
	}

	@Override
	public void onCommand(@Nonnull Player player, @Nonnull String[] args) throws Exception {

		AnimatedInventory inventory = new AnimatedInventory(InventoryTitleManager.getLeaderboardTitle(), 4*9, MenuPosition.HOLDER);
		StatsHelper.setAccent(inventory, 2);
		SlottedMenuPosition position = new SlottedMenuPosition();
		for (int i = 0; i < Statistic.values().length; i++) {
			Statistic statistic = Statistic.values()[i];
			ItemBuilder item = new ItemBuilder(StatsHelper.getMaterial(statistic), "§8» " + StatsHelper.getNameMessage(statistic).asString());
			inventory.cloneLastAndAdd().setItem(slots[i], item.hideAttributes());
			position.setAction(slots[i], () -> openMenu(player, statistic, 0));
		}

		inventory.open(player, Challenges.getInstance());
		MenuPosition.set(player, position);

	}

	private void openMenu(@Nonnull Player player, @Nonnull Statistic statistic, int page) {
		loadingInventory.open(player, Challenges.getInstance());
		MenuPosition.set(player, new EmptyMenuPosition());
		Challenges.getInstance().runAsync(() -> openMenu0(player, statistic, page));
	}

	private void openMenu0(@Nonnull Player player, @Nonnull Statistic statistic, int page) {

		int[] slots = {
			10, 11, 12, 13, 14, 15, 16,
			19, 20, 21, 22, 23, 24, 25,
			28, 29, 30, 31, 32, 33, 34,
			37, 38, 39, 40, 41, 42, 43
		};

		AnimatedInventory inventory = new AnimatedInventory(InventoryTitleManager.getLeaderboardTitle(), 6*9, MenuPosition.HOLDER);
		inventory.createAndAdd().fill(ItemBuilder.FILL_ITEM);

		List<PlayerStats> leaderboard = Challenges.getInstance().getStatsManager().getLeaderboard(statistic);
		int pages = leaderboard.size() / slots.length;
		InventoryUtils.setNavigationItemsToFrame(inventory.cloneLastAndAdd(), navigationSlots, true, page, pages);

		SlottedMenuPosition position = new SlottedMenuPosition();

		for (int i = page * slots.length; i < leaderboard.size() && i < slots.length; i++) {
			PlayerStats stats = leaderboard.get(i);
			ItemBuilder item = new SkullBuilder(stats.getPlayerUUID(), stats.getPlayerName()).setName(Message.forName("stats-leaderboard-display")
					.asArray(stats.getPlayerName(), statistic.formatChat(stats.getStatisticValue(statistic)), StatsHelper.getNameMessage(statistic).asString(), i + 1));
			inventory.cloneLastAndAdd().setItem(slots[i], item.hideAttributes());

			position.setAction(slots[i], () -> {
				loadingInventory.open(player, Challenges.getInstance());
				player.performCommand("stats " + stats.getPlayerName());
			});
		}

		if (page == 0) position.setAction(navigationSlots[0], () -> player.performCommand("lb"));
		else position.setAction(navigationSlots[0], () -> openMenu(player, statistic, page - 1));
		if (inventory.getLastFrame().getItemType(navigationSlots[1]) == Material.PLAYER_HEAD)
			position.setAction(navigationSlots[1], () -> openMenu(player, statistic, page + 1));

		inventory.open(player, Challenges.getInstance());
		MenuPosition.set(player, position);

	}

}
