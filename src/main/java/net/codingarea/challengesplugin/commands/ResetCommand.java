package net.codingarea.challengesplugin.commands;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.WorldManager;
import net.codingarea.challengesplugin.manager.lang.Translation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
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
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (resetConfirm && (args.length != 1 || !args[0].equalsIgnoreCase("confirm"))) {
			sender.sendMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.RESET_CONFIRM.get());
			return true;
		}

		WorldManager.prepareReset(true, sender);

		return true;

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (resetConfirm) {
			if (args.length == 1) {
				return Arrays.asList("confirm");
			}
		}
		return null;
	}
}
