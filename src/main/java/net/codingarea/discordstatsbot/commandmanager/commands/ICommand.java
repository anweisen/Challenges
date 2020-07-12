package net.codingarea.discordstatsbot.commandmanager.commands;

import net.codingarea.discordstatsbot.commandmanager.CommandEvent;
import net.codingarea.discordstatsbot.commandmanager.commands.Command.CommandType;

/**
 * @author anweisen
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 */

public interface ICommand {

	void onCommand(CommandEvent event);

	CommandType getType();

	boolean shouldReactToWebhooks();

	boolean shouldReactToBots();

	String getName();

	String[] getAlias();

}
