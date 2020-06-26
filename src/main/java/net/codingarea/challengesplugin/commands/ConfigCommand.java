package net.codingarea.challengesplugin.commands;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.LanguageManager;
import net.codingarea.challengesplugin.manager.lang.Translation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author anweisen
 * Challenges developed on 06-26-2020
 * https://github.com/anweisen
 */

public class ConfigCommand implements CommandExecutor, TabCompleter {

	private Challenges plugin;

	public ConfigCommand(Challenges plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) return true;

		Player player = (Player) sender;

		if (args.length != 1) {
			player.sendMessage(plugin.getStringManager().CHALLENGE_PREFIX + LanguageManager.syntax("/config <save/load>"));
			return true;
		}

		switch (args[0].toLowerCase()) {

			case "save":
				try {
					plugin.getPlayerSettingsManager().save(player.getName());
					player.sendMessage(plugin.getStringManager().CHALLENGE_PREFIX + Translation.SAVE_CONFIG_SUCCESS.get());
				} catch (SQLException ex) {
					player.sendMessage(plugin.getStringManager().CHALLENGE_PREFIX + Translation.MYSQL_ERROR.get().replace("%error%", ex.getMessage()));
				}
				break;

			case "load":
				try {
					if (plugin.getPlayerSettingsManager().load(player.getName())) {
						plugin.getMenuManager().loadMenus();
						player.sendMessage(plugin.getStringManager().CHALLENGE_PREFIX + Translation.LOAD_CONFIG_SUCCESS.get());
					} else {
						player.sendMessage(plugin.getStringManager().CHALLENGE_PREFIX + Translation.NO_CONFIG_FOUND.get());
					}
				} catch (SQLException ex) {
					player.sendMessage(plugin.getStringManager().CHALLENGE_PREFIX + Translation.MYSQL_ERROR.get().replace("%error%", ex.getMessage()));
				}
				break;

			default:
				player.sendMessage(plugin.getStringManager().CHALLENGE_PREFIX + LanguageManager.syntax("/config <save/load>"));
				break;

		}

		return true;

	}

	@Override
	public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		if (args == null || args.length != 1) return null;

		List<String> list = new ArrayList<>(Arrays.asList("load", "save"));
		Collections.sort(list);

		List<String> remove = new ArrayList<>();
		for (String currentArg : list) {
			if (!currentArg.toLowerCase().startsWith(args[0].toLowerCase())) {
				remove.add(currentArg);
			}
		}
		list.removeAll(remove);

		return list;

	}
}
