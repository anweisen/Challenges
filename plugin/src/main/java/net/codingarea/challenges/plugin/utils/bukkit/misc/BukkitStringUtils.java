package net.codingarea.challenges.plugin.utils.bukkit.misc;

import net.anweisen.utilities.common.collection.WrappedException;
import net.anweisen.utilities.common.logging.ILogger;
import net.codingarea.challenges.plugin.content.Prefix;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class BukkitStringUtils {

	@Nonnull
	public static BaseComponent[] format(@Nonnull Prefix prefix, @Nonnull String[] array, @Nonnull Object... args) {
		List<BaseComponent> results = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			String s = array[i];
			if (!s.isEmpty()) {
				s = prefix + s;
			}
			BaseComponent comp = null;
			for (BaseComponent component : format(s, args)) {
				if (comp == null) {
					comp = component;
				} else {
					comp.addExtra(component);
				}
			}
			results.add(comp);
		}
		return results.toArray(new BaseComponent[0]);
	}

	@Nonnull
	public static List<BaseComponent> format(@Nonnull String sequence, @Nonnull Object... args) {
		List<BaseComponent> results = new ArrayList<>();
		char start = '{', end = '}';
		boolean inArgument = false;

		boolean lastWasParagraph = false;
		ChatColor currentColor = null;
		List<ChatColor> currentFormatting = new LinkedList<>();

		StringBuilder argument = new StringBuilder();
		TextComponent currentText = new TextComponent();
		for (char c : sequence.toCharArray()) {

			if (c == '§') {
				lastWasParagraph = true;
			} else {
				if (lastWasParagraph) {
					ChatColor newColor = ChatColor.getByChar(c);
					if (!newColor.isColor()) {
						if (newColor == ChatColor.RESET) {
							currentFormatting.clear();
							currentColor = null;
						} else {
							currentFormatting.add(newColor);
						}
					} else {
						currentColor = newColor;
						currentFormatting.clear();
					}
				}
				lastWasParagraph = false;
			}

			if (c == end && inArgument) {
				inArgument = false;
				try {
					int arg = Integer.parseInt(argument.toString());
					Object current = args[arg];
					BaseComponent replacement =
							current instanceof BaseComponent ? (BaseComponent) current :
							current instanceof Material ? new TranslatableComponent((((Material) current).isBlock() ? "block" : "item") + "." + ((Material) current).getKey().getNamespace() + "." + ((Material) current).getKey().getKey()) :
							current instanceof EntityType ? new TranslatableComponent("entity." + ((EntityType) current).getKey().getNamespace() + "." + ((EntityType) current).getKey().getKey()) :
							current instanceof Supplier ? new TextComponent(String.valueOf(((Supplier<?>)current).get())) :
									current instanceof Callable ? new TextComponent(String.valueOf(((Callable<?>)current).call())) :
											new TextComponent(String.valueOf(current));
					results.add(currentText);
					currentText = new TextComponent();
					TextComponent e = new TextComponent("§e");

					if (currentColor != null) {
						if (replacement instanceof TextComponent) {
							replacement = new TextComponent("§" + currentColor.getChar() + ((TextComponent) replacement).getText());
						} else {
							replacement.setColor(currentColor.asBungee());
						}
						currentColor = null;
					}

					e.addExtra(replacement);
					results.add(e);
				} catch (NumberFormatException | IndexOutOfBoundsException ex) {
					ILogger.forThisClass().warn("Invalid argument index '{}'", argument);
					results.add(new TextComponent(String.valueOf(start)));
					results.add(new TextComponent(String.valueOf(argument)));
					results.add(new TextComponent(String.valueOf(end)));
				} catch (Exception ex) {
					throw new WrappedException(ex);
				}
				argument = new StringBuilder();
				continue;
			}
			if (c == start && !inArgument) {
				inArgument = true;
				continue;
			}
			if (inArgument) {
				argument.append(c);
				continue;
			}
			currentText = new TextComponent(currentText.getText() + c);
		}
		if (!currentText.getText().isEmpty()) {
			results.add(new TextComponent(currentText));
		}
		if (argument.length() > 0) {
			results.add(new TextComponent(String.valueOf(start)));
			results.add(new TextComponent(String.valueOf(argument)));
		}
		return results;
	}

}
