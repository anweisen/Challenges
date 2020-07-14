package net.codingarea.challengesplugin.utils.commons;

import com.google.common.base.Preconditions;

import java.awt.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author anweisen
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 */

public class ChatColor {

	public static final char COLOR_CHAR = '§';
	public static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";
	public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + '§' + "[0-9A-FK-ORX]");

	private static final Map<Character, ChatColor> BY_CHAR = new HashMap();
	private static final Map<String, ChatColor> BY_NAME = new HashMap();

	public static final ChatColor BLACK = new ChatColor('0', "black");
	public static final ChatColor DARK_BLUE = new ChatColor('1', "dark_blue");
	public static final ChatColor DARK_GREEN = new ChatColor('2', "dark_green");
	public static final ChatColor DARK_AQUA = new ChatColor('3', "dark_aqua");
	public static final ChatColor DARK_RED = new ChatColor('4', "dark_red");
	public static final ChatColor DARK_PURPLE = new ChatColor('5', "dark_purple");
	public static final ChatColor GOLD = new ChatColor('6', "gold");
	public static final ChatColor GRAY = new ChatColor('7', "gray");
	public static final ChatColor DARK_GRAY = new ChatColor('8', "dark_gray");
	public static final ChatColor BLUE = new ChatColor('9', "blue");
	public static final ChatColor GREEN = new ChatColor('a', "green");
	public static final ChatColor AQUA = new ChatColor('b', "aqua");
	public static final ChatColor RED = new ChatColor('c', "red");
	public static final ChatColor LIGHT_PURPLE = new ChatColor('d', "light_purple");
	public static final ChatColor YELLOW = new ChatColor('e', "yellow");
	public static final ChatColor WHITE = new ChatColor('f', "white");
	public static final ChatColor MAGIC = new ChatColor('k', "obfuscated");
	public static final ChatColor BOLD = new ChatColor('l', "bold");
	public static final ChatColor STRIKETHROUGH = new ChatColor('m', "strikethrough");
	public static final ChatColor UNDERLINE = new ChatColor('n', "underline");
	public static final ChatColor ITALIC = new ChatColor('o', "italic");
	public static final ChatColor RESET = new ChatColor('r', "reset");

	private static int count = 0;
	private final String toString;
	private final String name;
	private final int ordinal;

	private ChatColor(char code, String name) {
		this.name = name;
		this.toString = new String(new char[]{'§', code});
		this.ordinal = count++;
		BY_CHAR.put(code, this);
		BY_NAME.put(name.toUpperCase(Locale.ROOT), this);
	}

	private ChatColor(String name, String toString) {
		this.name = name;
		this.toString = toString;
		this.ordinal = -1;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj != null && this.getClass() == obj.getClass()) {
			ChatColor other = (ChatColor)obj;
			return Objects.equals(this.toString, other.toString);
		} else {
			return false;
		}
	}

	public String toString() {
		return this.toString;
	}

	public static String stripColor(String input) {
		return input == null ? null : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
	}

	public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {

		char[] chars = textToTranslate.toCharArray();
		for(int i = 0; i < chars.length - 1; ++i) {
			if (chars[i] == altColorChar && ALL_CODES.indexOf(chars[i + 1]) > -1) {
				chars[i] = 167;
				chars[i + 1] = Character.toLowerCase(chars[i + 1]);
			}
		}

		return new String(chars);
	}

	public static ChatColor getByChar(char code) {
		return BY_CHAR.get(code);
	}

	public static ChatColor of(Color color) {
		return of("#" + Integer.toHexString(color.getRGB()).substring(2));
	}

	public static ChatColor of(String string) {

		Preconditions.checkArgument(string != null, "string cannot be null");
		if (string.startsWith("#") && string.length() == 7) {

			try {
				Integer.parseInt(string.substring(1), 16);
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Illegal hex string " + string);
			}

			StringBuilder magic = new StringBuilder("§x");
			char[] chars = string.substring(1).toCharArray();

			for (char currentChar : chars) {
				magic.append('§').append(currentChar);
			}

			return new ChatColor(string, magic.toString());

		} else {

			ChatColor defined = BY_NAME.get(string.toUpperCase(Locale.ROOT));
			if (defined != null) {
				return defined;
			} else {
				throw new IllegalArgumentException("Could not parse ChatColor " + string);
			}

		}

	}

	public static ChatColor valueOf(String name) {
		Preconditions.checkNotNull(name, "Name is null");
		ChatColor defined = BY_NAME.get(name);
		Preconditions.checkArgument(defined != null, "No enum constant " + ChatColor.class.getName() + "." + name);
		return defined;
	}

	public static ChatColor[] values() {
		return BY_CHAR.values().toArray(new ChatColor[BY_CHAR.values().size()]);
	}

	public String name() {
		return this.getName().toUpperCase(Locale.ROOT);
	}

	public int ordinal() {
		Preconditions.checkArgument(ordinal >= 0, "Cannot get ordinal of hex color");
		return ordinal;
	}

	public String getName() {
		return name;
	}

}
