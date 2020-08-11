package net.codingarea.challengesplugin.commands;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.LanguageManager;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.utils.PositionUtil;
import net.codingarea.challengesplugin.utils.PositionUtil.ChallengePosition;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.YamlConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-07-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class PositionCommand implements CommandExecutor, TabCompleter {

	private final List<ChallengePosition> positions = new ArrayList<>();

	public PositionCommand() {
		loadPositions();
	}

	public void loadPositions() {

		ConfigurationSection section = getConfig().toFileConfig().getConfigurationSection("position");

		if (section == null) return;

		for (String currentPositionKey : section.getKeys(false)) {

			ChallengePosition currentPosition = PositionUtil.loadPosition(getConfig().toFileConfig(), currentPositionKey);
			if (currentPosition == null) continue;
			positions.add(currentPosition);

		}

	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

		if (!(sender instanceof Player)) return true;
		Player player = (Player) sender;

		if (args.length == 0) {

			if (positions.isEmpty()) {
				sender.sendMessage(Prefix.POSITIONS + Translation.POSITION_EMPTY.get());
			} else {

				sender.sendMessage(Prefix.POSITIONS + Translation.POSITION_LIST.get());
				for (ChallengePosition currentPosition : positions) {
					sender.sendMessage(Prefix.POSITIONS + Translation.POSITION_GET.get().replace("%name%", currentPosition.getName()).replace("%position%", currentPosition.toString() + " §eDistance: §7" + (int)currentPosition.getLocation().distance(player.getLocation())));
				}

			}

			sender.sendMessage(Prefix.POSITIONS + Translation.POSITION_CREATE.get());

		} else if (args.length == 1) {

			String name = args[0].toLowerCase();

			ChallengePosition position = getPositionByName(name);
			if (position != null) {

				sender.sendMessage(Prefix.POSITIONS + Translation.POSITION_GET.get().replace("%name%", position.getName()).replace("%position%", position.toString() + " §eDistance: §7" + (int)position.getLocation().distance(player.getLocation())));

			} else {

				if (!Challenges.timerIsStarted()) {
					sender.sendMessage(Prefix.POSITIONS + Translation.TIMER_NOT_STARTED.get());
					return true;
				}

				String displayName = args[0];

				position = new ChallengePosition(player.getLocation(), displayName, player.getName());
				positions.add(position);

				PositionUtil.savePosition(getConfig().toFileConfig(), position);
				this.getConfig().save();

				String positionName = "§e" + displayName + " §8(" + position.toString() + "§8)";
				Bukkit.broadcastMessage(Prefix.POSITIONS + Translation.POSITION_CREATED.get().replace("%player%", player.getName()).replace("%position%", positionName));

			}

		} else {
			sender.sendMessage(Prefix.POSITIONS + LanguageManager.syntax("/position [name]"));
		}

		return true;

	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

		if (args.length != 1) return new ArrayList<>();

		ArrayList<String> list = getLocationNames(true);
		return Utils.getMatchingSuggestions(args[0].toLowerCase(), list);

	}

	private ChallengePosition getPositionByName(String name) {

		for (ChallengePosition currentPosition : positions) {
			if (currentPosition == null || currentPosition.getName() == null) continue;
			if (currentPosition.getName().equalsIgnoreCase(name)) return currentPosition;
		}

		return null;

	}

	private ArrayList<String> getLocationNames(boolean lowerCase) {

		ArrayList<String> list = new ArrayList<>();

		for (ChallengePosition currentPosition : positions) {
			if (currentPosition == null || currentPosition.getName() == null) continue;
			list.add(lowerCase ? currentPosition.getName().toLowerCase() : currentPosition.getName());
		}

		return list;

	}

	public YamlConfig getConfig() {
		return Challenges.getInstance().getConfigManager().getPositionConfig();
	}

}
