package net.codingarea.challenges.plugin.spigot.command;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.utils.bukkit.command.SenderCommand;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class HelpCommand implements SenderCommand {

	@Override
	public void onCommand(@Nonnull CommandSender sender, @Nonnull String[] args) {
		sender.sendMessage(Prefix.CHALLENGES + "§7This server is running §e§lChallenges §ev" + Challenges.getInstance().getVersion());
		sender.sendMessage(Prefix.CHALLENGES + "§7This plugin was made by §eCodingArea");
		sender.sendMessage(Prefix.CHALLENGES + "§7Visit the source at §egithub.com/anweisen/Challenges");
		sender.sendMessage(Prefix.CHALLENGES + "§7For more join our discord §ediscord.gg/74Ay5zF");
	}

}
