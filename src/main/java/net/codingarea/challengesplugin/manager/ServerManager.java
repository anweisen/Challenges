package net.codingarea.challengesplugin.manager;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.events.ChallengeEndCause;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.timer.ChallengeTimer;
import net.codingarea.challengesplugin.utils.AnimationUtil.AnimationSound;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-06-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
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
			Bukkit.broadcastMessage(Translation.CHALLENGE_END_GOAL_REACHED.get().replace("%time%", ChallengeTimer.getTimeDisplay(plugin.getChallengeTimer().getSeconds())).replace("%winner%", players).replace("%seed%", Bukkit.getWorlds().get(0).getSeed() + ""));
		} else if (cause == ChallengeEndCause.TIMER_END) {
			Bukkit.broadcastMessage(Translation.CHALLENGE_END_TIMER_END.get().replace("%time%", ChallengeTimer.getTimeDisplay(plugin.getChallengeTimer().getSeconds())).replace("%winner%", players).replace("%seed%", Bukkit.getWorlds().get(0).getSeed() + ""));
		} else if (cause == ChallengeEndCause.PLAYER_CHALLENGE_FAIL || cause == ChallengeEndCause.PLAYER_DEATH) {
			Bukkit.broadcastMessage(Translation.CHALLENGE_END_GOAL_FAILED.get().replace("%time%", ChallengeTimer.getTimeDisplay(plugin.getChallengeTimer().getSeconds())).replace("%winner%", players).replace("%seed%", Bukkit.getWorlds().get(0).getSeed() + ""));
		}

		plugin.getChallengeTimer().stopTimer(null, false);

		if (setAllToSpectator) {
			for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
				dropInventory(currentPlayer.getInventory(), currentPlayer.getLocation());
				currentPlayer.setGameMode(GameMode.SPECTATOR);
				currentPlayer.updateInventory();
			}
		}

		playEndEffect();

	}

	private void dropInventory(PlayerInventory inventory, Location location) {

		List<ItemStack> items = new ArrayList<>();
		items.addAll(Arrays.asList(inventory.getContents()));
		items.addAll(Arrays.asList(inventory.getArmorContents()));

		inventory.clear();

		for (ItemStack currentItemStack : items) {
			if (currentItemStack.getType() == Material.AIR) continue;
			Item currentItem = (Item) location.getWorld().spawnEntity(location, EntityType.DROPPED_ITEM);
			currentItem.setItemStack(currentItemStack);
		}

	}

	private void playEndEffect() {

		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			currentPlayer.getWorld().spawnEntity(currentPlayer.getLocation(), EntityType.FIREWORK);
		}

		new AnimationSound(Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1F, 1F).broadcast();

	}

}
