package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.server.ChallengeEndCause;
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
public class RespawnSetting extends Setting {

	private final Map<Player, Location> locationsBeforeRespawn = new ConcurrentHashMap<>();

	public RespawnSetting() {
		super(MenuType.SETTINGS);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.RED_BED, Message.forName("item-respawn-setting"));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDeath(@Nonnull PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (isEnabled()) {
			SoundSample.DEATH.play(player);
		} else {
			locationsBeforeRespawn.put(player, player.getLocation());
			player.setGameMode(GameMode.SPECTATOR);

			if (ChallengeAPI.isStarted())
				checkAllPlayersDead();
		}

		ParticleUtils.spawnUpGoingParticleCircle(Challenges.getInstance(), player.getLocation(), Particle.SPELL_WITCH, 17, 1, 2);
	}

	public void checkAllPlayersDead() {
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
		if (locationBeforeRespawn.getY() < 0) return; // If the player dies in the void (y <= 0), we dont want him to spawn there, he would die again
		event.setRespawnLocation(locationBeforeRespawn);
	}

}
