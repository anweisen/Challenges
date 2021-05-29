package net.codingarea.challenges.plugin.spigot.command;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.utils.bukkit.command.SenderCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class ReloadCommand implements SenderCommand {

	@Override
	public void onCommand(@Nonnull CommandSender sender, @Nonnull String[] args) throws Exception {
		Message.forName("reload").send(sender, Prefix.CHALLENGES);
		try {
			Bukkit.getPluginManager().disablePlugin(Challenges.getInstance());
			Challenges.getInstance().onLoad();
			Bukkit.getPluginManager().enablePlugin(Challenges.getInstance());
		} catch (Exception ex) {
			ex.printStackTrace();
			Message.forName("reload-failed").send(sender, Prefix.CHALLENGES);
		}
		Message.forName("reload-success").send(sender, Prefix.CHALLENGES);
	}

}
