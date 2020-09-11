package net.codingarea.challengesplugin.commands;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.LanguageManager;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

/**
 * @author anweisen
 * Challenges developed on 06-26-2020
 * https://github.com/anweisen
 */

public class ConfigCommand implements CommandExecutor, TabCompleter {

	private final Challenges plugin;

	public ConfigCommand(Challenges plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

		if (!(sender instanceof Player)) return true;

		Player player = (Player) sender;

		if (!plugin.getPlayerSettingsManager().savePlayerConfigs()) {
			player.sendMessage(Prefix.CHALLENGES + Translation.FEATURE_DISABLED.get());
			return true;
		}

		if (label.equalsIgnoreCase("save")) {
			player.performCommand("config save");
			return true;
		} else if (label.equalsIgnoreCase("load")) {
			player.performCommand("config load");
			return true;
		}

		if (args.length != 1) {
			player.sendMessage(Prefix.CHALLENGES + LanguageManager.syntax("/config <save/load>"));
			return true;
		}

		switch (args[0].toLowerCase()) {

			case "save":
				try {
					plugin.getPlayerSettingsManager().save(player.getName());
					player.sendMessage(Prefix.CHALLENGES + Translation.SAVE_CONFIG_SUCCESS.get());
				} catch (SQLException ex) {
					player.sendMessage(Prefix.CHALLENGES+ Translation.MYSQL_ERROR.get().replace("%error%", ex.getMessage()));
				}
				break;

			case "load":
				try {
					if (plugin.getPlayerSettingsManager().load(player.getName())) {
						plugin.getMenuManager().loadMenus();
						player.sendMessage(Prefix.CHALLENGES + Translation.LOAD_CONFIG_SUCCESS.get());
					} else {
						player.sendMessage(Prefix.CHALLENGES + Translation.NO_CONFIG_FOUND.get());
					}
				} catch (SQLException ex) {
					player.sendMessage(Prefix.CHALLENGES + Translation.MYSQL_ERROR.get().replace("%error%", ex.getMessage()));
				}
				break;

			default:
				player.sendMessage(Prefix.CHALLENGES + LanguageManager.syntax("/config <save/load>"));
				break;

		}

		return true;

	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

		if (args == null || args.length != 1) return null;
		return Utils.getMatchingSuggestions(args[0].toLowerCase(), "load", "save");

	}
}
