package net.codingarea.challengesplugin.commands;

import com.mysql.fabric.xmlrpc.base.Array;
import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.LanguageManager;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-01-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class GamemodeCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

		if (!(sender instanceof Player)) return true;
		Player player = (Player) sender;

		List<Player> players = new ArrayList<>();
		if (args.length == 2) {
			if (args[1].equalsIgnoreCase("@a") || args[1].equals("*")) {
				players.addAll(Bukkit.getOnlinePlayers());
			} else {
				player = Bukkit.getPlayer(args[1]);
				if (player == null) {
					sender.sendMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.PLAYER_NOT_FOUND.get().replace("%player%", args[0]));
					return true;
				}
				players.add(player);
			}
		} else {
			players.add(player);
		}

		GameMode gamemode = GameMode.CREATIVE;
		if (args.length > 0) {
			String arg = args[0];
			if (arg.equalsIgnoreCase("survival") || arg.equals("0") || arg.equalsIgnoreCase("s")) {
				gamemode = GameMode.SURVIVAL;
			} else if (arg.equalsIgnoreCase("spectator") || arg.equals("3")) {
				gamemode = GameMode.SPECTATOR;
			} else if (arg.equalsIgnoreCase("adventure") || arg.equals("2") || arg.equalsIgnoreCase("a")) {
				gamemode = GameMode.ADVENTURE;
			}
		}

		String mode = Utils.getEnumName(gamemode.name());

		for (Player currentPlayer : players) {
			currentPlayer.sendMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.GAMEMODE_CHANGED.get().replace("%mode%", mode));
			currentPlayer.setGameMode(gamemode);
		}

		if (!players.contains(sender)) {
			sender.sendMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.GAMEMODE_CHANGED_OTHERS.get().replace("%players%", players.size()+""));
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

		if (args.length == 2) {
			ArrayList<String> suggestions = new ArrayList<>();
			for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
				suggestions.add(currentPlayer.getName());
			}
			suggestions.add("@a");
			return Utils.getMatchingSuggestions(args[1].toLowerCase(), suggestions);
		} else if (args.length > 2) {
			return new ArrayList<>();
		}

		return Utils.getMatchingSuggestions(args[0].toLowerCase(), "creative", "survival", "adventure", "spectator");
	}

}
