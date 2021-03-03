package net.codingarea.challenges.plugin.spigot.command;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuPosition;
import net.codingarea.challenges.plugin.management.menu.MenuPosition.EmptyMenuPosition;
import net.codingarea.challenges.plugin.management.menu.TitleManager;
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
			player.sendMessage(Prefix.CHALLENGES + Message.FEATURE_DISABLED.asString());
			return;
		}

		switch (args.length) {
			case 0:
				player.sendMessage(Prefix.CHALLENGES + Message.FETCHING_DATA.asString());
				handleCommand(player);
				break;
			case 1:
				player.sendMessage(Prefix.CHALLENGES + Message.FETCHING_DATA.asString());
				handleCommand(player, args[0]);
				break;
			default:
				player.sendMessage(Prefix.CHALLENGES + Message.SYNTAX.asString("stats [player]"));
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

		AnimatedInventory inventory = new AnimatedInventory(TitleManager.getStatsTitle(name), 5*9, MenuPosition.HOLDER)
				.setEndSound(SoundSample.OPEN).setFrameSound(SoundSample.CLICK);
		inventory.createAndAdd().fill(ItemBuilder.FILL_ITEM);
		inventory.cloneLastAndAdd().setAkzent(39, 41);
		inventory.cloneLastAndAdd().setAkzent(38, 42);
		inventory.cloneLastAndAdd().setAkzent(37, 43);
		inventory.cloneLastAndAdd().setAkzent(28, 34);
		inventory.cloneLastAndAdd().setAkzent(27, 35);
		inventory.cloneLastAndAdd().setItem(13, new SkullBuilder(name, Message.STATS_OF.asString(name)).build());

		PlayerStats stats = Challenges.getInstance().getStatsManager().getStats(uuid);
		createInventory(stats, inventory, 19, 20, 21, 22, 23, 24, 25, 29, 30, 31, 32, 33);

		Bukkit.getScheduler().callSyncMethod(Challenges.getInstance(), () -> {
			MenuPosition.set(player, new EmptyMenuPosition());
			inventory.open(player, Challenges.getInstance());
			return null;
		});
	}

	private void createInventory(@Nonnull PlayerStats stats, @Nonnull AnimatedInventory inventory, @Nonnull int... slots) {
		int i = 0;
		for (Statistic statistic : Statistic.values()) {
			double value = stats.getStatisticValue(statistic);
			String format = statistic.formatChat(value);

			Message message = Message.valueOf("STAT_" + statistic.name());
			inventory.cloneLastAndAdd().setItem(slots[i], new ItemBuilder(Material.PAPER, message.asString()).setLore("§8» §7" + format).build());

			i++;
		}
	}

}
