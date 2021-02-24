package net.codingarea.challenges.plugin.spigot.listener;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.misc.ParticleUtils;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class PlayerConnectionListener implements Listener {

	@EventHandler
	public void onJoin(@Nonnull PlayerJoinEvent event) {

		Player player = event.getPlayer();

		player.getLocation().getChunk().load(true);
		ParticleUtils.spawnUpGoingParticleCircle(Challenges.getInstance(), player.getLocation(), Particle.SPELL_MOB, 17, 1, 2);

	}

	@EventHandler
	public void onQuit(@Nonnull PlayerQuitEvent event) {

	}

}
