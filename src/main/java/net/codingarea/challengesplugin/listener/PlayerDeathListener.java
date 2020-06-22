package net.codingarea.challengesplugin.listener;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.events.ChallengeEndCause;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * @author anweisen
 * Challenges developed on 06-06-2020
 * https://github.com/anweisen
 */

public class PlayerDeathListener implements Listener {

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!Challenges.timerIsStarted()) return;
		Utils.spawnUpgoingParticleCircle(event.getEntity().getLocation(), Particle.SPELL_WITCH, Challenges.getInstance(), 2, 17, 1);
		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			Challenges.getInstance().getPlayerManager().handlePlayerDeath(event.getEntity(), ChallengeEndCause.PLAYER_DEATH);
		}, 1);
	}

}
