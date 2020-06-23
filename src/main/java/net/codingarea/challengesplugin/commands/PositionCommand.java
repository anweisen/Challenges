package net.codingarea.challengesplugin.commands;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.LanguageManager;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.utils.PositionUtil;
import net.codingarea.challengesplugin.utils.PositionUtil.ChallengePosition;
import net.codingarea.challengesplugin.utils.YamlConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-07-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class PositionCommand implements CommandExecutor, TabCompleter {

	private final List<ChallengePosition> positions;

	public PositionCommand() {
		positions = new ArrayList<>();
		loadPositions();
	}

	public void loadPositions() {

		ConfigurationSection section = getConfig().getConfig().getConfigurationSection("position");

		if (section == null) return;

		for (String currentPositionKey : section.getKeys(false)) {

			ChallengePosition currentPosition = PositionUtil.loadPosition(getConfig().getConfig(), currentPositionKey);
			if (currentPosition == null) continue;
			positions.add(currentPosition);

		}

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) return true;

		if (args.length != 1) {
			sender.sendMessage(Challenges.getInstance().getStringManager().POSITION_PREFIX + LanguageManager.syntax("/position <Name>"));
			return true;
		}

		Player player = (Player) sender;

		String name = args[0].toLowerCase();

		if (getLocationNamesLowerCase().contains(name)) {

			/*
			 * This will be executed when a position was found by the given name
			 */

			ChallengePosition position = getPositionByLowerCaseName(name);
			sender.sendMessage(Challenges.getInstance().getStringManager().POSITION_PREFIX + Translation.POSITION_GET.get()
					.replace("%name%", position.getName()).replace("%position%", position.toString() + " §eDistance: §7" + (int)position.getLocation().distance(player.getLocation()) + ""));

		} else {

			String displayName = args[0];

			ChallengePosition position = new ChallengePosition(player.getLocation(), displayName, player.getName());
			positions.add(position);

			PositionUtil.savePosition(getConfig().getConfig(), position);
			getConfig().save();

			for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
				String positionName = "§e" + displayName + " §8(" + position.toString() + "§8)";
				currentPlayer.sendMessage(Challenges.getInstance().getStringManager().POSITION_PREFIX + Translation.POSITION_CREATE.get().replace("%player%", player.getName()).replace("%position%", positionName));
			}

		}

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		if (args.length != 1) return null;

		List<String> list = getLocationNamesLowerCase();

		if (!args[0].isEmpty()) {
			List<String> remove = new ArrayList<>();
			for (String currentString : list) {
				if (!currentString.toLowerCase().startsWith(args[0].toLowerCase())) remove.add(currentString);
			}
			list.removeAll(remove);
		}

		return list;

	}

	private ChallengePosition getPositionByLowerCaseName(String name) {

		for (ChallengePosition currentPosition : positions) {
			if (currentPosition == null || currentPosition.getName() == null) continue;
			if (currentPosition.getName().toLowerCase().equalsIgnoreCase(name)) return currentPosition;
		}

		return null;

	}

	private List<String> getLocationNamesLowerCase() {

		List<String> list = new ArrayList<>();

		for (ChallengePosition currentPosition : positions) {
			if (currentPosition == null || currentPosition.getName() == null) continue;
			list.add(currentPosition.getName().toLowerCase());
		}

		return list;

	}

	private List<String> getLocationNames() {

		List<String> list = new ArrayList<>();

		for (ChallengePosition currentPosition : positions) {
			list.add(currentPosition.getName());
		}

		return list;

	}

	public YamlConfig getConfig() {
		return Challenges.getInstance().getConfigManager().getPositionConfig();
	}

}
