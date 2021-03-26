package net.codingarea.challenges.plugin.spigot.command;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.World;
import org.bukkit.command.Command;
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
public class WeatherCommand implements PlayerCommand, TabCompleter {

	@Override
	public void onCommand(@Nonnull Player player, @Nonnull String[] args) throws Exception {

		if (args.length == 0) {
			player.sendMessage(Prefix.CHALLENGES + "§7Syntax: §e/weather <sun | clear | rain | thunder>");
			return;
		}

		World world = player.getWorld();

		switch (args[0].toLowerCase()) {

			case "clear":
			case "sun": {
				world.setStorm(false);
				world.setThundering(false);
				Message.forName("command-weather-clear").send(player, Prefix.CHALLENGES);
				break;
			}
			case "rain": {
				world.setStorm(true);
				world.setThundering(false);
				Message.forName("command-weather-rain").send(player, Prefix.CHALLENGES);
				break;
			}
			case "thunder": {
				world.setStorm(true);
				world.setThundering(true);
				Message.forName("command-weather-thunder").send(player, Prefix.CHALLENGES);
				break;
			}
			default: {
				player.sendMessage(Prefix.CHALLENGES + "Syntax: §e/weather <sun | clear | rain | thunder>");
			}

		}

	}

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
		if (args.length > 1) return new ArrayList<>();
		return Utils.filterRecommendations(args[0], "sun", "clear", "rain", "thunder");
	}

}