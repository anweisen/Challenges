package net.codingarea.challenges.plugin.management.stats;

import net.anweisen.utilities.database.exceptions.DatabaseException;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.management.scheduler.policy.ChallengeStatusPolicy;
import net.codingarea.challenges.plugin.spigot.listener.StatsListener;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class StatsManager implements Listener {

	private final boolean enabled;
	private final boolean noStatsAfterCheating;

	private final Map<UUID, PlayerStats> cache = new ConcurrentHashMap<>();

	public StatsManager() {
		enabled = Challenges.getInstance().getConfigDocument().getBoolean("save-player-stats") && Challenges.getInstance().getDatabaseManager().getDatabase() != null;
		noStatsAfterCheating = Challenges.getInstance().getConfigDocument().getBoolean("no-stats-after-cheating");
	}

	public void register() {
		if (enabled) {
			Challenges.getInstance().getScheduler().register(this);
			Challenges.getInstance().registerListener(this, new StatsListener());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onLeave(@Nonnull PlayerQuitEvent event) {
		if (Challenges.getInstance().getWorldManager().isShutdownBecauseOfReset()) return;
		PlayerStats cached = cache.remove(event.getPlayer().getUniqueId());
		if (cached == null) return;
		store(event.getPlayer().getUniqueId(), cached);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(@Nonnull PlayerJoinEvent event) {
		PlayerStats stats = getStats(event.getPlayer().getUniqueId());
		cache.put(event.getPlayer().getUniqueId(), stats);
	}

	@ScheduledTask(ticks = 30 * 20, challengePolicy = ChallengeStatusPolicy.ALWAYS)
	public void storeCached() {
		for (Entry<UUID, PlayerStats> entry : cache.entrySet()) {
			store(entry.getKey(), entry.getValue());
		}
	}

	private void store(@Nonnull UUID uuid, @Nonnull PlayerStats stats)  {
		try {
			Challenges.getInstance().getDatabaseManager().getDatabase()
					.insertOrUpdate("challenges")
					.where("uuid", uuid)
					.set("stats", stats.asDocument())
					.execute();
			Logger.debug("Saved stats for uuid '" + uuid + "': " + stats);
		} catch (DatabaseException ex) {
			Logger.severe("Could not save player stats for uuid '" + uuid + "'", ex);
		}
	}

	@Nonnull
	public PlayerStats getStats(@Nonnull UUID uuid) {
		PlayerStats cached = cache.get(uuid);
		if (cached != null) return cached;

		try {
			PlayerStats stats = getStatsFromDatabase(uuid);
			cache.put(uuid, stats);
			Logger.debug("Loaded stats for uuid '" + uuid + "': " + stats);
			return stats;
		} catch (DatabaseException ex) {
			Logger.severe("Could not get player stats for uuid " + uuid, ex);
			return new PlayerStats();
		}
	}

	@Nonnull
	private PlayerStats getStatsFromDatabase(@Nonnull UUID uuid) throws DatabaseException {
		return Challenges.getInstance().getDatabaseManager().getDatabase()
				.query("challenges")
				.where("uuid", uuid)
				.execute().first()
				.map(result -> new PlayerStats(result.getDocument("stats")))
				.orElse(new PlayerStats());
	}

	public boolean isEnabled() {
		return enabled && Challenges.getInstance().getDatabaseManager().isConnected();
	}

	public boolean isNoStatsAfterCheating() {
		return noStatsAfterCheating;
	}

}
