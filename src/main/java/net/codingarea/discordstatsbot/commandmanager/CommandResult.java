package net.codingarea.discordstatsbot.commandmanager;

/**
 * @author anweisen
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 */

public enum CommandResult {

	INVALID_CHANNEL_PRIVATE_COMMAND,
	INVALID_CHANNEL_GUILD_COMMAND,
	WEBHOOK_MESSAGE_NO_REACT,
	BOT_MESSAGE_NO_REACT,
	PREFIX_NOT_USED,
	COMMAND_NOT_FOUND,
	SUCCESS,
	MENTION_PREFIX_NO_REACT;
}
