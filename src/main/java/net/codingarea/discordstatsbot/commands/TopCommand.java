package net.codingarea.discordstatsbot.commands;

import net.codingarea.challengesplugin.manager.players.stats.PlayerStats;
import net.codingarea.challengesplugin.manager.players.stats.StatsAttribute;
import net.codingarea.challengesplugin.utils.sql.MySQL;
import net.codingarea.discordstatsbot.commandmanager.events.CommandEvent;
import net.codingarea.discordstatsbot.commandmanager.commands.Command;
import net.codingarea.discordstatsbot.enitites.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @author anweisen
 * Challenges developed on 08-02-2020
 * https://github.com/anweisen
 */

public class TopCommand extends Command {

	public TopCommand() {
		super("top", true, "lb", "lead", "best");
	}

	@Override
	public void onCommand(CommandEvent event) {

		if (event.getArgs().length == 0) {
			event.queueReply("Benutze " + CommandEvent.syntax(event, "<category>"));
			return;
		}

		String categoryName = event.getArgsAsString();
		StatsAttribute category = StatsAttribute.byName(categoryName);

		if (category == null) {
			event.queueReply("Die Kategorie `" + categoryName + "` gibt es nicht. Mit " + CommandEvent.syntax(event, "categories", false) + " siehst du alle.");
			return;
		}

		try {

			ResultSet result = MySQL.get("SELECT * FROM user");

			if (result == null) {
				event.queueReply("Es wurden keine Spieler gefunden");
				return;
			}

			TreeMap<Double, List<String>> stats = new TreeMap<>(Collections.reverseOrder());

			while (result.next()) {

				String playerName = result.getString("player");
				String currentStatsJSON = result.getString("stats");

				if (playerName == null || currentStatsJSON == null) continue;

				PlayerStats currentStats = PlayerStats.fromJSON(currentStatsJSON);
				double attribute = currentStats.get(category);

				if (attribute == 0) continue;

				List<String> playersWithStat = stats.getOrDefault(attribute, new ArrayList<>());
				playersWithStat.add(playerName);

				stats.put(attribute, playersWithStat);

			}

			if (stats.isEmpty()) {
				event.queueReply("Es wurden keine Spieler gefunden");
				return;
			}

			EmbedBuilder embed = Embeds.builder();
			StringBuilder builder = new StringBuilder();
			embed.setAuthor("» Leaderboard von " + category.getName(), null, "https://i.imgur.com/NlE6R0Z.png");

			int place = 1;
			for (Entry<Double, List<String>> currentPlaceEntry : stats.entrySet()) {

				String emoji = toEmoji(place);
				for (String currentPlayer : currentPlaceEntry.getValue()) {
					builder.append("\n" + emoji + " | **" + currentPlayer + "** » " + category.format(currentPlaceEntry.getKey(), true));
				}

				place++;
				if (place > 10) break;

			}
			embed.setDescription(builder.toString());

			event.queueReply(embed.build());

		} catch (Exception ex) {
			event.queueReply("Etwas ist schief gelafuen: `" + ex.getMessage() + "`");
		}

	}

	private String toEmoji(int place) {
		switch (place) {
			case 1:
				return ":first_place:";
			case 2:
				return ":second_place:";
			case 3:
				return ":third_place:";
			case 4:
				return "<:4th:739601711173468260>";
			case 5:
				return "<:5th:739601711311880325>";
			case 6:
				return "<:6th:739601711378726963>";
			case 7:
				return "<:7th:739602225436950599>";
			case 8:
				return "<:8th:739602225600659477>";
			case 9:
				return "<:9th:739602225705517167>";
			default:
				return "<:10th:739602225860575244>";
		}
	}

}
