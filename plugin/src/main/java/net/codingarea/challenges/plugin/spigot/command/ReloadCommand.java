package net.codingarea.challenges.plugin.spigot.command;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.utils.bukkit.command.SenderCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 * @deprecated Not working as intended. Therefore removed from the plugin entirely for the time being.
 */
@Deprecated
public class ReloadCommand implements SenderCommand {

	@Override
	public void onCommand(@Nonnull CommandSender sender, @Nonnull String[] args) throws Exception {
		Message.forName("reload").broadcast(Prefix.CHALLENGES);
		try {
			Challenges plugin = Challenges.getInstance();
			Bukkit.getPluginManager().disablePlugin(plugin);
			plugin.onLoad();
			Bukkit.getPluginManager().enablePlugin(plugin);
		} catch (Exception ex) {
			ex.printStackTrace();
			Message.forName("reload-failed").broadcast(Prefix.CHALLENGES);
		}
		Message.forName("reload-success").broadcast(Prefix.CHALLENGES);
	}

}
