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

	public Command(String name, String... alias) {
		this.name = name;
		this.alias = alias;
	}

	protected String name;
	protected String[] alias;

	protected CommandType type = CommandType.GENERAL;
	protected boolean reactToWebhooks = false;
	protected boolean reactToBots = false;

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
	public final String getName() {
		return name;
	}

	@Override
	public final String[] getAlias() {
		return alias;
	}

}
