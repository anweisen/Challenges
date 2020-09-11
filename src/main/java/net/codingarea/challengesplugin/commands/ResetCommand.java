package net.codingarea.challengesplugin.commands;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.WorldManager;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-02-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class ResetCommand implements CommandExecutor, TabCompleter {

	private final boolean resetConfirm;

	public ResetCommand() {
		resetConfirm = Challenges.getInstance().getConfig().getBoolean("force-reset-confirm");
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

		if (resetConfirm && (args.length != 1 || !args[0].equalsIgnoreCase("confirm"))) {
			sender.sendMessage(Prefix.CHALLENGES + Translation.RESET_CONFIRM.get());
			return true;
		}

		WorldManager.prepareReset(sender);

		return true;

	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if (resetConfirm) {
			if (args.length == 1) {
				return Collections.singletonList("confirm");
			}
		}
		return null;
	}
}
