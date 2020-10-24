package net.codingarea.engine.utils;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2.2
 */
public class NamedValue {

	public static NamedValue[] ofStrings(String... values) {

		NamedValue[] namedValues = new NamedValue[values.length];
		for (int i = 0; i < values.length; i++) {
			namedValues[i] = new NamedValue(values[i]);
		}

		return namedValues;

	}

	protected final String key;
	protected String value;

	public NamedValue(@Nonnull String key, Object value) {
		this(key, value == null ? null : value.toString());
	}

	public NamedValue(@Nonnull String key, String value) {
		this.key = key;
		this.value = value;
	}

	public NamedValue(@Nonnull String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public String setValue(String value) {
		return this.value = value;
	}

	public String setValue(Object value) {
		return setValue(value == null ? null : value.toString());
	}

	@Nonnull
	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return "NamedValue{" +
				"key='" + key + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
