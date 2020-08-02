package net.codingarea.discordstatsbot.commandmanager;

import net.codingarea.discordstatsbot.commandmanager.commands.Command.CommandType;
import net.codingarea.discordstatsbot.commandmanager.commands.ICommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.lang.Thread.UncaughtExceptionHandler;
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
	private final ArrayList<ICommand> commands = new ArrayList<>();

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
	public ArrayList<ICommand> getCommands() {
		return new ArrayList<>(this.commands);
	}

	public CommandResult handleCommand(String prefix, MessageReceivedEvent event) {

		String raw = event.getMessage().getContentRaw().toLowerCase().trim();
		boolean byMention = false;
		String mention = mention(event);

		if (!raw.startsWith(prefix)) {
			if (raw.startsWith(mention)) {
				prefix = mention;
				byMention = true;
			} else {
				return CommandResult.PREFIX_NOT_USED;
			}
		}

		int commandIndex = prefix.length();
		try {
			while (raw.substring(commandIndex).startsWith(" ")) {
				commandIndex++;
				prefix = prefix + " ";
			}
		} catch (Exception ignored) { }

		String commandName = raw.substring(commandIndex);
		ICommand command = getCommand(commandName);

		if (command == null) return CommandResult.COMMAND_NOT_FOUND;

		if (!command.shouldReactToMentionPrefix() && byMention) {
			return CommandResult.MENTION_PREFIX_NO_REACT;
		} else if (reactToWebhooks != MessageReactionBehavior.REACT_ALWAYS && event.isWebhookMessage() && (!command.shouldReactToWebhooks() || reactToWebhooks == MessageReactionBehavior.REACT_NEVER)) {
			return CommandResult.WEBHOOK_MESSAGE_NO_REACT;
		} else if (reactToBots != MessageReactionBehavior.REACT_ALWAYS && event.getAuthor().isBot() && (!command.shouldReactToBots() || reactToBots == MessageReactionBehavior.REACT_NEVER)) {
			return CommandResult.BOT_MESSAGE_NO_REACT;
		} else if (command.getType() != null && command.getType() == CommandType.GUILD && !event.isFromGuild()) {
			return CommandResult.INVALID_CHANNEL_GUILD_COMMAND;
		} else if (command.getType() != null && command.getType() == CommandType.PRIVATE && event.isFromGuild()) {
			return CommandResult.INVALID_CHANNEL_PRIVATE_COMMAND;
		}

		process(command, new CommandEvent(prefix, getCommandName(command, commandName), event));
		return CommandResult.SUCCESS;

	}

	private String mention(MessageReceivedEvent event) {
		return "<@!" + event.getJDA().getSelfUser().getId() + ">";
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

	public static final ThreadGroup THREAD_GROUP = new ThreadGroup("CommandProcessGroup");
	private static final UncaughtExceptionHandler EXCEPTION_HANDLER = new ExceptionHandler();

	private static void process(ICommand command, CommandEvent event) {
		if (!command.shouldProcessInNewThread()) {
			command.onCommand(event);
		} else {
			Thread thread = new Thread(THREAD_GROUP, () -> command.onCommand(event), "CommandProcess-" + (THREAD_GROUP.activeCount()+1));
			thread.setUncaughtExceptionHandler(EXCEPTION_HANDLER);
			thread.start();
		}
	}

	private static class ExceptionHandler implements UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread thread, Throwable exception) {
			System.err.println("[" + thread.getName() + "] One of your command generated an exception: " + exceptionMessage(exception));
		}

		private static String exceptionMessage(Throwable exception) {

			StringBuilder builder = new StringBuilder();
			builder.append(exception.getMessage());

			for (StackTraceElement currentTraceElement : exception.getStackTrace()) {
				builder.append("\nat " + currentTraceElement.toString());
			}

			return builder.toString();

		}

	}

}
