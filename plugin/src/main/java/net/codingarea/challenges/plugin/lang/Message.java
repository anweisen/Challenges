package net.codingarea.challenges.plugin.lang;

import net.codingarea.challenges.plugin.utils.misc.StringUtils;

import javax.annotation.CheckReturnValue;
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
	CHALLENGES_END_TIMER_HIT_ZERO,
	CHALLENGES_END_TIMER_HIT_ZERO_WINNER,
	CHALLENGES_END_GOAL_REACHED,
	CHALLENGES_END_GOAL_REACHED_WINNER,
	CHALLENGES_END_GOAL_FAILED,
	;

	private Object value;

	public void setValue(@Nonnull String value) {
		this.value = value.startsWith("§") ? value : "§7" + value;
	}

	public void setValue(@Nonnull String[] value) {
		this.value = value;
	}

	@Nonnull
	@CheckReturnValue
	public String asString(@Nonnull String... args) {
		if (value == null) return "§rN/A";
		if (value instanceof String) return StringUtils.format((String) value, args);
		if (value instanceof String[]) return StringUtils.getArrayAsString(StringUtils.format((String[]) value, args));
		throw new IllegalStateException();
	}

	@Nonnull
	@CheckReturnValue
	public String[] asArray(@Nonnull String... args) {
		if (value == null) return new String[] { "§rN/A" };
		if (value instanceof String[]) return StringUtils.format((String[]) value, args);
		if (value instanceof String) return StringUtils.getStringAsArray(StringUtils.format((String) value, args));
		throw new IllegalStateException();
	}

	@Nonnull
	@Override
	public String toString() {
		return asString();
	}

}
