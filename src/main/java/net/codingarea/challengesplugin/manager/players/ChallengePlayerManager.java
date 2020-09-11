package net.codingarea.challengesplugin.manager.players;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.events.ChallengeEndCause;
import net.codingarea.challengesplugin.utils.animation.AnimationSound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-06-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class ChallengePlayerManager {

	private final Challenges plugin;

	private boolean playerRespawn;
	private boolean endOnPlayerDeath;

	public ChallengePlayerManager(Challenges plugin) {
		this.plugin = plugin;
	}

	public void handlePlayerDeath(Player player, ChallengeEndCause cause, PlayerDeathEvent deathEvent) {

		if (cause == null || cause == ChallengeEndCause.KILL_ALL) return;
		if (player == null) throw new NullPointerException("Player cannot be null!");

		if (endOnPlayerDeath) {
			plugin.getServerManager().handleChallengeEnd(player, cause, deathEvent);
			return;
		}

		if (playerRespawn) {
			AnimationSound.DEATH_SOUND.play(player);
		} else {
			respawnDeadPlayer(player);
			checkEndOnDeath(player, cause, deathEvent);
		}

	}

	private void respawnDeadPlayer(Player player) {
		player.setGameMode(GameMode.SPECTATOR);
		AnimationSound.DEATH_SOUND.play(player);
	}

	private void checkEndOnDeath(Player player, ChallengeEndCause cause, PlayerDeathEvent deathEvent) {

		int alivePlayers = 0;
		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			if (currentPlayer.getGameMode() == GameMode.SPECTATOR) continue;
			alivePlayers++;
		}

		if (alivePlayers == 1) {
			plugin.getServerManager().handleChallengeEnd(player, ChallengeEndCause.LAST_MAN_STANDING, deathEvent);
		}
		if (alivePlayers == 0) {
			plugin.getServerManager().handleChallengeEnd(player, cause, deathEvent);
		}
	}

	public void setEndOnPlayerDeath(boolean endOnPlayerDeath) {
		this.endOnPlayerDeath = endOnPlayerDeath;
	}

	public void setPlayerRespawn(boolean playerRespawn) {
		this.playerRespawn = playerRespawn;
	}

	public boolean isEndOnPlayerDeath() {
		return endOnPlayerDeath;
	}

	public boolean isPlayerRespawn() {
		return playerRespawn;
	}
}
