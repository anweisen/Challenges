package net.codingarea.challenges.plugin.spigot.listener;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.lang.loader.UpdateLoader;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import net.codingarea.challenges.plugin.utils.misc.ParticleUtils;
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

	public PlayerConnectionListener() {
		messages = Challenges.getInstance().getConfigDocument().getBoolean("join-quit-messages");
		timerPausedInfo = Challenges.getInstance().getConfigDocument().getBoolean("timer-is-paused-info");
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
			if (timerPausedInfo && ChallengeAPI.isPaused()) {
				Message.forName("timer-paused-message").send(player, Prefix.CHALLENGES);
			}
		}

		if (Challenges.getInstance().getStatsManager().isNoStatsAfterCheating() && Challenges.getInstance().getServerManager().hasCheated()) {
			Message.forName("cheats-already-detected").send(player, Prefix.CHALLENGES);
		}

	}

	@EventHandler
	public void onQuit(@Nonnull PlayerQuitEvent event) {

		Player player = event.getPlayer();
		Challenges.getInstance().getScoreboardManager().handleQuit(player);

		if (Challenges.getInstance().getWorldManager().isShutdownBecauseOfReset()) {
			event.setQuitMessage(null);
		} else if (messages) {
			event.setQuitMessage(null);
			Message.forName("quit-message").broadcast(Prefix.CHALLENGES, NameHelper.getName(event.getPlayer()));
		}

	}

}
