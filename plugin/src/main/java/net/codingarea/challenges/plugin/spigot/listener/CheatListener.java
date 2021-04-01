package net.codingarea.challenges.plugin.spigot.listener;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class CheatListener implements Listener {

	public CheatListener() {
		Bukkit.getOnlinePlayers().stream()
				.filter(player -> player.getGameMode() == GameMode.CREATIVE)
				.findFirst().ifPresent(player -> handleCheatsDetected(player));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onGameModeChange(@Nonnull PlayerGameModeChangeEvent event) {
		if (event.getNewGameMode() == GameMode.CREATIVE)
			handleCheatsDetected(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCommand(@Nonnull PlayerCommandPreprocessEvent event) {
		String[] commands = {
				"give",
				"replaceitem",
				"effect",
				"i",
				"summon",
				"enchant",
				"heal",
				"kill"
		};
		String message = event.getMessage().toLowerCase();
		if (message.isEmpty()) return;

		String[] args = message.substring(1).trim().split(" ");
		String commandName = args[0];
		if (commandName.contains(":")) {
			commandName = commandName.substring(commandName.indexOf(':') + 1);
		}

		for (String command : commands) {
			if (!message.startsWith("/" + command)) continue;
			if (!hasPermission(event.getPlayer(), command)) continue;
			handleCheatsDetected(event.getPlayer());
			break;
		}
	}

	private boolean hasPermission(@Nonnull Player player, @Nonnull String command) {
		String[] prefixes = {
			"challenges.",
			"minecraft.command.",
			"essentials.",
			"bukkit."
		};

		for (String prefix : prefixes) {
			if (player.hasPermission(prefix + command))
				return true;
		}

		return false;
	}

	private void handleCheatsDetected(@Nonnull Player player) {
		if (Challenges.getInstance().getServerManager().hasCheated()) return;
		if (!Challenges.getInstance().getStatsManager().isNoStatsAfterCheating()) return;
		Challenges.getInstance().getServerManager().setHasCheated();
		Logger.info("Detected cheating: No more stats can be collected");
		Message.forName("cheats-detected").broadcast(Prefix.CHALLENGES, NameHelper.getName(player));
	}

}
