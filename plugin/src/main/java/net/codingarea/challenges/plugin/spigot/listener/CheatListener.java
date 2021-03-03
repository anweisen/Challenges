package net.codingarea.challenges.plugin.spigot.listener;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
				.findFirst().ifPresent(player -> handleCheatsDetected());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onGameModeChange(@Nonnull PlayerGameModeChangeEvent event) {
		if (event.getNewGameMode() == GameMode.CREATIVE)
			handleCheatsDetected();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCommand(@Nonnull PlayerCommandPreprocessEvent event) {
		String[] commands = {
				"gamemode",
				"gm",
				"give",
				"replaceitem",
				"effect",
				"i",
				"summon",
				"kill"
		};
		String message = event.getMessage().toLowerCase();
		for (String command : commands) {
			if (message.startsWith("/" + command)) {
				handleCheatsDetected();
				return;
			}
		}
	}

	private void handleCheatsDetected() {
		if (Challenges.getInstance().getServerManager().hasCheated()) return;
		Challenges.getInstance().getServerManager().setHasCheated();
		Logger.info("Detected cheating: No more stats can be collected");
		// TODO: Chat message
	}

}
