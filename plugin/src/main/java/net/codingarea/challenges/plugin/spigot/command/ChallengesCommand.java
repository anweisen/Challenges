package net.codingarea.challenges.plugin.spigot.command;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class ChallengesCommand implements PlayerCommand {

	@Override
	public void onCommand(@Nonnull Player player, @Nonnull String[] args) {
		Challenges.getInstance().getMenuManager().openGUI(player);
	}

}
