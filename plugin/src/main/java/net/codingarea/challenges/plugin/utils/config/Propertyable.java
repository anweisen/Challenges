package net.codingarea.challenges.plugin.utils.config;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

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

	char getChar(@Nonnull String path, char def);

	long getLong(@Nonnull String path);

	long getLong(@Nonnull String path, long def);

	int getInt(@Nonnull String path);

	int getInt(@Nonnull String path, int def);

	short getShort(@Nonnull String path);

	short getShort(@Nonnull String path, short def);

	byte getByte(@Nonnull String path);

	byte getByte(@Nonnull String path, byte def);

	float getFloat(@Nonnull String path);

	float getFloat(@Nonnull String path, float def);

	double getDouble(@Nonnull String path);

	double getDouble(@Nonnull String path, double def);

	boolean getBoolean(@Nonnull String path);

	boolean getBoolean(@Nonnull String path, boolean def);

	@Nonnull
	List<String> getList(@Nonnull String path);

	@Nullable
	UUID getUUID(@Nonnull String path);

	@Nonnull
	UUID getUUID(@Nonnull String path, @Nonnull UUID def);

	@Nullable
	Location getLocation(@Nonnull String path);

	@Nonnull
	Location getLocation(@Nonnull String path, @Nonnull Location def);

	@Nullable
	ItemStack getItemStack(@Nonnull String path);

	@Nonnull
	ItemStack getItemStack(@Nonnull String path, @Nonnull ItemStack def);

	@Nullable
	<E extends Enum<E>> E getEnum(@Nonnull String path, @Nonnull Class<E> classOfEnum);

	@Nonnull
	<E extends Enum<E>> E getEnum(@Nonnull String path, @Nonnull E def);

	boolean contains(@Nonnull String path);

	boolean isEmpty();

	@Nonnull
	Map<String, Object> values();

	@Nonnull
	Collection<String> keys();

	void forEach(@Nonnull BiConsumer<? super String, ? super Object> action);

}
