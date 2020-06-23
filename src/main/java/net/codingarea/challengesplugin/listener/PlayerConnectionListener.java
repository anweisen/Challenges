package net.codingarea.challengesplugin.listener;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.CloudNetManager;
import net.codingarea.challengesplugin.manager.WorldManager;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-31-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class PlayerConnectionListener implements Listener {

	private final Challenges plugin;

	private final boolean message;
	private final String quitMessage;
	private final String joinMessage;

	private final boolean resetOnLastQuit;

	public PlayerConnectionListener(Challenges plugin) {
		this.plugin = plugin;
		message = plugin.getConfig().getBoolean("connection-messages-enabled");
		quitMessage = plugin.getConfig().getString("quit-message");
		joinMessage = plugin.getConfig().getString("join-message");
		resetOnLastQuit = plugin.getConfig().getBoolean("reset-on-last-quit");
	}

	@EventHandler
	public void handleJoin(PlayerJoinEvent event) {
		plugin.getPermissionsSystem().setPermissions(event.getPlayer(), false);

		event.getPlayer().getLocation().getChunk().load(true);

		plugin.getScoreboardManager().handleJoin(event.getPlayer());

		Utils.spawnUpgoingParticleCircle(event.getPlayer().getLocation(), Particle.SPELL_MOB, Challenges.getInstance(), 2, 17, 1);

		if (message) event.setJoinMessage(null);
		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			if (message && joinMessage != null) Bukkit.broadcastMessage(joinMessage.replace("%name%", event.getPlayer().getName()).replace("%display%", event.getPlayer().getDisplayName()));
			plugin.getPermissionsSystem().handlePlayerConnect(event.getPlayer());
		}, 1);

	}

	@EventHandler
	public void handleQuit(PlayerQuitEvent event) {

		if ((Bukkit.getOnlinePlayers().size() - 1) <= 0) {
			if (resetOnLastQuit && CloudNetManager.wasAlreadyIngame()) {
				WorldManager.prepareReset(true, null);
			}
		}

		plugin.getPermissionsSystem().handlePlayerDisconnect(event.getPlayer());


		if (!message || quitMessage == null) return;
		event.setQuitMessage(quitMessage.replace("%name%", event.getPlayer().getName()).replace("%display%", event.getPlayer().getDisplayName()));

	}

}
