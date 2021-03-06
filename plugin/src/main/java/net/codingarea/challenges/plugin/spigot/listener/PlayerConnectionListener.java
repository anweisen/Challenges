package net.codingarea.challenges.plugin.spigot.listener;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.lang.loader.UpdateLoader;
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

	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin(@Nonnull PlayerJoinEvent event) {

		Player player = event.getPlayer();

		player.getLocation().getChunk().load(true);
		ParticleUtils.spawnUpGoingParticleCircle(Challenges.getInstance(), player.getLocation(), Particle.SPELL_MOB, 17, 1, 2);
		Challenges.getInstance().getScoreboardManager().handleJoin(player);

		if (player.hasPermission("challenges.gui")) {
			if (!UpdateLoader.isNewestPluginVersion()) {
				Message.DEPRECATED_PLUGIN_VERSION.send(player, Prefix.CHALLENGES, "spigotmc.org/resources/" + UpdateLoader.RESOURCE_ID);
			}
			if (!UpdateLoader.isNewestConfigVersion()) {
				Message.DEPRECATED_CONFIG_VERSION.send(player, Prefix.CHALLENGES);
			}
		}

	}

	@EventHandler
	public void onQuit(@Nonnull PlayerQuitEvent event) {

		Player player = event.getPlayer();
		Challenges.getInstance().getScoreboardManager().handleQuit(player);

	}

}
