package net.codingarea.challengesplugin.commands;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.Translation;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

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
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return true;
		Player player = (Player) sender;

		Player[] players = {player};

		if (args.length > 1) {
			if (args[1].equals("@a")) {
				players = (Player[]) Bukkit.getOnlinePlayers().toArray();
			} else {
				player = Bukkit.getPlayer(args[1]);
				if (player == null) {
					return true;
				}
				players = new Player[]{player};

			}
		}

		GameMode gameMode = GameMode.CREATIVE;

		if (args.length > 0) {
			String arg = args[0];
			if (arg.equalsIgnoreCase("survival") || arg.equals("0") || arg.equalsIgnoreCase("s")) {
				gameMode = GameMode.SURVIVAL;
			} else if (arg.equalsIgnoreCase("spectator") || arg.equals("3")) {
				gameMode = GameMode.SPECTATOR;
			} else if (arg.equalsIgnoreCase("adventure") || arg.equals("2") || arg.equalsIgnoreCase("a")) {
				gameMode = GameMode.ADVENTURE;
			}

		}

		String mode =  gameMode.name().substring(0, 1) + gameMode.name().toLowerCase().substring(1);

		for (Player currentPlayer : players) {
			currentPlayer.sendMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.GAMEMODE_CHANGED.get().replace("%mode%", mode));
			currentPlayer.setGameMode(gameMode);

		}


		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		if (args.length == 2) {
			return null;
		} else if (args.length > 2) {
			return new ArrayList<>();
		}

		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList("creative", "survival", "adventure", "spectator", "0", "1", "2", "3"));
		List<String> remove = new ArrayList<>();

		for (String currentString : list) {
			if (!(currentString.startsWith(args[0].toLowerCase()))) remove.add(currentString);
		}
		list.removeAll(remove);

		return list;
	}

}
