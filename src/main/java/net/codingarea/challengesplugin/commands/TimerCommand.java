package net.codingarea.challengesplugin.commands;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.LanguageManager;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.timer.ChallengeTimer;
import net.codingarea.challengesplugin.timer.ChallengeTimer.TimerMode;
import net.codingarea.challengesplugin.utils.animation.AnimationSound;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class TimerCommand implements CommandExecutor, TabCompleter {

	private final Challenges plugin;

	public TimerCommand(Challenges plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

		if (!(sender instanceof Player)) return true;

		Player player = (Player) sender;

		if (command.getName().equals("start")) {
			if (!plugin.getChallengeTimer().isPaused()) {
				player.sendMessage(Prefix.TIMER + Translation.TIMER_ALREADY_STARTED.get());
			} else {
				plugin.getChallengeTimer().resume(player);
			}
			return true;
		} else if (command.getName().equals("pause")) {
			plugin.getChallengeTimer().stopTimer(player, true);
			return true;
		}

		// command can only be the "/timer" command here
		if (args.length == 0) {
			plugin.getChallengeTimer().getMenu().openMainMenu(player);
			AnimationSound.OPEN_SOUND.play(player);
			return true;
		}

		switch (args[0].toLowerCase()) {

			case "resume": case "start":

				if (!plugin.getChallengeTimer().isPaused()) {
					player.sendMessage(Prefix.TIMER + Translation.TIMER_ALREADY_STARTED.get());
					return true;
				}

				plugin.getChallengeTimer().resume(player);
				break;

			case "pause":
				plugin.getChallengeTimer().stopTimer(player, true);
				break;

			case "reset":
				plugin.getChallengeTimer().resetTimer(player);
				Bukkit.broadcastMessage(Prefix.TIMER + Translation.TIMER_RESET.get().replace("%player%", player.getName()));
				break;

			case "show":

				if (!plugin.getChallengeTimer().isHided()) {
					player.sendMessage(Prefix.TIMER.get() + Translation.TIMER_ALREADY_SHOW);
					return true;
				}

				plugin.getChallengeTimer().setHided(false);
				Bukkit.broadcastMessage(Prefix.TIMER.get() + Translation.TIMER_SHOW);
				break;

			case "hide":

				if (plugin.getChallengeTimer().isHided()) {
					player.sendMessage(Prefix.TIMER + Translation.TIMER_ALREADY_HIDE.get());
					return true;
				}

				plugin.getChallengeTimer().setHided(true);
				Bukkit.broadcastMessage(Prefix.TIMER + Translation.TIMER_HIDE.get());

				break;

			case "set":

				if (!(args.length > 1)) {
					player.sendMessage(Prefix.TIMER + LanguageManager.syntax("/timer set <time s/m/h/d>"));
					return true;
				}

				int seconds = calculateSeconds(args);

				if (seconds >= ChallengeTimer.MAX_VALUE) {
					seconds = ChallengeTimer.MAX_VALUE + 1;
				} else if (seconds < ChallengeTimer.MIN_VALUE) {
					seconds = ChallengeTimer.MIN_VALUE;
				}

				plugin.getChallengeTimer().setMaxSeconds(seconds);
				Bukkit.broadcastMessage(Prefix.TIMER + Translation.TIMER_SET_TIME.get().replace("%time%", ChallengeTimer.getTimeDisplay(seconds)).replace("%player%", player.getName()));
				break;

			case "mode":

				if (args.length != 2) {
					player.sendMessage(Prefix.TIMER + LanguageManager.syntax("/timer mode <up / down>"));
					return true;
				}

				String mode = args[1];

				if (mode.equalsIgnoreCase("up")) {
					plugin.getChallengeTimer().setMode(TimerMode.UP, player);
				} else if (mode.equalsIgnoreCase("down")) {
					plugin.getChallengeTimer().setMode(TimerMode.DOWN, player);
				} else {
					player.sendMessage(Prefix.TIMER + LanguageManager.syntax("/timer mode <up / down>"));
					return true;
				}

				break;

		}

		return true;

	}

	private int getMultiplierByEnding(String ending) {

		if (ending == null) return 1;
		if (ending.isEmpty()) return 1;

		switch (ending.toLowerCase()) {

			case "s":
				return 1;

			case "m":
				return 60;

			case "h":
				return 60*60;

			case "d":
				return 24*60*60;

		}

		return -1;

	}

	private int calculateSeconds(String[] args) {

		int seconds = 0;

		for (String currentArg : args) {

			int multiplier = getMultiplierByEnding(currentArg.substring(currentArg.length() - 1));

			String raw;
			if (multiplier != -1) {
				raw = currentArg.substring(0, currentArg.length() - 1);
			} else {
				raw = currentArg;
				multiplier = 1;
			}

			try {
				seconds += Integer.parseInt(raw) * multiplier;
			} catch (NumberFormatException ignored) { }

		}

		return seconds;

	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

		if (!command.getName().equals("timer")) return null;

		if (args.length == 1) {
			return Utils.getMatchingSuggestions(args[0].toLowerCase(), "show", "hide", "mode", "set", "pause", "resume", "reset", "start");
		} else if (args.length >= 2) {

			if (args[0].equalsIgnoreCase("set")) {

				if (args[args.length - 1].isEmpty()) return null;

				List<String> list = new ArrayList<>(Arrays.asList("d", "h", "m", "s"));
				Collections.sort(list);

				if (args[args.length - 1].isEmpty()) return null;

				for (String currentEnding : list) {
					if (args[args.length - 1].toLowerCase().endsWith(currentEnding)) {
						return null;
					}
				}

				return list;

			} else if (args[0].equalsIgnoreCase("mode")) {
				return Utils.getMatchingSuggestions(args[1].toLowerCase(), "up", "down");
			}
		}

		return null;

	}

}
