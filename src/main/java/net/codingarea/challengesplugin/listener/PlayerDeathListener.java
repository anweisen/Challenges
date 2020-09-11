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
 * @author anweisen & Dominik
 * Challenges developed on 06-06-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class PlayerDeathListener implements Listener {

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {

		event.setDeathMessage(null);

		if (!Challenges.timerIsStarted()) return;

		if (!Challenges.getInstance().getPlayerManager().isPlayerRespawn() || Challenges.getInstance().getPlayerManager().isEndOnPlayerDeath()) {
			event.getEntity().setHealth(event.getEntity().getMaxHealth());
			event.getEntity().teleport(event.getEntity().getLocation());
		}

		Utils.spawnUpGoingParticleCircle(event.getEntity().getLocation(), Particle.SPELL_WITCH, Challenges.getInstance(), 2, 17, 1);
		Challenges.getInstance().getPlayerManager().handlePlayerDeath(event.getEntity(), ChallengeEndCause.PLAYER_DEATH, event);

	}

}
