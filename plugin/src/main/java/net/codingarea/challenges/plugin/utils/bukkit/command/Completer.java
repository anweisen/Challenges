package net.codingarea.challenges.plugin.utils.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface Completer extends TabCompleter {

	@Override
	@Nullable
	default List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		return onTabComplete(sender, args);
	}

	@Nullable
	List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull String[] args);
}
