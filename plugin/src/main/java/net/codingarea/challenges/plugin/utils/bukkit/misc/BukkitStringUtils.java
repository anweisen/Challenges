package net.codingarea.challenges.plugin.utils.bukkit.misc;

import net.anweisen.utilities.common.collection.WrappedException;
import net.anweisen.utilities.common.logging.ILogger;
import net.codingarea.challenges.plugin.content.Prefix;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
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
		for (String value : array) {
			String s = value;
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

		args = replaceArgumentStrings(args, false);

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
									current instanceof Supplier ? new TextComponent(String.valueOf(((Supplier<?>) current).get())) :
											current instanceof Callable ? new TextComponent(String.valueOf(((Callable<?>) current).call())) :
													new TextComponent(String.valueOf(current));
					results.add(currentText);
					currentText = new TextComponent();
					TextComponent e = new TextComponent("§e");

					if (currentColor != null) {
						if (replacement instanceof TextComponent) {
							String prefix = "§" + currentColor.getChar();
							for (ChatColor color : currentFormatting) {
								prefix += "§" + color.getChar();
							}
							replacement = new TextComponent(prefix + ((TextComponent) replacement).getText());
						} else {
							replacement.setColor(currentColor.asBungee());
							for (ChatColor color : currentFormatting) {
								switch (color) {
									case BOLD:
										replacement.setBold(true);
										break;
									case MAGIC:
										replacement.setObfuscated(true);
										break;
									case ITALIC:
										replacement.setItalic(true);
										break;
									case STRIKETHROUGH:
										replacement.setStrikethrough(true);
										break;
									case UNDERLINE:
										replacement.setUnderlined(true);
										break;
								}
							}
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

	public static Object[] replaceArgumentStrings(Object[] args, boolean toStrings) {
		args = Arrays.copyOf(args, args.length);
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];

			args[i] = arg instanceof Material ? getItemName((Material) arg) :
					arg instanceof EntityType ? getEntityName((EntityType) arg) :
					arg instanceof PotionEffectType ? getPotionEffectName((PotionEffectType) arg) :
					arg instanceof GameMode ? getGameModeName((GameMode) arg) :
					arg instanceof Advancement ? getAdvancementComponent((Advancement) arg) :
					arg;

			if (toStrings) {
				if (arg instanceof BaseComponent) {
					args[i] = ((BaseComponent) arg).toPlainText();
				}
			}

		}
		return args;
	}

	public static TranslatableComponent getItemName(@Nonnull Material material) {
		NamespacedKey key = material.getKey();
		return new TranslatableComponent((material.isBlock() ? "block" : "item") + "." + key.getNamespace() + "." + key.getKey());
	}

	public static TranslatableComponent getEntityName(@Nonnull EntityType type) {
		NamespacedKey key = type.getKey();
		return new TranslatableComponent("entity." + key.getNamespace() + "." + key.getKey());
	}

	public static TranslatableComponent getPotionEffectName(@Nonnull PotionEffectType type) {
		NamespacedKey key = type.getKey();
		return new TranslatableComponent("effect." + key.getNamespace() + "." + key.getKey());
	}

	public static TranslatableComponent getGameModeName(@Nonnull GameMode gameMode) {
		return new TranslatableComponent("selectWorld.gameMode." + gameMode.name().toLowerCase());
	}

	public static BaseComponent getAdvancementTitle(@Nonnull Advancement advancement) {
		String replace = advancement.getKey().getKey().replace("/", ".");
		return new TranslatableComponent("advancements." + correctAdvancementKeys(replace) + ".title");
	}

	public static BaseComponent getAdvancementDescription(@Nonnull Advancement advancement) {
		String replace = advancement.getKey().getKey().replace("/", ".");
		return new TranslatableComponent("advancements." + correctAdvancementKeys(replace) + ".description");
	}

	public static BaseComponent getAdvancementComponent(@Nonnull Advancement advancement) {
		BaseComponent title = getAdvancementTitle(advancement);
		BaseComponent description = getAdvancementDescription(advancement);
		description.setColor(net.md_5.bungee.api.ChatColor.GREEN);
		title.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(description).create()));
		return title;
	}

	private static String correctAdvancementKeys(String s) {
		return s.replace("bred_all_animals", "breed_all_animals").replace("obtain_netherite_hoe", "netherite_hoe"); // mc sucks
	}

}
