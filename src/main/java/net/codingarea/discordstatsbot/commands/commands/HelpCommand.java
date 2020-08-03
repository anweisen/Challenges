package net.codingarea.discordstatsbot.commands;

import net.codingarea.discordstatsbot.DiscordBot;
import net.codingarea.discordstatsbot.commandmanager.events.CommandEvent;
import net.codingarea.discordstatsbot.commandmanager.commands.Command;
import net.codingarea.discordstatsbot.enitites.Embeds;

import static net.codingarea.discordstatsbot.commandmanager.events.CommandEvent.syntax;

/**
 * @author anweisen
 * Challenges developed on 08-02-2020
 * https://github.com/anweisen
 */

public class HelpCommand extends Command {

	public HelpCommand() {
		super("help", "h");
	}

	@Override
	public void onCommand(CommandEvent event) {
		event.queueReply(Embeds.builder()
				.setAuthor("» " + event.getJDA().getSelfUser().getName() + " • Information", DiscordBot.BOT_INVITE, event.getJDA().getSelfUser().getEffectiveAvatarUrl())
				.addField("Information (3)", "» " + syntax(event, "stats <gamemode> [player]", false) + " • Sehe Spielerstatistiken ein\n" +
						"» " + syntax(event, "top <category>", false) + " • Sehe die Leaderboards ein\n" +
						"» " + syntax(event, "categories", false) + " • Sehe ein welche Kategorien es gibt\n", false)
				.addField("Allgemeines (2)", "» Lade den Bot auf deinen Server [ein](" + DiscordBot.BOT_INVITE + ")\n" +
						"» Joine auf den [Supportserver](" + DiscordBot.SERVER_INVITE + ")", false)
				.build());
	}
}
