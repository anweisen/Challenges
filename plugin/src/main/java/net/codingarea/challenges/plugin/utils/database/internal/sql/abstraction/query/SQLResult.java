package net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.query;

import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.config.document.EmptyDocument;
import net.codingarea.challenges.plugin.utils.config.document.GsonDocument;
import net.codingarea.challenges.plugin.utils.config.document.readonly.ReadOnlyGsonDocument;
import net.codingarea.challenges.plugin.utils.database.Result;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class SQLResult implements Result {

	private final Map<String, Object> values;

	public SQLResult(@Nonnull Map<String, Object> values) {
		this.values = values;
	}

	@Nullable
	@Override
	public Object getObject(@Nonnull String path) {
		return values.get(path);
	}

	@Nullable
	@Override
	public String getString(@Nonnull String path) {
		Object value = getObject(path);
		return value == null ? null : value.toString();
	}

	@Nonnull
	@Override
	public String getString(@Nonnull String path, @Nonnull String def) {
		String value = getString(path);
		return value == null ? def : value;
	}

	@Override
	public char getChar(@Nonnull String path) {
		return getChar(path, (char) 0);
	}

	@Override
	public char getChar(@Nonnull String path, char def) {
		try {
			return getString(path).charAt(0);
		} catch (Exception ex) {
			return def;
		}
	}

	@Override
	public long getLong(@Nonnull String path) {
		return getLong(path, 0);
	}

	@Override
	public long getLong(@Nonnull String path, long def) {
		try {
			return Long.parseLong(getString(path));
		} catch (Exception ex) {
			return def;
		}
	}

	@Override
	public int getInt(@Nonnull String path) {
		return getInt(path, 0);
	}

	@Override
	public int getInt(@Nonnull String path, int def) {
		try {
			return Integer.parseInt(getString(path));
		} catch (Exception ex) {
			return def;
		}
	}

	@Override
	public short getShort(@Nonnull String path) {
		return getShort(path, (short) 0);
	}

	@Override
	public short getShort(@Nonnull String path, short def) {
		try {
			return Short.parseShort(getString(path));
		} catch (Exception ex) {
			return def;
		}
	}

	@Override
	public byte getByte(@Nonnull String path) {
		return getByte(path, (byte) 0);
	}

	@Override
	public byte getByte(@Nonnull String path, byte def) {
		try {
			return Byte.parseByte(getString(path));
		} catch (Exception ex) {
			return def;
		}
	}

	@Override
	public float getFloat(@Nonnull String path) {
		return getFloat(path, 0);
	}

	@Override
	public float getFloat(@Nonnull String path, float def) {
		try {
			return Float.parseFloat(getString(path));
		} catch (Exception ex) {
			return def;
		}
	}

	@Override
	public double getDouble(@Nonnull String path) {
		return getDouble(path, 0);
	}

	@Override
	public double getDouble(@Nonnull String path, double def) {
		try {
			return Double.parseDouble(getString(path));
		} catch (Exception ex) {
			return def;
		}
	}

	@Override
	public boolean getBoolean(@Nonnull String path) {
		return getBoolean(path, false);
	}

	@Override
	public boolean getBoolean(@Nonnull String path, boolean def) {
		try {
			return Boolean.getBoolean(getString(path));
		} catch (Exception ex) {
			return def;
		}
	}

	@Nonnull
	@Override
	public List<String> getList(@Nonnull String path) {
		throw new UnsupportedOperationException("SQLResult.getList(String)");
	}


	@Nullable
	@Override
	public UUID getUUID(@Nonnull String path) {
		try {
			return UUID.fromString(getString(path));
		} catch (Exception ex) {
			return null;
		}
	}

	@Nonnull
	@Override
	public UUID getUUID(@Nonnull String path, @Nonnull UUID def) {
		UUID value = getUUID(path);
		return value == null ? def : value;
	}

	@Nullable
	@Override
	public <E extends Enum<E>> E getEnum(@Nonnull String path, @Nonnull Class<E> classOfEnum) {
		return null;
	}

	@Nonnull
	@Override
	public <E extends Enum<E>> E getEnum(@Nonnull String path, @Nonnull E def) {
		E value = getEnum(path, (Class<E>) def.getClass());
		return value == null ? def : value;
	}

	@Override
	public boolean contains(@Nonnull String path) {
		return values.containsKey(path);
	}

	@Override
	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Nonnull
	@Override
	public Document getDocument(@Nonnull String path) {
		try {
			return new ReadOnlyGsonDocument(getString(path));
		} catch (Exception ex) {
			return new EmptyDocument();
		}
	}

	@Nullable
	@Override
	public ItemStack getItemStack(@Nonnull String path) {
		if (!contains(path)) return null;
		try {
			return ItemStack.deserialize(getDocument(path).values());
		} catch (Exception ex) {
			return null;
		}
	}

	@Nonnull
	@Override
	public ItemStack getItemStack(@Nonnull String path, @Nonnull ItemStack def) {
		ItemStack value = getItemStack(path);
		return value == null ? def : value;
	}

	@Nullable
	@Override
	public Location getLocation(@Nonnull String path) {
		if (!contains(path)) return null;
		try {
			return Location.deserialize(getDocument(path).values());
		} catch (Exception ex) {
			return null;
		}
	}

	@Nonnull
	@Override
	public Location getLocation(@Nonnull String path, @Nonnull Location def) {
		Location value = getLocation(path);
		return value == null ? def : value;
	}

	@Nonnull
	@Override
	public Map<String, Object> values() {
		return Collections.unmodifiableMap(values);
	}

	@Nonnull
	@Override
	public Collection<String> keys() {
		return values.keySet();
	}

	@Override
	public void forEach(@Nonnull BiConsumer<? super String, ? super Object> action) {
		values.forEach(action);
	}

	@Nonnull
	@Override
	public String toJson() {
		return new GsonDocument(values).toJson();
	}

	@Override
	public String toString() {
		return toJson();
	}

}
