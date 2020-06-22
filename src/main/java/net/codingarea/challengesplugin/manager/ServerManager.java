package net.codingarea.challengesplugin.manager;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.events.ChallengeEndCause;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.timer.ChallengeTimer;
import net.codingarea.challengesplugin.utils.AnimationUtil.AnimationSound;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen
 * Challenges developed on 06-06-2020
 * https://github.com/anweisen
 */

public class ServerManager {

	private static ServerManager instance;

	private Challenges plugin;

	public ServerManager(Challenges plugin) {
		instance = this;
		this.plugin = plugin;
	}

	public static void simulateChallengeEnd(Player player, ChallengeEndCause cause) {
		instance.handleChallengeEnd(player, cause);
	}

	public void handleChallengeEnd(Player player, ChallengeEndCause cause) {

		if (!Challenges.timerIsStarted()) {
			System.out.println("Challenge tried to end :: Timer not started :: " + player);
			return;
		}

		if (cause == ChallengeEndCause.KILL_ALL) return;

		List<Player> winners = new ArrayList<>();
		if (cause == ChallengeEndCause.PLAYER_CHALLENGE_GOAL_REACHED || cause == ChallengeEndCause.LAST_MAN_STANDING) {
			winners.add(player);
		} else {
			try { winners.addAll(plugin.getChallengeManager().getGoalManager().getCurrentGoal().getWinners()); } catch (NullPointerException ignored) { };
		}

		String players = Utils.getPlayerListAsString(winners);
		boolean setAllToSpectator = true;

		if (cause == ChallengeEndCause.PLAYER_CHALLENGE_GOAL_REACHED || cause == ChallengeEndCause.LAST_MAN_STANDING) {
			setAllToSpectator = false;
			Bukkit.broadcastMessage(Translation.CHALLENGE_END_GOAL_REACHED.get().replace("%time%", ChallengeTimer.getTimeDisplay(plugin.getChallengeTimer().getSeconds())).replace("%winner%", players).replace("%seed%", Bukkit.getWorlds().get(0).getSeed() + ""));
		} else if (cause == ChallengeEndCause.TIMER_END) {
			Bukkit.broadcastMessage(Translation.CHALLENGE_END_TIMER_END.get().replace("%time%", ChallengeTimer.getTimeDisplay(plugin.getChallengeTimer().getSeconds())).replace("%winner%", players).replace("%seed%", Bukkit.getWorlds().get(0).getSeed() + ""));
		} else if (cause == ChallengeEndCause.PLAYER_CHALLENGE_FAIL || cause == ChallengeEndCause.PLAYER_DEATH) {
			Bukkit.broadcastMessage(Translation.CHALLENGE_END_GOAL_FAILED.get().replace("%time%", ChallengeTimer.getTimeDisplay(plugin.getChallengeTimer().getSeconds())).replace("%winner%", players).replace("%seed%", Bukkit.getWorlds().get(0).getSeed() + ""));
		}

		plugin.getChallengeTimer().stopTimer(null, false);

		if (setAllToSpectator) {
			for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
				currentPlayer.setGameMode(GameMode.SPECTATOR);
			}
		}

		playEndEffect();

	}

	private void playEndEffect() {

		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			currentPlayer.getWorld().spawnEntity(currentPlayer.getLocation(), EntityType.FIREWORK);
		}

		new AnimationSound(Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1F, 1F).broadcast();

	}

}
