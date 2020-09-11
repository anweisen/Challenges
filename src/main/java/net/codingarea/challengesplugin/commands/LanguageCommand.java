package net.codingarea.challengesplugin.commands;

import net.codingarea.challengesplugin.manager.lang.LanguageManager;
import net.codingarea.challengesplugin.manager.lang.LanguageManager.Language;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-10-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class LanguageCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		if (args.length != 1 || Language.getLanguage(args[0]) == null) {
			sender.sendMessage(Prefix.CHALLENGES + LanguageManager.syntax("/language <de/en>"));
			return true;
		}

		Language language = Language.getLanguage(args[0]);
		try {
			LanguageManager.loadTemplate(language);
		} catch (IOException ignored) { }
		LanguageManager.setLanguage(language);
		LanguageManager.onLanguageChange();
		Bukkit.broadcastMessage(Prefix.CHALLENGES + "§7Language templates loaded: §e" + Utils.getEnumName(language.name()));
		return true;

	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		return args.length != 1 ? null : Utils.getMatchingSuggestions(args[0], "german", "english");
	}
}
