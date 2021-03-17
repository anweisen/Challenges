package net.codingarea.challenges.plugin.spigot.command;

import net.codingarea.challenges.plugin.utils.bukkit.command.SenderCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class StartCommand implements SenderCommand {

	@Override
	public void onCommand(@Nonnull CommandSender sender, @Nonnull String[] args) {
		Bukkit.dispatchCommand(sender, "timer resume");
	}

}
