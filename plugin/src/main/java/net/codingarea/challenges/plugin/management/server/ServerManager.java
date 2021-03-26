package net.codingarea.challenges.plugin.management.server;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.Goal;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import net.codingarea.challenges.plugin.utils.misc.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public final class ServerManager {

	private static boolean isFresh; // This indicated if the timer was never started before
	private static boolean hasCheated = false;

	public ServerManager() {
		hasCheated = Challenges.getInstance().getConfigManager().getSessionConfig().getBoolean("cheated", hasCheated);
		isFresh = Challenges.getInstance().getConfigManager().getSessionConfig().getBoolean("fresh", true);
	}

	public void setNotFresh() {
		isFresh = false;
		Challenges.getInstance().getConfigManager().getSessionConfig().set("fresh", false);
	}

	public void setHasCheated() {
		hasCheated = true;
		Challenges.getInstance().getConfigManager().getSessionConfig().set("cheated", true);
	}

	public boolean isFresh() {
		return isFresh;
	}

	public boolean hasCheated() {
		return hasCheated;
	}

	public void endChallenge(@Nonnull ChallengeEndCause endCause) {
		if (ChallengeAPI.isPaused()) {
			Logger.warn("Tried to end challenge while timer was paused");
			return;
		}

		Challenges.getInstance().getChallengeTimer().pause(false);

		Goal currentGoal = Challenges.getInstance().getChallengeManager().getCurrentGoal();
		List<Player> winners = new LinkedList<>();
		if (currentGoal != null)
			currentGoal.getWinnersOnEnd(winners);

		String winnerString = StringUtils.getIterableAsString(winners, ", ", NameHelper::getName);
		String time = Challenges.getInstance().getChallengeTimer().getFormattedTime();
		String seed = Bukkit.getWorlds().isEmpty() ? "?" : Bukkit.getWorlds().get(0).getSeed() + "";
		endCause.getMessage(!winners.isEmpty()).broadcast(Prefix.CHALLENGES, time, winnerString, seed);

		for (Player player : Bukkit.getOnlinePlayers()) {
			player.setGameMode(GameMode.SPECTATOR);
			SoundSample.BLAST.play(player);

			try {
				player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
			} catch (IllegalArgumentException ex) {
				// We cant spawn fireworks like that in some versions of spigot
			}
		}

	}

}
