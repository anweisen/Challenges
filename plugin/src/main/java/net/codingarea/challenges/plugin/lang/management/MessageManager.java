package net.codingarea.challenges.plugin.lang.management;

import net.codingarea.challenges.plugin.lang.Message;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class MessageManager {

	private static final Map<String, Message> cache = new ConcurrentHashMap<>();

	private MessageManager() {
	}

	@Nonnull
	@CheckReturnValue
	public static Message forName(@Nonnull String name) {
		return cache.computeIfAbsent(name, key -> new MessageImpl(key));
	}

}
