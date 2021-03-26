package net.codingarea.challenges.plugin.management.player;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class PlayerInstanceManager {

	private static final Map<Player, ChallengePlayer> players = new ConcurrentHashMap<>();

	@Nonnull
	public static ChallengePlayer getPlayer(@Nonnull Player player) {
		return players.computeIfAbsent(player, key -> new ChallengePlayer(player));
	}

}
