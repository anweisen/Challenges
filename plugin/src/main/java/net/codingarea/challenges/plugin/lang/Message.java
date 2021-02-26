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
	CHALLENGES_END_TIMER_HIT_ZERO           (String[].class),
	CHALLENGES_END_TIMER_HIT_ZERO_WINNER    (String[].class),
	CHALLENGES_END_GOAL_REACHED             (String[].class),
	CHALLENGES_END_GOAL_REACHED_WINNER      (String[].class),
	CHALLENGES_END_GOAL_FAILED              (String[].class),
	;

	public static final String NULL_MESSAGE = "§r§fN/A";

	private final Class<?> target;

	Message() {
		this(String.class);
	}

	Message(@Nonnull Class<?> target) {
		this.target = target;
	}

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
		if (value == null)                      return NULL_MESSAGE;
		if (value instanceof String)            return StringUtils.format((String) value, args);
		if (value instanceof String[])          return StringUtils.getArrayAsString(StringUtils.format((String[]) value, args));
		if (value instanceof ItemDescription)   return ((ItemDescription)value).getName();
		Logger.severe("Message." + name() + " has an illegal value " + value.getClass().getName());
		return NULL_MESSAGE;
	}

	@Nonnull
	@CheckReturnValue
	public String[] asArray(@Nonnull String... args) {
		if (value == null)                      return new String[] { NULL_MESSAGE };
		if (value instanceof String[])          return StringUtils.format((String[]) value, args);
		if (value instanceof String)            return StringUtils.getStringAsArray(StringUtils.format((String) value, args));
		if (value instanceof ItemDescription)   return ((ItemDescription)value).getLore();
		Logger.severe("Message." + name() + " has an illegal value " + value.getClass().getName());
		return new String[] { NULL_MESSAGE };
	}

	private <T> T setIfWanted(@Nonnull T t) {
		if (target.isAssignableFrom(t.getClass()))
			value = t;
		return t;
	}

	@Nonnull
	@Override
	public String toString() {
		return asString();
	}

}
