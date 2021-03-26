package net.codingarea.challenges.plugin.spigot.command;

import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.utils.misc.CommandHelper;
import net.codingarea.challenges.plugin.utils.misc.StringUtils;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GamemodeCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {

		List<Player> targets = new ArrayList<>();

		if (args.length <= 0) {
			sender.sendMessage(Prefix.CHALLENGES + "§7Syntax: §e/gm <0 | 1 | 2| 3> <player | @p | @e | @a | @r>");
			return true;
		}

		GameMode gameMode = getGameMode(args[0]);

		if (args.length > 1) {
			targets.addAll(CommandHelper.getPlayers(sender, args[1]));

		} else if (sender instanceof Player) {
			targets.add(((Player) sender).getPlayer());
		}

		if (targets.isEmpty()) {
			Message.forName("command-gamemode-no-target").send(sender, Prefix.CHALLENGES);
			return true;
		}

		boolean otherPlayers = false;
		String enumName = StringUtils.getEnumName(gameMode);

		for (Player player : targets) {
			Message.forName("command-gamemode-gamemode-changed").send(player, Prefix.CHALLENGES, enumName);
			player.setGameMode(gameMode);
			if (player != sender) {
				otherPlayers = true;
			}

		}
		if (otherPlayers) {
			Message.forName("command-gamemode-gamemode-changed-others").send(sender, Prefix.CHALLENGES, enumName, targets.size());
		}

		return true;
	}

	@Nonnull
	private GameMode getGameMode(String input) {

		switch (input.toLowerCase()) {
			case "survival":
			case "0": return GameMode.SURVIVAL;

			case "spectator":
			case "3": return GameMode.SPECTATOR;

			case "adventure":
			case "2": return GameMode.ADVENTURE;
		}

		return GameMode.CREATIVE;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
		if (args.length > 1) {
			return null;
		}
		return Utils.filterRecommendations(args[0], "0", "1", "2", "3");
	}

}