package net.codingarea.challenges.plugin.content.impl;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.anweisen.utilities.bukkit.utils.logging.Logger;
import net.anweisen.utilities.common.collection.IRandom;
import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.content.ItemDescription;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
		if (value instanceof String && args.length == 0) return (String) value;
		if (value instanceof String)            return StringUtils.format((String) value, args);
		if (value instanceof String[] && args.length == 0) return StringUtils.getArrayAsString((String[]) value, "\n");
		if (value instanceof String[])          return StringUtils.getArrayAsString(StringUtils.format((String[]) value, args), "\n");
		if (value instanceof ItemDescription)   return ((ItemDescription)value).getName();
		Logger.error("Message '{}' has an illegal value {}", name, value.getClass().getName());
		return Message.NULL;
	}

	@Nonnull
	@Override
	public String asRandomString(@Nonnull Object... args) {
		return asRandomString(defaultRandom(), args);
	}

	@Nonnull
	@Override
	public String asRandomString(@Nonnull IRandom random, @Nonnull Object... args) {
		String[] array = asArray(args);
		if (array.length == 0) return Message.unknown(name);
		return random.choose(array);
	}

	@Nonnull
	@Override
	public String[] asArray(@Nonnull Object... args) {
		if (value == null)                      return new String[] { Message.unknown(name) };
		if (value instanceof String[])          return StringUtils.format((String[]) value, args);
		if (value instanceof String)            return StringUtils.getStringAsArray(StringUtils.format((String) value, args));
		if (value instanceof ItemDescription)   return ((ItemDescription)value).getLore();
		Logger.error("Message '{}' has an illegal value {}", name, value.getClass().getName());
		return new String[] { Message.NULL };
	}

	@Nonnull
	@Override
	public ItemDescription asItemDescription(@Nonnull Object... args) {
		if (value == null)                      { Message.unknown(name); return ItemDescription.empty(); }
		if (value instanceof ItemDescription)   return (ItemDescription) value;
		return new ItemDescription(asArray(args));
	}

	@Override
	public void send(@Nonnull CommandSender target, @Nonnull Prefix prefix, @Nonnull Object... args) {
		doSendLines(target::sendMessage, prefix, asArray(args));
	}

	@Override
	public void sendRandom(@Nonnull CommandSender target, @Nonnull Prefix prefix, @Nonnull Object... args) {
		sendRandom(defaultRandom(), target, prefix, args);
	}

	@Override
	public void sendRandom(@Nonnull IRandom random, @Nonnull CommandSender target, @Nonnull Prefix prefix, @Nonnull Object... args) {
		doSendLine(target::sendMessage, prefix, asRandomString(random, args));
	}

	@Override
	public void broadcast(@Nonnull Prefix prefix, @Nonnull Object... args) {
		doSendLines(Bukkit::broadcastMessage, prefix, asArray(args));
	}

	@Override
	public void broadcastRandom(@Nonnull Prefix prefix, @Nonnull Object... args) {
		broadcastRandom(defaultRandom(), prefix, args);
	}

	@Override
	public void broadcastRandom(@Nonnull IRandom random, @Nonnull Prefix prefix, @Nonnull Object... args) {
		doSendLine(Bukkit::broadcastMessage, prefix, asRandomString(random, args));
	}

	private void doSendLines(@Nonnull Consumer<? super String> sender, @Nonnull Prefix prefix, @Nonnull String[] lines) {
		for (String line : lines) {
			doSendLine(sender, prefix, line);
		}
	}

	private void doSendLine(@Nonnull Consumer<? super String> sender, @Nonnull Prefix prefix, @Nonnull String line) {
		if (line.trim().isEmpty()) sender.accept(line);
		else                       sender.accept(prefix + line);
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
		this.value = value;
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

	@Nonnull
	protected static IRandom defaultRandom() {
		return IRandom.threadLocal();
	}

}
