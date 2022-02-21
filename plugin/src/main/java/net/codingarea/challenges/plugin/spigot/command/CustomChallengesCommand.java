package net.codingarea.challenges.plugin.spigot.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.utils.bukkit.command.Completer;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class CustomChallengesCommand implements PlayerCommand, Completer {

	private final boolean savePlayerChallenges;

	public CustomChallengesCommand() {
		savePlayerChallenges = Challenges.getInstance().getConfigDocument().getBoolean("save-player-custom_challenges");
	}

	@Override
	public void onCommand(@Nonnull Player player, @Nonnull String[] args) throws Exception {

		if (args.length != 1) {
			Message.forName("syntax").send(player, Prefix.CHALLENGES, "custom <save/load/reset>");
			return;
		}

		switch (args[0].toLowerCase()) {
			case "reset":
			case "restore":
				Challenges.getInstance().getCustomChallengesLoader().resetChallenges();
				Message.forName("player-custom_challenges-reset").broadcast(Prefix.CHALLENGES);
				break;
			case "save":
				if (!checkEnabled(player)) return;
				Challenges.getInstance().getChallengeManager().saveCustomChallenges(player);
				Message.forName("player-custom_challenges-saved").send(player, Prefix.CHALLENGES);
				break;
			case "load":
				if (!checkEnabled(player)) return;
				Document config = Challenges.getInstance().getDatabaseManager().getDatabase()
						.query("challenges")
						.select("custom-challenges")
						.where("uuid", player.getUniqueId())
						.execute().firstOrEmpty().getDocument("custom-challenges");
				Challenges.getInstance().getChallengeManager().loadCustomChallenges(config);
				Message.forName("player-custom_challenges-loaded").send(player, Prefix.CHALLENGES);
				break;
			default:
				Message.forName("syntax").send(player, Prefix.CHALLENGES, "custom <save/load/reset>");
		}

	}

	private boolean checkEnabled(@Nonnull Player player) {
		if (!savePlayerChallenges || !Challenges.getInstance().getDatabaseManager().isConnected()) {
			Message.forName("feature-disabled").send(player, Prefix.CHALLENGES);
			SoundSample.BASS_OFF.play(player);
			return false;
		}
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull String[] args) {
		return args.length == 1 ? Utils.filterRecommendations(args[0], "save", "load", "reset") : Collections.emptyList();
	}

}
