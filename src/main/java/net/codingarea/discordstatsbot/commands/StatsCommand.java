package net.codingarea.discordstatsbot.commands;

import net.codingarea.challengesplugin.manager.players.stats.PlayerStats;
import net.codingarea.challengesplugin.manager.players.stats.StatsAttribute;
import net.codingarea.challengesplugin.manager.players.stats.StatsWrapper;
import net.codingarea.challengesplugin.utils.ImageUtils;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.discordstatsbot.commandmanager.events.CommandEvent;
import net.codingarea.discordstatsbot.commandmanager.commands.Command;
import net.codingarea.discordstatsbot.enitites.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;

import static net.codingarea.challengesplugin.utils.Utils.nameIsValid;

/**
 * @author anweisen
 * Challenges developed on 08-02-2020
 * https://github.com/anweisen
 */

public class StatsCommand extends Command {

	public StatsCommand() {
		super("stats", true, "s");
	}

	@Override
	public void onCommand(CommandEvent event) {

		String player = event.getMemberName();
		if (event.getArgs().length == 1) {
			player = event.getArg(0);
		}

		if (event.getArgs().length > 1 || !nameIsValid(player)) {
			event.queueReply("Benutze " + CommandEvent.syntax(event, "<player>"));
			return;
		}


		if (!nameIsValid(player)) {
			event.queueReply("`" + player + "` ist kein gültiger Spielername");
			return;
		}

		try {

			String uuid = Utils.getUUID(player);
			PlayerStats stats = StatsWrapper.getStatsByUUID(uuid);

			if (stats.getSavedName() != null && stats.getSavedName().equalsIgnoreCase(player)) {
				player = stats.getSavedName();
			}

			EmbedBuilder embed = Embeds.builder();
			StringBuilder builder = new StringBuilder();

			for (StatsAttribute currentAttribute : stats.getDeclaredAttributes()) {
				builder.append("\n" + currentAttribute.getEmoji() + " | **" + currentAttribute.getName() + "** » " + stats.asString(currentAttribute, true));
			}
			embed.setDescription(builder.toString());

			String head = ImageUtils.getHeadURLByUUID(uuid);
			embed.setAuthor("» Challenge Stats von " + player, null, head);

			event.queueReply(embed.build());

		} catch (Exception ex) {
			event.queueReply("Etwas ist schief gelaufen: `" + ex.getMessage() + "`");
		}

	}
}
