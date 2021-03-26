package net.codingarea.challenges.plugin.utils.bukkit.command;

import net.codingarea.challenges.plugin.utils.misc.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ForwardingCommand implements PlayerCommand, TabCompleter {

	private final String forwardingCommand;
	private final boolean overrideTab;

	public ForwardingCommand(@Nonnull String forwardingCommand) {
		this(forwardingCommand, true);
	}

	public ForwardingCommand(@Nonnull String forwardingCommand, boolean overrideTab) {
		this.forwardingCommand = forwardingCommand;
		this.overrideTab = overrideTab;
	}

	@Override
	public void onCommand(@Nonnull Player player, @Nonnull String[] args) throws Exception {
		player.performCommand(forwardingCommand + " " + StringUtils.getArrayAsString(args, " "));
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
		sender.sendMessage(overrideTab + "");
		return overrideTab ? new ArrayList<>() : null;
	}

}