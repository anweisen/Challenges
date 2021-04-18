package net.codingarea.challenges.plugin.spigot.command;

import net.anweisen.utilities.bukkit.utils.animation.AnimatedInventory;
import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.management.cloud.CloudSupportManager;
import net.codingarea.challenges.plugin.management.menu.MenuPosition;
import net.codingarea.challenges.plugin.management.menu.MenuPosition.EmptyMenuPosition;
import net.codingarea.challenges.plugin.management.menu.InventoryTitleManager;
import net.codingarea.challenges.plugin.management.stats.LeaderboardInfo;
import net.codingarea.challenges.plugin.management.stats.PlayerStats;
import net.codingarea.challenges.plugin.management.stats.Statistic;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.SkullBuilder;
import net.codingarea.challenges.plugin.utils.misc.StatsHelper;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.Bukkit;
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
			Player target = Bukkit.getPlayer(name);
			if (target != null) {
				open(player, target.getUniqueId(), target.getName());
				return;
			}

			try {
				UUID uuid = Utils.fetchUUID(name);
				open(player, uuid, name);
			} catch (IOException ex) {
				player.sendMessage(Prefix.CHALLENGES + "§7Something went wrong");
			}
		});
	}

	private void open(@Nonnull Player player, @Nonnull UUID uuid, @Nonnull String name) {

		PlayerStats stats = Challenges.getInstance().getStatsManager().getStats(uuid, name);
		name = stats.getPlayerName();

		CloudSupportManager cloudSupport = Challenges.getInstance().getCloudSupportManager();
		String coloredName = cloudSupport.isNameSupport() && cloudSupport.hasNameFor(uuid) ? cloudSupport.getColoredName(uuid) : name;

		AnimatedInventory inventory = new AnimatedInventory(InventoryTitleManager.getStatsTitle(name), 5*9, MenuPosition.HOLDER);
		StatsHelper.setAccent(inventory, 3);
		inventory.cloneLastAndAdd().setItem(13, new SkullBuilder(uuid, name, Message.forName("stats-of").asString(coloredName)).build());

		LeaderboardInfo info = Challenges.getInstance().getStatsManager().getLeaderboardInfo(uuid);
		createInventory(stats, info, inventory, StatsHelper.getSlots(2));

		MenuPosition.set(player, new EmptyMenuPosition());
		inventory.open(player, Challenges.getInstance());
	}

	private void createInventory(@Nonnull PlayerStats stats, @Nonnull LeaderboardInfo info, @Nonnull AnimatedInventory inventory, @Nonnull int... slots) {
		for (int i = 0; i < Statistic.values().length; i++) {
			Statistic statistic = Statistic.values()[i];
			double value = stats.getStatisticValue(statistic);
			String format = statistic.formatChat(value);

			ItemBuilder item = new ItemBuilder(StatsHelper.getMaterial(statistic), StatsHelper.getNameMessage(statistic).asString()).setLore(Message.forName("stats-display").asArray(format, info.getPlace(statistic))).hideAttributes();
			inventory.cloneLastAndAdd().setItem(slots[i], item);
		}
	}

}
