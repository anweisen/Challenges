package net.codingarea.challenges.plugin.language.management;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.ItemDescription;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import net.codingarea.challenges.plugin.utils.misc.RandomizeUtils;
import net.codingarea.challenges.plugin.utils.misc.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class MessageImpl implements Message {

	protected final String name;
	protected Object value;

	public MessageImpl(@Nonnull String name) {
		this.name = name;
	}

	@Nonnull
	@Override
	public String asString(@Nonnull Object... args) {
		if (value == null)                      return Message.NULL;
		if (value instanceof String)            return StringUtils.format((String) value, args);
		if (value instanceof String[])          return StringUtils.getArrayAsString(StringUtils.format((String[]) value, args), "\n");
		if (value instanceof ItemDescription)   return ((ItemDescription)value).getName();
		Logger.error("Message '{}' has an illegal value {}", name, value.getClass().getName());
		return Message.NULL;
	}

	@Nonnull
	@Override
	public String asRandomString(@Nonnull Object... args) {
		return asRandomString(ThreadLocalRandom.current(), args);
	}

	@Nonnull
	@Override
	public String asRandomString(@Nonnull Random random, @Nonnull Object... args) {
		String[] array = asArray(args);
		if (array.length == 0) return Message.NULL;
		return RandomizeUtils.choose(random, array);
	}

	@Nonnull
	@Override
	public String[] asArray(@Nonnull Object... args) {
		if (value == null)                      return new String[] { Message.NULL};
		if (value instanceof String[])          return StringUtils.format((String[]) value, args);
		if (value instanceof String)            return StringUtils.getStringAsArray(StringUtils.format((String) value, args));
		if (value instanceof ItemDescription)   return ((ItemDescription)value).getLore();
		Logger.error("Message '{}' has an illegal value {}", name, value.getClass().getName());
		return new String[] { Message.NULL};
	}

	@Nonnull
	@Override
	public ItemDescription asItemDescription(@Nonnull Object... args) {
		if (value == null)                      return ItemDescription.empty();
		if (value instanceof ItemDescription)   return (ItemDescription) value;
		return new ItemDescription(asArray(args));
	}

	@Override
	public void send(@Nonnull CommandSender target, @Nonnull Prefix prefix, @Nonnull Object... args) {
		for (String line : asArray(args)) {
			if (line.trim().isEmpty()) target.sendMessage(line);
			else                       target.sendMessage(prefix + line);
		}
	}

	@Override
	public void broadcast(@Nonnull Prefix prefix, @Nonnull Object... args) {
		for (String line : asArray(args)) {
			if (line.trim().isEmpty()) Bukkit.broadcastMessage(line);
			else                       Bukkit.broadcastMessage(prefix + line);
		}
	}

	@Override
	public void broadcastRandom(@Nonnull Prefix prefix, @Nonnull Object... args) {
		String line = asRandomString(args);
		if (line.trim().isEmpty()) Bukkit.broadcastMessage(line);
		else                       Bukkit.broadcastMessage(prefix + line);
	}

	@Override
	public void broadcastRandom(@Nonnull Random random, @Nonnull Prefix prefix, @Nonnull Object... args) {
		String line = asRandomString(random, args);
		if (line.trim().isEmpty()) Bukkit.broadcastMessage(line);
		else                       Bukkit.broadcastMessage(prefix + line);
	}

	@Override
	public void broadcastTitle(@Nonnull Object... args) {
		String[] title = asArray(args);
		Bukkit.getOnlinePlayers().forEach(player -> doSendTitle(player, title));
	}

	@Override
	public void sendTitle(@Nonnull Player player, @Nonnull Object... args) {
		doSendTitle(player, asArray(args));
	}

	@Override
	public void sendTitleInstant(@Nonnull Player player, @Nonnull Object... args) {
		doSendTitleInstant(player, asArray(args));
	}

	protected void doSendTitle(@Nonnull Player player, @Nonnull String[] title) {
		sendTitle(title, (line1, line2) -> Challenges.getInstance().getTitleManager().sendTitle(player, line1, line2));
	}

	protected void doSendTitleInstant(@Nonnull Player player, @Nonnull String[] title) {
		sendTitle(title, (line1, line2) -> Challenges.getInstance().getTitleManager().sendTitleInstant(player, line1, line2));
	}

	protected void sendTitle(@Nonnull String[] title, @Nonnull BiConsumer<String, String> send) {
		if (title.length == 0)      send.accept("", "");
		else if (title.length == 1) send.accept(title[0], "");
		else                        send.accept(title[0], title[1]);
	}

	@Override
	public void setValue(@Nonnull String value) {
		this.value = value.startsWith("§") || value.startsWith("{") ? value : "§7" + value;
	}

	@Override
	public void setValue(@Nonnull String[] value) {
		for (int i = 0; i < value.length; i++) {
			if (value[i] != null && !value[i].startsWith("§") && !value[i].startsWith("{") && !value[i].trim().isEmpty())
				value[i] = "§7" + value[i];
		}
		this.value = value;
	}

	@Nonnull
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return asString();
	}

}
