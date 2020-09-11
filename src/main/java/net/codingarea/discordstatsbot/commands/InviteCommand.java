package net.codingarea.discordstatsbot.commands;

import net.codingarea.discordstatsbot.DiscordBot;
import net.codingarea.discordstatsbot.commandmanager.events.CommandEvent;
import net.codingarea.discordstatsbot.commandmanager.commands.Command;

/**
 * @author anweisen
 * Challenges developed on 07-14-2020
 * https://github.com/anweisen
 */

public class InviteCommand extends Command {

	public InviteCommand() {
		super("invite");
	}

	@Override
	public void onCommand(CommandEvent event) {
		event.reply("Du kannst mich über diesen Link einladen:" +
			"\n" + DiscordBot.BOT_INVITE).queue(message -> {
			message.suppressEmbeds(true).queue(ignored -> {}, exception -> {});
		}, exception -> {});
	}
}
