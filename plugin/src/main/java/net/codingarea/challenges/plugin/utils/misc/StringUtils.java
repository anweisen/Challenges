package net.codingarea.challenges.plugin.utils.misc;

import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class StringUtils {

	private StringUtils() {
	}

	@Nonnull
	public static String getEnumName(@Nonnull Enum<?> enun) {
		StringBuilder builder = new StringBuilder();
		boolean lastWasSpace = true;
		for (char letter : enun.name().toCharArray()) {
			// Replace _ with space
			if (letter == '_') {
				builder.append(' ');
				lastWasSpace = true;
				continue;
			}
			builder.append(lastWasSpace ? Character.toUpperCase(letter) : Character.toLowerCase(letter));
			lastWasSpace = false;
		}

		String name = builder.toString();
		String[] lowerCase = {"And", "Or", "If", "But", "It", "He", "She", "It", "We", "Are", "Is"};
		for (String sequence : lowerCase) {
			name = name.replace(sequence + " ", sequence.toLowerCase() + " ")
					.replace(" " + sequence, " " + sequence.toLowerCase());
		}

		return name;
	}

	@Nonnull
	public static String format(@Nonnull String sequence, @Nonnull Object... args) {
		char start = '{', end = '}';
		boolean inArgument = false;
		StringBuilder argument = new StringBuilder();
		StringBuilder builder = new StringBuilder();
		for (char c : sequence.toCharArray()) {
			if (c == end && inArgument) {
				inArgument = false;
				try {
					int arg = Integer.parseInt(argument.toString());
					builder.append(args[arg]);
				} catch (NumberFormatException | IndexOutOfBoundsException ex) {
					Logger.warn("Invalid argument index '" + argument + "'");
					builder.append(start).append(argument).append(end);
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
			builder.append(c);
		}
		return builder.toString();
	}

	@Nonnull
	public static String[] format(@Nonnull String[] array, @Nonnull Object... args) {
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = format(array[i], args);
		}
		return result;
	}

	@Nonnull
	public static String getArrayAsString(@Nonnull String[] array) {
		StringBuilder builder = new StringBuilder();
		for (String string : array) {
			if (builder.length() != 0) builder.append('\n');
			builder.append(string);
		}
		return builder.toString();
	}

	@Nonnull
	public static String[] getStringAsArray(@Nonnull String string) {
		return string.split("\n");
	}

	@Nonnull
	public static <T> String getIterableAsString(@Nonnull Iterable<T> iterable, @Nonnull Function<T, String> mapper) {
		StringBuilder builder = new StringBuilder();
		for (T t : iterable) {
			if (builder.length() > 0) builder.append(", ");
			String string = mapper.apply(t);
			builder.append(string);
		}
		return builder.toString();
	}

	@Nonnull
	public static String repeat(@Nullable Object sequence, int amount) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < amount; i++) builder.append(sequence);
		return builder.toString();
	}

	public static boolean isValidColorCode(char code) {
		for (ChatColor color : ChatColor.values()) {
			if (color.isColor() && color.getChar() == code)
				return true;
		}
		return false;
	}

	public static boolean isValidColorCode(@Nonnull String code) {
		if (code.length() != 1) return false;
		return isValidColorCode(code.toCharArray()[0]);
	}

}
