package net.codingarea.challengesplugin.listener;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.CloudNetManager;
import net.codingarea.challengesplugin.manager.WorldManager;
import net.codingarea.challengesplugin.manager.checker.UpdateChecker;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.players.stats.StatsWrapper;
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

		if (!UpdateChecker.pluginIsNewestVersion()) {

		}
		if (!UpdateChecker.configIsNewestVersion() && event.getPlayer().hasPermission("challenges.gui")) {
			event.getPlayer().sendMessage(Prefix.CHALLENGES + Translation.CONFIG_OLD_VERSION.get());
		}

		if (plugin.getStatsManager().isEnabled()) {
			plugin.getStatsManager().loadForPlayer(event.getPlayer());
		}

		event.getPlayer().getLocation().getChunk().load(true);
		plugin.getScoreboardManager().handlePlayerConnect(event);
		Utils.spawnUpGoingParticleCircle(event.getPlayer().getLocation(), Particle.SPELL_MOB, Challenges.getInstance(), 2, 17, 1);

		// Sending the join message 1 tick after the player joined, so the permission systems have enough time to set the display name for the player
		// Setting the join message to null, so it wont be sent twice
		if (message) event.setJoinMessage(null);
		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			if (message && joinMessage != null) Bukkit.broadcastMessage(joinMessage.replace("%player%", event.getPlayer().getName()).replace("%display%", event.getPlayer().getDisplayName()));
		}, 1);

	}

	@EventHandler
	public void handleQuit(PlayerQuitEvent event) {

		plugin.getScoreboardManager().handlePlayerDisconnect(event);

		if (plugin.getStatsManager().isEnabled()) {
			StatsWrapper.storeStats(Utils.getUUID(event.getPlayer().getName()), plugin.getStatsManager().getPlayerStats(event.getPlayer()));
			plugin.getStatsManager().removeFromStorage(event.getPlayer());
		}

		if (WorldManager.getInstance().getReseted()) {
			event.setQuitMessage(null);
			return;
		}

		if ((Bukkit.getOnlinePlayers().size() - 1) <= 0) {
			if (resetOnLastQuit && CloudNetManager.wasAlreadyIngame()) {
				WorldManager.prepareReset(null);
			} else {
				plugin.getChallengeTimer().stopTimer(null, true);
			}
		}

		if (!message || quitMessage == null) return;
		event.setQuitMessage(quitMessage.replace("%player%", event.getPlayer().getName()).replace("%display%", event.getPlayer().getDisplayName()));

	}

}
