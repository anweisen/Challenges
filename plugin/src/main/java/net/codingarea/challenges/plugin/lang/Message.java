package net.codingarea.challenges.plugin.lang;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public enum Message {

	SYNTAX,
	PLAYER_COMMAND,
	NAVIGATE_BACK,
	NAVIGATE_NEXT,
	SECONDS,
	SECOND,
	MINUTES,
	MINUTE,
	HOURS,
	HOUR,
	TIMER_COUNTING_UP,
	TIMER_COUNTING_DOWN,
	;

	private String value;

	Message() {
	}

	public void setValue(@Nonnull String value) {
		this.value = value.startsWith("§") ? value : "§7" + value;
	}

	@Nonnull
	public String toString(@Nonnull String... args) {

		char start = '{', end = '}';

		boolean in = false;
		StringBuilder argument = new StringBuilder();
		StringBuilder builder = new StringBuilder();
		for (char c : toString().toCharArray()) {

			if (c == end && in) {
				in = false;
				int arg = Integer.parseInt(argument.toString());
				builder.append(args[arg]);
				continue;
			}
			if (c == start && !in) {
				in = true;
				continue;
			}
			if (in) {
				argument.append(c);
				continue;
			}

			builder.append(c);

		}

		return builder.toString();

	}

	@Nonnull
	@Override
	public String toString() {
		return value == null ? "N/A" : value;
	}

}
