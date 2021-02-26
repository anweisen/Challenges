package net.codingarea.challenges.plugin.lang;

import net.codingarea.challenges.plugin.utils.misc.StringUtils;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ItemDescription {

	private final String[] colors;
	private final String name;
	private final String[] lore;

	public static ItemDescription empty() {
		return new ItemDescription(new String[] {"§r§f"}, Message.NULL_MESSAGE, new String[0]);
	}

	public ItemDescription(@Nonnull String[] themeColors, @Nonnull String formattedName, @Nonnull String[] formattedLore) {
		this.colors = themeColors;
		this.name = formattedName;
		this.lore = formattedLore;
	}

	public ItemDescription(@Nonnull String[] description) {
		if (description.length == 0) throw new IllegalArgumentException("Invalid item description: Cannot be empty");

		name = description[0];
		colors = determineColors(name);
		lore = new String[description.length - 1];
		fillLore(description);
	}

	@Nonnull
	public String getName() {
		return name;
	}

	@Nonnull
	public String[] getTheme() {
		return colors;
	}

	@Nonnull
	public String[] getLore() {
		return lore;
	}

	private void fillLore(@Nonnull String[] origin) {



		// Start at 1, first entry is not content of lore
		for (int i = 1; i < origin.length; i++) {
			String line = lore[i];

		}

	}

	private String[] determineColors(@Nonnull String input) {
		List<String> colors = new LinkedList<>();
		int colorIndex = 0;

		boolean nextIsCode = false;
		for (char c : input.toCharArray()) {
			if (c == ChatColor.COLOR_CHAR) {
				nextIsCode = true;
				continue;
			}
			if (!nextIsCode) continue;
			nextIsCode = false;

			String latestColors = "";
			if (colors.size() > colorIndex)
				latestColors = colors.remove(colorIndex);

			colors.add(latestColors + ChatColor.COLOR_CHAR + c);

			if (StringUtils.isValidColorCode(c))
				colorIndex++;
		}

		return colors.toArray(new String[0]);
	}

}
