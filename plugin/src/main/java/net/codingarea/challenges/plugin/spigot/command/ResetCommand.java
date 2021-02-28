package net.codingarea.challenges.plugin.spigot.command;

import com.sun.istack.internal.NotNull;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.utils.bukkit.Completer;
import net.codingarea.challenges.plugin.utils.bukkit.SenderCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class ResetCommand implements SenderCommand, Completer {

	private final boolean confirmReset;

	public ResetCommand() {
		confirmReset = Challenges.getInstance().getConfigDocument().getBoolean("confirm-reset");
	}

	@Override
	public void onCommand(@Nonnull CommandSender sender, @Nonnull String[] args) {

		if (confirmReset && (args.length != 1 || !args[0].equalsIgnoreCase("confirm"))) {
			sender.sendMessage(Prefix.CHALLENGES + Message.CONFIRM_RESET.asString());
			return;
		}

		Challenges.getInstance().getWorldManager().prepareWorldReset(sender);

	}

	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull String[] args) {
		if (confirmReset) {
			if (args.length == 1) {
				return Collections.singletonList("confirm");
			}
		}
		return null;
	}

}
