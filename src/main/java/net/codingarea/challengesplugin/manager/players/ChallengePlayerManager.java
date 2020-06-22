package net.codingarea.challengesplugin.manager.players;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.events.ChallengeEndCause;
import net.codingarea.challengesplugin.utils.AnimationUtil.AnimationSound;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * @author anweisen
 * Challenges developed on 06-06-2020
 * https://github.com/anweisen
 */

public class ChallengePlayerManager {

	private final Challenges plugin;

	@Setter	private boolean playerRespawn;
	@Setter private boolean endOnPlayerDeath;

	public ChallengePlayerManager(Challenges plugin) {
		this.plugin = plugin;
	}

	public void handlePlayerDeath(Player player, ChallengeEndCause cause) {

		if (cause == null || cause == ChallengeEndCause.KILL_ALL) return;
		if (player == null) throw new NullPointerException("Player cannot be null!");

		if (endOnPlayerDeath) {
			plugin.getServerManager().handleChallengeEnd(player, cause);
			return;
		}

		if (playerRespawn) {
			AnimationSound.DEATH_SOUND.play(player);
		} else {
			respawnDeadPlayer(player);
			checkEndOnDeath(player, cause);
		}


	}

	private void respawnDeadPlayer(Player player) {
		player.setGameMode(GameMode.SPECTATOR);
		AnimationSound.DEATH_SOUND.play(player);
	}

	private void checkEndOnDeath(Player player, ChallengeEndCause cause) {

		int alivePlayers = 0;
		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			if (currentPlayer.getGameMode() == GameMode.SPECTATOR) continue;
			alivePlayers++;
		}

		if (alivePlayers == 0) {
			plugin.getServerManager().handleChallengeEnd(player, cause);
		}

	}

}
