package net.codingarea.challenges.plugin.spigot.command;

import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.utils.bukkit.command.SenderCommand;
import net.codingarea.challenges.plugin.utils.misc.CommandHelper;
import net.codingarea.challenges.plugin.utils.misc.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class HealCommand implements SenderCommand {

	@Override
	public void onCommand(@Nonnull CommandSender sender, @Nonnull String[] args) {

		List<Player> targets = new ArrayList<>();

		if (args.length > 0) {
			targets.addAll(CommandHelper.getPlayers(sender, args[0]));

		} else if (sender instanceof Player) {
			targets.add((Player) sender);
		}

		if (targets.isEmpty()) {
			Message.forName("command-no-target").send(sender, Prefix.CHALLENGES);
			return;
		}

		boolean otherPlayers = false;
		for (Player player : targets) {
			Message.forName("command-heal-healed").send(player, Prefix.CHALLENGES);
			player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			player.setFoodLevel(20);
			player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));

			if (player != sender)
				otherPlayers = true;
		}

		if (otherPlayers)
			Message.forName("command-heal-healed-others").send(sender, Prefix.CHALLENGES, targets.size());
	}

}
