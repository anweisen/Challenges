package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.OneEnabledSetting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.server.ChallengeEndCause;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.ParticleUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class RespawnSetting extends OneEnabledSetting {

	private final Map<Player, Location> locationsBeforeRespawn = new ConcurrentHashMap<>();

	public RespawnSetting() {
		super(MenuType.SETTINGS, "challenge_end_handle");
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.RED_BED, Message.forName("item-respawn-setting"));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDeath(@Nonnull PlayerDeathEvent event) {
		Player player = event.getEntity();
		locationsBeforeRespawn.put(player, player.getLocation());
		if (isEnabled()) {
			SoundSample.DEATH.play(player);
		} else {
			player.setGameMode(GameMode.SPECTATOR);
			player.teleport(player.getLocation());
			player.setBedSpawnLocation(null, true);

			if (ChallengeAPI.isStarted())
				checkAllPlayersDead();
		}

		ParticleUtils.spawnUpGoingParticleCircle(Challenges.getInstance(), player.getLocation(), Particle.SPELL_WITCH, 17, 1, 2);
	}

	private void checkAllPlayersDead() {
		int playersAlive = 0;
		for (Player current : Bukkit.getOnlinePlayers()) {
			if (current.getGameMode() != GameMode.SPECTATOR)
				playersAlive++;
		}
		if (playersAlive == 0) {
			ChallengeAPI.endChallenge(ChallengeEndCause.GOAL_FAILED);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onRespawn(@Nonnull PlayerRespawnEvent event) {
		if (isEnabled()) return;

		Player player = event.getPlayer();
		Location locationBeforeRespawn = locationsBeforeRespawn.remove(player);
		if (locationBeforeRespawn == null) return;
		event.setRespawnLocation(locationBeforeRespawn);
	}

}
