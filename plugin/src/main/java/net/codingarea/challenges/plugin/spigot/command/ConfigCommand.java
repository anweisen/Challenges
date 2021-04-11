package net.codingarea.challenges.plugin.spigot.command;

import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.commons.config.document.GsonDocument;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.bukkit.command.Completer;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class ConfigCommand implements PlayerCommand, Completer {

	private final boolean enabled;

	public ConfigCommand() {
		enabled = Challenges.getInstance().getConfigDocument().getBoolean("save-player-configs");
	}

	@Override
	public void onCommand(@Nonnull Player player, @Nonnull String[] args) throws Exception {

		if (args.length != 1) {
			Message.forName("syntax").send(player, Prefix.CHALLENGES, "config <save/load/reset>");
			return;
		}

		switch (args[0].toLowerCase()) {
			case "reset":
			case "restore":
				Challenges.getInstance().getChallengeManager().restoreDefaults();
				Message.forName("player-config-reset").broadcast(Prefix.CHALLENGES);
				if (!enabled) break;
			case "save":
				if (!checkEnabled(player)) return;
				Challenges.getInstance().getChallengeManager().saveSettings(player);
				Message.forName("player-config-saved").send(player, Prefix.CHALLENGES);
				break;
			case "load":
				if (!checkEnabled(player)) return;
				Document config = Challenges.getInstance().getDatabaseManager().getDatabase()
						.query("challenges")
						.select("config")
						.where("uuid", player.getUniqueId())
						.execute().firstOrEmpty().getDocument("config");
				Challenges.getInstance().getChallengeManager().loadSettings(config);
				Message.forName("player-config-loaded").send(player, Prefix.CHALLENGES);
				break;
			default:
				Message.forName("syntax").send(player, Prefix.CHALLENGES, "config <save/load/reset>");
		}

	}

	private boolean checkEnabled(@Nonnull Player player) {
		if (!enabled || !Challenges.getInstance().getDatabaseManager().isConnected()) {
			Message.forName("feature-disabled").send(player, Prefix.CHALLENGES);
			SoundSample.BASS_OFF.play(player);
			return false;
		}
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull String[] args) {
		return args.length == 1 ? Utils.filterRecommendations(args[0], "save", "load", "reset") : null;
	}

}
