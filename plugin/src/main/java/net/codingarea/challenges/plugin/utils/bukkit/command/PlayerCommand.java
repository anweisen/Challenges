package net.codingarea.challenges.plugin.utils.bukkit.command;

import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface PlayerCommand extends CommandExecutor {

	@Override
	default boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (sender instanceof Player) {
			try {
				onCommand((Player) sender, args);
			} catch (Exception ex) {
				sender.sendMessage(Prefix.CHALLENGES + "§cSomething went wrong while executing the command");
				Logger.severe("Something went wrong while processing the command '/" + label + "'", ex);
			}
		} else {
			sender.sendMessage("" + Prefix.CHALLENGES + Message.forName("player-command"));
		}
		return true;
	}

	void onCommand(@Nonnull Player player, @Nonnull String[] args) throws Exception;

}
