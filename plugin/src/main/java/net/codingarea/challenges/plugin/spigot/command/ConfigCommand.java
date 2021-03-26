package net.codingarea.challenges.plugin.spigot.command;

import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.commons.config.document.EmptyDocument;
import net.anweisen.utilities.commons.config.document.GsonDocument;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
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

		if (!enabled || !Challenges.getInstance().getDatabaseManager().isConnected()) {
			Message.forName("feature-disabled").send(player, Prefix.CHALLENGES);
			SoundSample.BASS_OFF.play(player);
			return;
		}

		if (args.length != 1) {
			Message.forName("syntax").send(player, Prefix.CHALLENGES, "config <save/load>");
			return;
		}

		switch (args[0].toLowerCase()) {
			case "save":
				Document document = new GsonDocument();
				Challenges.getInstance().getChallengeManager().saveSettingsInto(document);
				Challenges.getInstance().getDatabaseManager().getDatabase()
						.insertOrUpdate("challenges")
						.where("uuid", player.getUniqueId())
						.set("config", document)
						.execute();
				Message.forName("player-config-saved").send(player, Prefix.CHALLENGES);
				break;
			case "load":
				Document config = Challenges.getInstance().getDatabaseManager().getDatabase()
						.query("challenges")
						.select("config")
						.where("uuid", player.getUniqueId())
						.execute().first()
						.orElse(new EmptyDocument());
				Challenges.getInstance().getChallengeManager().loadSettings(config);
				Message.forName("player-config-loaded").send(player, Prefix.CHALLENGES);
				break;
			default:
				Message.forName("syntax").send(player, Prefix.CHALLENGES, "config <save/load>");
		}

	}

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull String[] args) {
		return args.length == 1 ? Utils.filterRecommendations(args[0], "save", "load") : null;
	}

}
