package net.codingarea.challenges.plugin.spigot.command;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuPosition;
import net.codingarea.challenges.plugin.management.menu.MenuPosition.EmptyMenuPosition;
import net.codingarea.challenges.plugin.management.menu.InventoryTitleManager;
import net.codingarea.challenges.plugin.management.stats.LeaderboardInfo;
import net.codingarea.challenges.plugin.management.stats.PlayerStats;
import net.codingarea.challenges.plugin.management.stats.Statistic;
import net.codingarea.challenges.plugin.utils.animation.AnimatedInventory;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.SkullBuilder;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.UUID;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class StatsCommand implements PlayerCommand {

	@Override
	public void onCommand(@Nonnull Player player, @Nonnull String[] args) {
		if (!Challenges.getInstance().getStatsManager().isEnabled()) {
			player.sendMessage(Prefix.CHALLENGES + Message.forName("feature-disabled").asString());
			SoundSample.BASS_OFF.play(player);
			return;
		}

		switch (args.length) {
			case 0:
				Message.forName("fetching-data").send(player, Prefix.CHALLENGES);
				handleCommand(player);
				break;
			case 1:
				Message.forName("fetching-data").send(player, Prefix.CHALLENGES);
				handleCommand(player, args[0]);
				break;
			default:
				Message.forName("syntax").send(player, Prefix.CHALLENGES, "stats [player]");
		}
	}

	private void handleCommand(@Nonnull Player player) {
		Challenges.getInstance().runAsync(() -> {
			open(player, player.getUniqueId(), player.getName());
		});
	}
	private void handleCommand(@Nonnull Player player, @Nonnull String name) {
		Challenges.getInstance().runAsync(() -> {
			try {
				UUID uuid = Utils.fetchUUID(name);
				open(player, uuid, name);
			} catch (IOException ex) {
				player.sendMessage(Prefix.CHALLENGES + "§7Something went wrong");
			}
		});
	}

	private void open(@Nonnull Player player, @Nonnull UUID uuid, @Nonnull String name) {

		AnimatedInventory inventory = new AnimatedInventory(InventoryTitleManager.getStatsTitle(name), 5*9, MenuPosition.HOLDER)
				.setEndSound(SoundSample.OPEN).setFrameSound(SoundSample.CLICK);
		inventory.createAndAdd().fill(ItemBuilder.FILL_ITEM);
		inventory.cloneLastAndAdd().setAccent(39, 41);
		inventory.cloneLastAndAdd().setAccent(38, 42);
		inventory.cloneLastAndAdd().setAccent(37, 43);
		inventory.cloneLastAndAdd().setAccent(28, 34);
		inventory.cloneLastAndAdd().setAccent(27, 35);
		inventory.cloneLastAndAdd().setItem(13, new SkullBuilder(uuid, Message.forName("stats-of").asString(name)).build());

		PlayerStats stats = Challenges.getInstance().getStatsManager().getStats(uuid);
		LeaderboardInfo info = Challenges.getInstance().getStatsManager().getLeaderboardInfo(uuid);
		createInventory(stats, info, inventory, 19, 20, 21, 22, 23, 24, 25, 29, 30, 31, 32, 33);

		Bukkit.getScheduler().callSyncMethod(Challenges.getInstance(), () -> {
			MenuPosition.set(player, new EmptyMenuPosition());
			inventory.open(player, Challenges.getInstance());
			return null;
		});
	}

	private void createInventory(@Nonnull PlayerStats stats, @Nonnull LeaderboardInfo info, @Nonnull AnimatedInventory inventory, @Nonnull int... slots) {
		int i = 0;
		for (Statistic statistic : Statistic.values()) {
			double value = stats.getStatisticValue(statistic);
			String format = statistic.formatChat(value);

			Message message = Message.forName("stat-" + statistic.name().toLowerCase().replace('_', '-'));
			ItemBuilder item = new ItemBuilder(getMaterialForStatistic(statistic), message.asString()).setLore(Message.forName("stats-display").asArray(format, info.getPlace(statistic))).hideAttributes();
			inventory.cloneLastAndAdd().setItem(slots[i], item);

			i++;
		}
	}

	private Material getMaterialForStatistic(@Nonnull Statistic statistic) {
		switch (statistic) {
			default:                return Material.PAPER;
			case DEATHS:            return Material.STONE_SHOVEL;
			case BLOCKS_MINED:      return Material.GOLDEN_PICKAXE;
			case BLOCKS_PLACED:     return Material.DIRT;
			case DAMAGE_DEALT:      return Material.STONE_SWORD;
			case DAMAGE_TAKEN:      return Material.LEATHER_CHESTPLATE;
			case ENTITY_KILLS:      return Material.IRON_SWORD;
			case DRAGON_KILLED:     return Material.DRAGON_EGG;
			case BLOCKS_TRAVELED:   return Material.MINECART;
			case CHALLENGES_PLAYED: return Material.GOLD_INGOT;
		}
	}

}
