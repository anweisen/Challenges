package net.codingarea.challenges.plugin.utils.bukkit.command;

import net.codingarea.challenges.plugin.lang.Prefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface SenderCommand extends CommandExecutor {

	@Override
	default boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		try {
			onCommand(sender, args);
		} catch (Exception ex) {
			sender.sendMessage(Prefix.CHALLENGES + "§cSomething went wrong while executing the command");
		}
		return true;
	}

	void onCommand(@Nonnull CommandSender sender, @Nonnull String[] args);

}
