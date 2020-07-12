package net.codingarea.discordstatsbot.commandmanager;

import net.codingarea.discordstatsbot.commandmanager.commands.Command.CommandType;
import net.codingarea.discordstatsbot.commandmanager.commands.ICommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author anweisen
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 */

public class CommandHandler {

	public enum MessageReactionBehavior {
		REACT_NEVER,
		REACT_IF_COMMAND_WANTS,
		REACT_ALWAYS;
	}

	private MessageReactionBehavior reactToWebhooks = MessageReactionBehavior.REACT_IF_COMMAND_WANTS;
	private MessageReactionBehavior reactToBots = MessageReactionBehavior.REACT_IF_COMMAND_WANTS;
	private final Collection<ICommand> commands;

	public CommandHandler() {
		commands = new ArrayList<>();
	}

	public void registerCommand(ICommand command) {
		commands.add(command);
	}

	public void registerCommands(ICommand... commands) {
		for (ICommand currentCommand : commands) {
			registerCommand(currentCommand);
		}
	}

	public void unregisterAllCommands() {
		commands.clear();
	}

	/**
	 * @return returns null when no command is registered with this name
	 */
	public ICommand getCommand(String name) {

		for (ICommand currentCommand : commands) {
			if (currentCommand.getName() == null) continue;
			if (name.toLowerCase().startsWith(currentCommand.getName().toLowerCase())) return currentCommand;
			if (currentCommand.getAlias() == null) continue;
			for (String currentAlias : currentCommand.getAlias()) {
				if (name.toLowerCase().startsWith(currentAlias.toLowerCase())) return currentCommand;
			}
		}

		return null;

	}

	private String getCommandName(ICommand command, String raw) {

		if (raw.toLowerCase().startsWith(command.getName().toLowerCase())) {
			return command.getName();
		}

		if (command.getAlias() != null) {
			for (String currentAlias : command.getAlias()) {
				if (currentAlias == null) continue;
				if (raw.toLowerCase().startsWith(currentAlias.toLowerCase())) return currentAlias;
			}
		}

		return null;

	}

	/**
	 * Returns a list with all commands registered.
	 * It will return a empty list when there are no commands registered
	 * @return a list with all commands
	 */
	public List<ICommand> getCommands() {
		return new ArrayList<>(this.commands);
	}

	/**
	 * @return returns
	 *    CommandResult.COMMAND_NOT_FOUND if no command was found
	 *    CommandResult.INVALID_CHANNEL_GUILD_COMMAND if a guild command was triggered in a private channel
	 *    CommandResult.INVALID_CHANNEL_PRIVATE_COMMAND if a private command was triggered in a guild channel
	 *    CommandResult.PREFIX_NOT_USED if the prefix was not used ^^
	 *    CommandResult.WEBHOOK_MESSAGE_NO_REACT if the command was triggered by a webhook and react to webhooks is disabled
	 *
	 * @param prefix the prefix which should be in front of the command
	 * @param event the command event the command was received
	 */
	public CommandResult handleCommand(String prefix, MessageReceivedEvent event) {

		String raw = event.getMessage().getContentRaw().toLowerCase();

		if (!raw.startsWith(prefix)) return CommandResult.PREFIX_NOT_USED;

		String commandName = raw.substring(prefix.length());
		ICommand command = getCommand(commandName);

		if (command == null) return CommandResult.COMMAND_NOT_FOUND;

		if (reactToWebhooks != MessageReactionBehavior.REACT_ALWAYS && event.isWebhookMessage() && (!command.shouldReactToWebhooks() || reactToWebhooks == MessageReactionBehavior.REACT_NEVER)) {
			return CommandResult.WEBHOOK_MESSAGE_NO_REACT;
		} else if (reactToBots != MessageReactionBehavior.REACT_ALWAYS && event.getAuthor().isBot() && (!command.shouldReactToBots() || reactToBots == MessageReactionBehavior.REACT_NEVER)) {
			return CommandResult.BOT_MESSAGE_NO_REACT;
		} else if (command.getType() != null && command.getType() == CommandType.GUILD && !event.isFromGuild()) {
			return CommandResult.INVALID_CHANNEL_GUILD_COMMAND;
		} else if (command.getType() != null && command.getType() == CommandType.PRIVATE && event.isFromGuild()) {
			return CommandResult.INVALID_CHANNEL_PRIVATE_COMMAND;
		}

		command.onCommand(new CommandEvent(prefix, getCommandName(command, commandName), event));

		return CommandResult.SUCCESS;

	}


	public void setWebhookMessageBehavior(MessageReactionBehavior behavior) {
		this.reactToWebhooks = behavior;
	}

	public void setBotMessageBehavior(MessageReactionBehavior behavior) {
		this.reactToBots = behavior;
	}

	public MessageReactionBehavior getBotMessageBehavior() {
		return reactToBots;
	}

	public MessageReactionBehavior getWebhookMessageBehavior() {
		return reactToWebhooks;
	}

}
