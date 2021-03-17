package net.codingarea.challenges.plugin.spigot.command;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.bukkit.command.SenderCommand;
import net.codingarea.challenges.plugin.utils.bukkit.command.Completer;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class TimerCommand implements SenderCommand, Completer {

	@Override
	public void onCommand(@Nonnull CommandSender sender, @Nonnull String[] args) {

		if (args.length == 0) {
			if (sender instanceof Player)
				Challenges.getInstance().getMenuManager().openMenu((Player) sender, MenuType.TIMER, 0);
			return;
		}

		switch (args[0].toLowerCase()) {
			default:
				sender.sendMessage(Prefix.CHALLENGES + Message.forName("syntax").asString("timer <resume/pause/reset>"));
				return;
			case "resume":
			case "start":
				Challenges.getInstance().getChallengeTimer().resume();
				break;
			case "stop":
			case "pause":
				Challenges.getInstance().getChallengeTimer().pause(true);
				break;
			case "reset":
				Challenges.getInstance().getChallengeTimer().reset();
				break;
		}

	}

	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull String[] args) {
		if (args.length == 1) {
			return Utils.filterRecommendations(args[0], "resume", "pause", "stop", "start", "reset", "show", "hide", "mode", "set");
		}
		return null;
	}

}
