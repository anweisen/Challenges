package net.codingarea.discordstatsbot.commandmanager.commands;

import net.codingarea.discordstatsbot.commandmanager.CommandEvent;

/**
 * @author anweisen
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 */

public abstract class Command implements ICommand {

	public enum CommandType {
		GENERAL,
		PRIVATE,
		GUILD;
	}

	public static boolean REACT_TO_MENTION_PREFIX_DEFAULT = true;

	public Command(String name, String... alias) {
		this(name, false, alias);
	}

	public Command(String name, boolean processInNewThread, String... alias) {
		this.name = name;
		this.alias = alias;
		this.processInNewThread = processInNewThread;
	}

	public Command(String name, CommandType commandType, String... alias) {
		this(name, alias);
		this.type = commandType;
	}

	public Command(String name, CommandType commandType, boolean processInNewThread, String... alias) {
		this(name, processInNewThread, alias);
		this.type = commandType;
	}

	public Command(String name, CommandType commandType, boolean processInNewThread, boolean reactToMentionPrefix, String... alias) {
		this(name, commandType, processInNewThread, alias);
		this.reactToMentionPrefix = reactToMentionPrefix;
	}

	public Command(String name, boolean processInNewThread, boolean reactToMentionPrefix, String... alias) {
		this(name, processInNewThread, alias);
		this.reactToMentionPrefix = reactToMentionPrefix;
	}

	private final String name;
	private final String[] alias;

	private final boolean processInNewThread;

	private CommandType type = CommandType.GENERAL;
	private boolean reactToWebhooks = false;
	private boolean reactToBots = false;
	private boolean reactToMentionPrefix = REACT_TO_MENTION_PREFIX_DEFAULT;

	public abstract void onCommand(CommandEvent event);

	@Override
	public final CommandType getType() {
		return type;
	}

	@Override
	public final boolean shouldReactToWebhooks() {
		return reactToWebhooks;
	}

	@Override
	public final boolean shouldReactToBots() {
		return reactToBots;
	}

	@Override
	public final boolean shouldProcessInNewThread() {
		return processInNewThread;
	}

	@Override
	public final boolean shouldReactToMentionPrefix() {
		return reactToMentionPrefix;
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final String[] getAlias() {
		return alias;
	}

}
