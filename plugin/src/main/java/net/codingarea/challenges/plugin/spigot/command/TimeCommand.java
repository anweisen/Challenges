package net.codingarea.challenges.plugin.spigot.command;

import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.utils.bukkit.command.Completer;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public class TimeCommand implements PlayerCommand, Completer {

	@Override
	public void onCommand(@Nonnull Player player, @Nonnull String[] args) throws Exception {

		if (args.length <= 0) {
			Message.forName("syntax").send(player, Prefix.CHALLENGES, "<time <set/add/remove/query/day/night/noon/midnight>");
			return;
		}
		World world = player.getWorld();

		switch (args[0].toLowerCase()) {
			case "day":
				player.performCommand("time set day");
				break;
			case "night":
				player.performCommand("time set night");
				break;
			case "noon":
				player.performCommand("time set noon");
				break;
			case "midnight":
				player.performCommand("time set midnight");
				break;
			case "set": {
				if (args.length <= 1) {
					Message.forName("syntax").send(player, Prefix.CHALLENGES, "time set <ticks/day/night/noon/midnight>");
					break;
				}
				long time = getTime(args[1]);
				if (time == -1) {
					Message.forName("syntax").send(player, Prefix.CHALLENGES, "time set <ticks/day/night/noon/midnight>");
					break;
				}
				world.setTime(time);
				player.sendMessage(Prefix.CHALLENGES + "§7Set: §e" + time + " Ticks");
				break;
			}
			case "add": {
				if (args.length <= 1) {
					Message.forName("syntax").send(player, Prefix.CHALLENGES, "time add <ticks>");
					break;
				}
				long time = getLongFromString(args[1]);
				if (time == -1) {
					Message.forName("syntax").send(player, Prefix.CHALLENGES, "time add <ticks>");
					break;
				}
				world.setTime(world.getTime() + time);
				player.sendMessage(Prefix.CHALLENGES + "§7Add: §e" + time + " Ticks");
				break;
			}
			case "subtract": {
				if (args.length <= 1) {
					Message.forName("syntax").send(player, Prefix.CHALLENGES, "time subtract <ticks>");
					break;
				}
				long time = getLongFromString(args[1]);
				if (time == -1) {
					Message.forName("syntax").send(player, Prefix.CHALLENGES, "time subtract <ticks>");
					break;
				}
				world.setTime(world.getTime() - time);
				player.sendMessage(Prefix.CHALLENGES + "§7Subtract: §e" + time + " Ticks");
				break;
			}
			case "query": {
				if (args.length <= 1) {
					Message.forName("syntax").send(player, Prefix.CHALLENGES, "time query <day/daytime/gametime>");
					break;
				}
				long time = getQueryTime(world, args[1]);
				if (time == -1) {
					Message.forName("syntax").send(player, Prefix.CHALLENGES, "time query <day/daytime/gametime>");
					break;
				}
				player.sendMessage(Prefix.CHALLENGES + "§7Query: §e" + time + (args[1].equalsIgnoreCase("day") ? " Days" : " Ticks"));
				break;
			}

		}

	}

	private long getTime(@Nonnull String input) {
		switch (input.toLowerCase()) {
			case "day":         return 1000;
			case "noon":        return 6000;
			case "night":       return 13000;
			case "midnight":    return 18000;
		}
		return getLongFromString(input);
	}

	private long getLongFromString(@Nonnull String input) {
		try {
			return Long.parseLong(input);
		} catch (NumberFormatException ex) { }
		return -1;
	}

	private long getQueryTime(@Nonnull World world, @Nonnull String query) {
		switch (query.toLowerCase()) {
			case "day": return world.getFullTime() / 24000;
			case "daytime": return world.getTime();
			case "gametime": return world.getFullTime();
		}
		return -1;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull String[] args) {
		if (args.length <= 1) return Utils.filterRecommendations(args[0], "set", "subtract", "query", "day", "night", "noon", "midnight");
		if (args[0].equalsIgnoreCase("set")) return Utils.filterRecommendations(args[1], "day", "night", "noon", "midnight");
		if (args[0].equalsIgnoreCase("query")) return Utils.filterRecommendations(args[1], "day", "daytime", "gametime");
		return new ArrayList<>();
	}

}