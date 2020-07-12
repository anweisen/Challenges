package net.codingarea.discordstatsbot.listener;

import net.codingarea.discordstatsbot.commandmanager.CommandHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

/**
 * @author anweisen
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 */

public class MessageListener extends ListenerAdapter {

	private final CommandHandler commandHandler;

	public MessageListener(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	@Override
	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
		commandHandler.handleCommand("cs ", event);
	}
}
