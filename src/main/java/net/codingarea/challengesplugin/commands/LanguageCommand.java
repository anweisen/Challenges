package net.codingarea.challengesplugin.commands;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.LanguageManager;
import net.codingarea.challengesplugin.manager.lang.LanguageManager.Language;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-10-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class LanguageCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		if (args.length != 1 || Language.getLanguage(args[0]) == null) {
			sender.sendMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + LanguageManager.syntax("/language <de/en>"));
			return true;
		}

		Language language = Language.getLanguage(args[0]);
		LanguageManager.setLanguage(language);
		LanguageManager.onLanguageChange();
		Bukkit.broadcastMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + "§7Language temporarily set to §e" + Utils.getEnumName(language.name()));
		return true;

	}
}
