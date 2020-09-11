package net.codingarea.discordstatsbot.commandmanager.commands;

import net.codingarea.discordstatsbot.commandmanager.events.CommandEvent;
import net.codingarea.discordstatsbot.commandmanager.CommandType;

/**
 * @author anweisen
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 */

public interface ICommand {

	void onCommand(CommandEvent event);

	String getName();

	String[] getAlias();

	default CommandType getType() {
		return CommandType.GENERAL;
	}

	default boolean shouldReactToWebhooks() {
		return false;
	}

	default boolean shouldReactToBots() {
		return false;
	}

	default boolean shouldProcessInNewThread() {
		return false;
	}

	default boolean shouldReactToMentionPrefix() {
		return false;
	}

}
