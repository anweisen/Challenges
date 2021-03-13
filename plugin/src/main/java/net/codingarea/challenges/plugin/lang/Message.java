package net.codingarea.challenges.plugin.lang;

import net.codingarea.challenges.plugin.lang.management.MessageManager;
import org.bukkit.command.CommandSender;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface Message {

	String NULL = "§r§fN/A";

	@Nonnull
	String asString(@Nonnull Object... args);

	@Nonnull
	String[] asArray(@Nonnull Object... args);

	@Nonnull
	ItemDescription asItemDescription(@Nonnull Object... args);

	void send(@Nonnull CommandSender player, @Nonnull Prefix prefix, @Nonnull Object... args);

	void broadcast(@Nonnull Prefix prefix, @Nonnull Object... args);

	void broadcastTitle(@Nonnull Object... args);

	void setValue(@Nonnull String value);
	void setValue(@Nonnull String[] value);

	@Nonnull
	String getName();

	@Nonnull
	@CheckReturnValue
	static Message forName(@Nonnull String name) {
		return MessageManager.forName(name);
	}

}
