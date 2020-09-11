package net.codingarea.discordstatsbot.commandmanager.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author anweisen
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 */

public class CommandEvent {

	private final MessageReceivedEvent receivedEvent;
	private final String[] args;
	private final String prefix;
	private final String commandName;

	public CommandEvent(String prefix, String commandName, MessageReceivedEvent event) {

		receivedEvent = event;

		this.prefix = prefix;
		this.commandName = commandName;

		String raw = event.getMessage().getContentRaw().trim();

		if (raw.length() == (prefix + commandName).length()) {
			args = new String[0];
		} else {
			String argsRaw = raw.substring((prefix + commandName).length()).trim();
			args = argsRaw.split(" ");
		}

	}

	public ChannelType getChannelType() {
		return receivedEvent.getChannelType();
	}

	public String getCommandName() {
		return commandName;
	}

	public String getPrefix() {
		return prefix;
	}

	public String[] getArgs() {
		return args;
	}

	public String getArgsAsString() {
		StringBuilder builder = new StringBuilder();
		for (String currentArg : getArgs()) {
			builder.append(currentArg + " ");
		}
		return builder.toString().trim();
	}

	public String getMessageContentRaw() {
		return receivedEvent.getMessage().getContentRaw();
	}

	public Message getMessage() {
		return receivedEvent.getMessage();
	}

	public MessageReceivedEvent getReceivedEvent() {
		return receivedEvent;
	}

	public Guild getGuild() {
		return receivedEvent.getGuild();
	}

	public Member getMember() {
		return receivedEvent.getMember();
	}

	public User getUser() {
		return receivedEvent.getAuthor();
	}

	public JDA getJDA() {
		return receivedEvent.getJDA();
	}

	public boolean isWebhookMessage() {
		return receivedEvent.isWebhookMessage();
	}

	public MessageChannel getChannel() {
		return receivedEvent.getChannel();
	}

	public TextChannel getTextChannel() {
		return receivedEvent.getTextChannel();
	}

	public PrivateChannel getPrivateChannel() {
		return receivedEvent.getPrivateChannel();
	}

	public String getMessageID() {
		return receivedEvent.getMessageId();
	}

	public String getMemberID() {
		return receivedEvent.getMember().getId();
	}

	public String getMemberAvatarURL() {
		return receivedEvent.getAuthor().getEffectiveAvatarUrl();
	}

	public String getMemberName() {
		return receivedEvent.isFromGuild() ? receivedEvent.getMember().getEffectiveName() : receivedEvent.getAuthor().getName();
	}

	public boolean isPrivate() {
		return !receivedEvent.isFromGuild();
	}

	public boolean isFromGuild() {
		return receivedEvent.isFromGuild();
	}

	public void queueReply(MessageEmbed message) {
		reply(message).queue(ignored -> {}, exception -> {});
	}

	public void queueReply(CharSequence message) {
		reply(message).queue(ignored -> {}, exception -> {});
	}

	public void queueReply(MessageEmbed message, Consumer<Message> messageConsumer) {
		reply(message).queue(messageConsumer, exception -> {});
	}

	public void queueReply(CharSequence message, Consumer<Message> messageConsumer) {
		reply(message).queue(messageConsumer, exception -> {});
	}

	public MessageAction reply(MessageEmbed message) {
		return getChannel().sendMessage(message);
	}

	public MessageAction reply(CharSequence message) {
		return getChannel().sendMessage(message);
	}

	public void replyPrivate(MessageEmbed embed) {
		getUser().openPrivateChannel().queue(channel -> {
			channel.sendMessage(embed).queue();
		});
	}

	public void replyPrivate(CharSequence sequence) {
		getUser().openPrivateChannel().queue(channel -> {
			channel.sendMessage(sequence).queue();
		});
	}

	public List<Member> getMentionedMembers() {
		return receivedEvent.getMessage().getMentionedMembers();
	}

	public List<TextChannel> getMentionedChannels() {
		return receivedEvent.getMessage().getMentionedChannels();
	}

	public List<Role> getMentionedRoles() {
		return receivedEvent.getMessage().getMentionedRoles();
	}

	public boolean senderHasPermission(Permission... permission) {
		return receivedEvent.getMember().hasPermission(permission);
	}

	public String getArg(int i) {
		return args[i];
	}

	public static boolean containsMention(String text) {

		char[] goal = {'<','@','!','n','n','n','n','n','n','n','n','n','n','n','n','n','n','n','n','n','n','>'};
		int current = 0;
		for (char currentChar : text.toCharArray()) {

			boolean isInMention = false;
			char expected = goal[current];

			if (currentChar == expected) {
				isInMention = true;
			} else if (expected == 'n') {
				try {
					Integer.parseInt(String.valueOf(current));
					isInMention = true;
				} catch (NumberFormatException ignored) { }
			}

			if (isInMention) {
				current++;
			} else {
				current = 0;
			}

			if (current == goal.length) return true;

		}

		return false;

	}

	public static String syntax(CommandEvent event, String syntax) {
		return syntax(event, syntax, true);
	}

	public static String syntax(CommandEvent event, String syntax, boolean command) {
		String message = event.getPrefix() + (command ? event.getCommandName() + " " : "") + syntax;
		boolean mark = !containsMention(message);
		return (mark ? "`" : "*") + message + (mark ? "`" : "*");
	}

}
