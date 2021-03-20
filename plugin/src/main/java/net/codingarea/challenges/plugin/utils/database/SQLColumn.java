package net.codingarea.challenges.plugin.utils.database;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class SQLColumn {

	private final String name;
	private final String type;
	private final int length;

	public SQLColumn(@Nonnull String name, @Nonnull String type, int length) {
		if (name.contains(" ")) throw new IllegalArgumentException("Column name cannot contain spaces");
		if (type.contains(" ")) throw new IllegalArgumentException("Column type cannot contain spaces");
		if (length < 1) throw new IllegalArgumentException("Column length has to be greater than zero");

		this.name = name;
		this.type = type;
		this.length = length;
	}

	@Nonnull
	public String getName() {
		return name;
	}

	@Nonnull
	public String getType() {
		return type;
	}

	@Nonnegative
	public int getLength() {
		return length;
	}

}
