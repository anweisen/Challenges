package net.codingarea.challengesplugin.commands;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.players.stats.PlayerStats;
import net.codingarea.challengesplugin.manager.players.stats.StatsAttribute;
import net.codingarea.challengesplugin.manager.players.stats.StatsWrapper;
import net.codingarea.challengesplugin.utils.animation.AnimationSound;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import net.codingarea.challengesplugin.utils.items.ItemBuilder.SkullBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 */

public class StatsCommand implements CommandExecutor, TabCompleter, Listener {

	private final String title = "§8» §2Stats §8• §2";

	private final Challenges plugin;

	public StatsCommand(Challenges plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		if (!(sender instanceof Player)) return true;
		Player player = (Player) sender;

		String getFrom = player.getName();
		if (args.length > 0) {
			getFrom = args[0];
		}

		PlayerStats stats = StatsWrapper.getStatsByName(getFrom);
		if (stats.getSavedName() != null && stats.getSavedName().equalsIgnoreCase(getFrom)) {
			getFrom = stats.getSavedName();
		}

		player.openInventory(generateStatsInventory(stats, getFrom));
		AnimationSound.OPEN_SOUND.play(player);

		return true;
	}

	private Inventory generateStatsInventory(PlayerStats playerStats, String player) {

		String challengesPlayed = playerStats.asString(StatsAttribute.PLAYED, false);
		String challengesWon = playerStats.asString(StatsAttribute.WON, false);
		String damageDealt = playerStats.asString(StatsAttribute.DAMAGE_DEALT, false) + " §c❤";
		String damageTaken = playerStats.asString(StatsAttribute.DAMAGE_TAKEN, false) + " §c❤";
		String blocksBroken = playerStats.asString(StatsAttribute.BLOCKS_BROKEN, false);
		String entityKills = playerStats.asString(StatsAttribute.ENTITIES_KILLED, false);
		String timeSneaked = playerStats.asString(StatsAttribute.TIME_SNEAKED, false);
		String itemsCollected = playerStats.asString(StatsAttribute.ITEMS_COLLECTED, false);
		String jumps = playerStats.asString(StatsAttribute.JUMPS, false);

		Inventory inventory = Bukkit.createInventory(null, 3*9, title + player);
		Utils.fillInventory(inventory, ItemBuilder.FILL_ITEM, null);

		inventory.setItem(4, new SkullBuilder(player, "§7" + player + "'s stats").build());

		inventory.setItem(9, new ItemBuilder(Material.PAPER, "§e" + Translation.STATS_CHALLENGES_PLAYED).setLore(getLore(challengesPlayed)).hideAttributes().build());
		inventory.setItem(10, new ItemBuilder(Material.DIAMOND, "§e" + Translation.STATS_CHALLENGES_WON).setLore(getLore(challengesWon)).hideAttributes().build());
		inventory.setItem(11, new ItemBuilder(Material.IRON_SWORD, "§e" + Translation.STATS_DAMAGE_DEALT).setLore(getLore(damageDealt)).hideAttributes().build());
		inventory.setItem(12, new ItemBuilder(Material.LEATHER_CHESTPLATE, "§e" + Translation.STATS_DAMAGE_TAKEN).setLore(getLore(damageTaken)).hideAttributes().build());
		inventory.setItem(13, new ItemBuilder(Material.GOLDEN_PICKAXE, "§e" + Translation.STATS_BLOCKS_MINED).setLore(getLore(blocksBroken)).hideAttributes().build());
		inventory.setItem(14, new ItemBuilder(Material.DIAMOND_SWORD, "§e" + Translation.STATS_KILLS).setLore(getLore(entityKills)).hideAttributes().build());
		inventory.setItem(15, new ItemBuilder(Material.STICK, "§e" + Translation.STATS_ITEMS_COLLECTED).setLore(getLore(itemsCollected)).hideAttributes().build());
		inventory.setItem(16, new ItemBuilder(Material.CHAINMAIL_BOOTS, "§e" + Translation.STATS_TIMES_JUMPED).setLore(getLore(jumps)).hideAttributes().build());
		inventory.setItem(17, new ItemBuilder(Material.GOLDEN_BOOTS, "§e" + Translation.STATS_TIME_SNEAKED).setLore(getLore(timeSneaked)).hideAttributes().build());

		return inventory;

	}

	private String getLore(String value) {
		return "§8➥ §7" + value;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (args.length == 1) {
			return null; // Bukkit will replace 'null' with a list filled with the names of the players online
		} else {
			return new ArrayList<>();
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getView().getTitle().contains(title)) {
			event.setCancelled(true);
			AnimationSound.STANDARD_SOUND.play((Player) event.getWhoClicked());
		}
	}

}
