package net.codingarea.challenges.plugin.spigot.listener;

import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.language.loader.UpdateLoader;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import net.codingarea.challenges.plugin.utils.misc.ParticleUtils;
import net.codingarea.challenges.plugin.utils.misc.DatabaseHelper;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class PlayerConnectionListener implements Listener {

	private final boolean messages;
	private final boolean timerPausedInfo;
	private final boolean startTimerOnJoin;
	private final boolean resetOnLastQuit;
	private final boolean pauseOnLastQuit;
	private final boolean restoreDefaultsOnLastQuit;

	public PlayerConnectionListener() {
		Document config = Challenges.getInstance().getConfigDocument();
		messages = config.getBoolean("join-quit-messages");
		timerPausedInfo = config.getBoolean("timer-is-paused-info");
		startTimerOnJoin = config.getBoolean("start-on-first-join");
		resetOnLastQuit = config.getBoolean("reset-on-last-leave");
		pauseOnLastQuit = config.getBoolean("pause-on-last-leave");
		restoreDefaultsOnLastQuit = config.getBoolean("restore-defaults-on-last-leave");
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin(@Nonnull PlayerJoinEvent event) {

		Player player = event.getPlayer();

		player.getLocation().getChunk().load(true);
		ParticleUtils.spawnUpGoingParticleCircle(Challenges.getInstance(), player.getLocation(), Particle.SPELL_MOB, 17, 1, 2);
		Challenges.getInstance().getScoreboardManager().handleJoin(player);

		if (messages) {
			event.setJoinMessage(null);
			Message.forName("join-message").broadcast(Prefix.CHALLENGES, NameHelper.getName(event.getPlayer()));
		}

		if (player.hasPermission("challenges.gui")) {
			if (Challenges.getInstance().isFirstInstall()) {
				player.sendMessage(Prefix.CHALLENGES + "§7Thanks for downloading §e§lChallenges§7!");
				player.sendMessage(Prefix.CHALLENGES + "§7You can change the language in the §econfig.yml");
				player.sendMessage(Prefix.CHALLENGES + "§7For more join our discord §ediscord.gg/74Ay5zF");
			}
			if (!UpdateLoader.isNewestPluginVersion()) {
				Message.forName("deprecated-plugin-version").send(player, Prefix.CHALLENGES, "spigotmc.org/resources/" + UpdateLoader.RESOURCE_ID);
			}
			if (!UpdateLoader.isNewestConfigVersion()) {
				Message.forName("deprecated-config-version").send(player, Prefix.CHALLENGES);
			}
			if (timerPausedInfo && !startTimerOnJoin && ChallengeAPI.isPaused()) {
				Message.forName("timer-paused-message").send(player, Prefix.CHALLENGES);
			}
		}

		if (Challenges.getInstance().getStatsManager().isNoStatsAfterCheating() && Challenges.getInstance().getServerManager().hasCheated()) {
			Message.forName("cheats-already-detected").send(player, Prefix.CHALLENGES);
		}

		if (startTimerOnJoin) {
			ChallengeAPI.resumeTimer();
		}

		if (Challenges.getInstance().getDatabaseManager().getDatabase().isConnected()) {
			Challenges.getInstance().runAsync(() -> DatabaseHelper.savePlayerData(player));
		}

	}

	@EventHandler
	public void onQuit(@Nonnull PlayerQuitEvent event) {

		Player player = event.getPlayer();
		Challenges.getInstance().getScoreboardManager().handleQuit(player);
		DatabaseHelper.clearCache(event.getPlayer().getUniqueId());

		if (Challenges.getInstance().getWorldManager().isShutdownBecauseOfReset()) {
			event.setQuitMessage(null);
		} else if (messages) {
			event.setQuitMessage(null);
			Message.forName("quit-message").broadcast(Prefix.CHALLENGES, NameHelper.getName(event.getPlayer()));
		}

		if (Bukkit.getOnlinePlayers().size() <= 1) {
			if (restoreDefaultsOnLastQuit) {
				Challenges.getInstance().getChallengeManager().restoreDefaults();
			}

			if (!Challenges.getInstance().getWorldManager().isShutdownBecauseOfReset()) {
				if (resetOnLastQuit && !ChallengeAPI.isFresh()) {
					Challenges.getInstance().getWorldManager().prepareWorldReset(Bukkit.getConsoleSender());
				} else if (pauseOnLastQuit && ChallengeAPI.isStarted()) {
					ChallengeAPI.pauseTimer();
				}
			}
		}

	}

}
