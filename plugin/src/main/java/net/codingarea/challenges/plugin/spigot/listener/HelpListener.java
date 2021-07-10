package net.codingarea.challenges.plugin.spigot.listener;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.content.Prefix;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class HelpListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onCommand(@Nonnull PlayerCommandPreprocessEvent event) {

		String message = event.getMessage().toLowerCase();
		if (message.isEmpty()) return;

		String[] args = message.substring(1).trim().split(" ");
		String commandName = args[0];

		List<String> names = new ArrayList<>(Challenges.getInstance().getCommand("help").getAliases());
		names.add("help");

		if (!names.contains(commandName)) return;

		Player sender = event.getPlayer();

		sender.sendMessage(Prefix.CHALLENGES + "§7This server is running §e§lChallenges §ev" + Challenges.getInstance().getVersion());
		sender.sendMessage(Prefix.CHALLENGES + "");
		sender.sendMessage(Prefix.CHALLENGES + "§7Made by §eCodingArea §8(§eanweisen §7& §eKxmischesDomi§8)");
		sender.sendMessage(Prefix.CHALLENGES + "§7Visit the source at §egithub.com/anweisen/Challenges");
		sender.sendMessage(Prefix.CHALLENGES + "§7Download at §espigotmc.org/resources/80548");
		sender.sendMessage(Prefix.CHALLENGES + "§7For more join our discord §ediscord.gg/74Ay5zF");

	}

}
