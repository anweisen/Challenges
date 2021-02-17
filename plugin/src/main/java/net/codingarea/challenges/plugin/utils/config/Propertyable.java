package net.codingarea.challenges.plugin.utils.config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface Propertyable {

	@Nullable
	Object getObject(@Nonnull String path);

	@Nullable
	String getString(@Nonnull String path);

	@Nonnull
	String getString(@Nonnull String path, @Nonnull String def);

	char getChar(@Nonnull String path);

	long getLong(@Nonnull String path);

	int getInt(@Nonnull String path);

	short getShort(@Nonnull String path);

	byte getByte(@Nonnull String path);

	float getFloat(@Nonnull String path);

	double getDouble(@Nonnull String path);

	boolean getBoolean(@Nonnull String path);

	@Nonnull
	List<String> getList(@Nonnull String path);

	boolean contains(@Nonnull String path);

	@Nonnull
	Map<String, Object> values();

	@Nonnull
	Collection<String> keys();

	@Nonnull
	Propertyable set(@Nonnull String path, @Nullable Object value);

}
