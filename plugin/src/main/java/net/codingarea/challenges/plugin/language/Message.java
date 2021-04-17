package net.codingarea.challenges.plugin.language;

import net.codingarea.challenges.plugin.language.management.MessageManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Random;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface Message {

	String NULL = "§r§fN/A";

	@Nonnull
	String asString(@Nonnull Object... args);

	@Nonnull
	String asRandomString(@Nonnull Random random, @Nonnull Object... args);

	@Nonnull
	String asRandomString(@Nonnull Object... args);

	@Nonnull
	String[] asArray(@Nonnull Object... args);

	@Nonnull
	ItemDescription asItemDescription(@Nonnull Object... args);

	void send(@Nonnull CommandSender target, @Nonnull Prefix prefix, @Nonnull Object... args);
	void sendRandom(@Nonnull CommandSender target, @Nonnull Prefix prefix, @Nonnull Object... args);
	void sendRandom(@Nonnull Random random, @Nonnull CommandSender target, @Nonnull Prefix prefix, @Nonnull Object... args);

	void broadcast(@Nonnull Prefix prefix, @Nonnull Object... args);
	void broadcastRandom(@Nonnull Prefix prefix, @Nonnull Object... args);
	void broadcastRandom(@Nonnull Random random, @Nonnull Prefix prefix, @Nonnull Object... args);

	void broadcastTitle(@Nonnull Object... args);
	void sendTitle(@Nonnull Player player, @Nonnull Object... args);
	void sendTitleInstant(@Nonnull Player player, @Nonnull Object... args);

	void setValue(@Nonnull String value);
	void setValue(@Nonnull String[] value);

	@Nonnull
	String getName();

	@Nonnull
	@CheckReturnValue
	static Message forName(@Nonnull String name) {
		return MessageManager.getOrCreateMessage(name);
	}

}
