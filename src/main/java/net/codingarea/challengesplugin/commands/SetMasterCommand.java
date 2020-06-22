package net.codingarea.challengesplugin.commands;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-06-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public class SetMasterCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		if (args.length != 1) {
			sender.sendMessage(Challenges.getInstance().getStringManager().MASTER_PREFIX + LanguageManager.syntax("/setmaster <Player>"));
			return true;
		}

		Player master = Bukkit.getPlayer(args[0]);
		Challenges.getInstance().getPermissionsSystem().changeMasterTo(master);

		return true;
	}
}
