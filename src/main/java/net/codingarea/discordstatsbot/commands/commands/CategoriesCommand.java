package net.codingarea.discordstatsbot.commands;

import net.codingarea.challengesplugin.manager.players.stats.StatsAttribute;
import net.codingarea.discordstatsbot.commandmanager.events.CommandEvent;
import net.codingarea.discordstatsbot.commandmanager.commands.Command;

/**
 * @author anweisen
 * Challenges developed on 08-02-2020
 * https://github.com/anweisen
 */

public class CategoriesCommand extends Command {

	public CategoriesCommand() {
		super("categories", "c");
	}

	@Override
	public void onCommand(CommandEvent event) {

		StringBuilder categories = new StringBuilder();
		for (StatsAttribute currentAttribute : StatsAttribute.values()) {
			if (!currentAttribute.isSavedInDatabase()) continue;
			if (!categories.toString().isEmpty()) {
				categories.append(", ");
			}
			categories.append(currentAttribute.getShortName());
		}

		event.queueReply("Es gibt die folgenden Kategorien: \n`" + categories + "`");
		
	}
}
