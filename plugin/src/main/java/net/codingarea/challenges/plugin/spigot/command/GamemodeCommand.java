package net.codingarea.challenges.plugin.spigot.command;

import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.utils.bukkit.command.Completer;
import net.codingarea.challenges.plugin.utils.misc.CommandHelper;
import net.codingarea.challenges.plugin.utils.misc.StringUtils;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GamemodeCommand implements CommandExecutor, Completer {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {

		List<Player> targets = new ArrayList<>();

		if (args.length <= 0) {
			Message.forName("syntax").send(sender, Prefix.CHALLENGES, "gm <gamemode> [player]");
			return true;
		}

		GameMode gamemode = getGameMode(args[0]);

		if (args.length > 1) {
			targets.addAll(CommandHelper.getPlayers(sender, args[1]));

		} else if (sender instanceof Player) {
			targets.add(((Player) sender).getPlayer());
		}

		if (targets.isEmpty()) {
			Message.forName("command-no-target").send(sender, Prefix.CHALLENGES);
			return true;
		}

		boolean otherPlayers = false;
		String gamemodeName = StringUtils.getEnumName(gamemode);

		for (Player player : targets) {
			Message.forName("command-gamemode-gamemode-changed").send(player, Prefix.CHALLENGES, gamemodeName);
			player.setGameMode(gamemode);
			if (player != sender)
				otherPlayers = true;

		}
		if (otherPlayers) {
			Message.forName("command-gamemode-gamemode-changed-others").send(sender, Prefix.CHALLENGES, gamemodeName, targets.size());
		}

		return true;
	}

	@Nonnull
	private GameMode getGameMode(String input) {
		switch (input.toLowerCase()) {
			case "survival":
			case "0":
				return GameMode.SURVIVAL;
			case "spectator":
			case "3":
				return GameMode.SPECTATOR;
			case "adventure":
			case "2":
				return GameMode.ADVENTURE;
		}
		return GameMode.CREATIVE;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull String[] args) {
		if (args.length > 1) return null;
		return Utils.filterRecommendations(args[0], "0", "1", "2", "3", "creative", "survival", "spectator", "adventure");
	}

}