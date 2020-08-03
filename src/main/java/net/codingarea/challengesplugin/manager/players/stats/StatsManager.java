package net.codingarea.challengesplugin.manager.players.stats;

import net.codingarea.challengesplugin.manager.players.PlayerSettingsManager;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author anweisen
 * Challenges developed on 07-11-2020
 * https://github.com/anweisen
 */

public class StatsManager {

	private final ConcurrentHashMap<Player, PlayerStats> storedStats = new ConcurrentHashMap<>();
	private final boolean enabled;

	public StatsManager(JavaPlugin plugin) {
		enabled = plugin.getConfig().getBoolean("save-player-stats");
	}

	public void load() {
		if (enabled) {
			storedStats.clear();
			Utils.forEachPlayerOnline(this::loadForPlayer);
		}
	}

	public void storeAll() {
		for (Entry<Player, PlayerStats> currentEntry : storedStats.entrySet()) {
			String uuid = Utils.getUUID(currentEntry.getKey().getName());
			StatsWrapper.storeStats(uuid, currentEntry.getValue());
			PlayerSettingsManager.updatePlayerName(uuid, currentEntry.getKey().getName());
		}
	}

	public PlayerStats getPlayerStats(Player player) {
		PlayerStats storedPlayerStats = storedStats.get(player);
		if (storedPlayerStats != null) return storedPlayerStats;
		return loadForPlayer(player);
	}

	public void onSecond(Player player) {
		if (!player.isSneaking()) return;
		getPlayerStats(player).add(StatsAttribute.TIME_SNEAKED, 1);
	}

	public PlayerStats loadForPlayer(Player player) {
		PlayerStats fetchedStats = StatsWrapper.getStatsByName(player.getName());
		storedStats.put(player, fetchedStats);
		return fetchedStats;
	}

	public void forEveryPlayerStats(Consumer<PlayerStats> consumer) {
		for (PlayerStats currentPlayerStats : storedStats.values()) {
			consumer.accept(currentPlayerStats);
		}
	}

	public void removeFromStorage(Player player) {
		storedStats.remove(player);
	}

	public boolean isEnabled() {
		return enabled;
	}
}
