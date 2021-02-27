package net.codingarea.challenges.plugin.utils.config.document;

import net.codingarea.challenges.plugin.utils.config.Document;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class EmptyDocument implements Document {

	@Nonnull
	@Override
	public Document getDocument(@Nonnull String path) {
		return new EmptyDocument();
	}

	@Nonnull
	@Override
	public Document set(@Nonnull String path, @Nullable Object value) {
		throw new UnsupportedOperationException("EmptyDocument.set(String, Object)");
	}

	@Nonnull
	@Override
	public Document clear() {
		return this;
	}

	@Nonnull
	@Override
	public Document remove(@Nonnull String path) {
		return this;
	}

	@Override
	public void write(@Nonnull Writer writer) throws IOException {
		throw new UnsupportedOperationException("EmptyDocument.write(Writer)");
	}

	@Nullable
	@Override
	public Object getObject(@Nonnull String path) {
		return null;
	}

	@Nullable
	@Override
	public String getString(@Nonnull String path) {
		return null;
	}

	@Nonnull
	@Override
	public String getString(@Nonnull String path, @Nonnull String def) {
		return def;
	}

	@Override
	public char getChar(@Nonnull String path) {
		return 0;
	}

	@Override
	public long getLong(@Nonnull String path) {
		return 0;
	}

	@Override
	public int getInt(@Nonnull String path) {
		return 0;
	}

	@Override
	public short getShort(@Nonnull String path) {
		return 0;
	}

	@Override
	public byte getByte(@Nonnull String path) {
		return 0;
	}

	@Override
	public float getFloat(@Nonnull String path) {
		return 0;
	}

	@Override
	public double getDouble(@Nonnull String path) {
		return 0;
	}

	@Override
	public boolean getBoolean(@Nonnull String path) {
		return false;
	}

	@Nonnull
	@Override
	public List<String> getList(@Nonnull String path) {
		return Collections.emptyList();
	}

	@Nullable
	@Override
	public UUID getUUID(@Nonnull String path) {
		return null;
	}

	@Nonnull
	@Override
	public UUID getUUID(@Nonnull String path, @Nonnull UUID def) {
		return def;
	}

	@Nullable
	@Override
	public Location getLocation(@Nonnull String path) {
		return null;
	}

	@Nonnull
	@Override
	public Location getLocation(@Nonnull String path, @Nonnull Location def) {
		return def;
	}

	@Nullable
	@Override
	public ItemStack getItemStack(@Nonnull String path) {
		return null;
	}

	@Nonnull
	@Override
	public ItemStack getItemStack(@Nonnull String path, @Nonnull ItemStack def) {
		return def;
	}

	@Nullable
	@Override
	public <E extends Enum<E>> E getEnum(@Nonnull String path, @Nonnull Class<E> classOfEnum) {
		return null;
	}

	@Nonnull
	@Override
	public <E extends Enum<E>> E getEnum(@Nonnull String path, @Nonnull E def) {
		return def;
	}

	@Override
	public boolean contains(@Nonnull String path) {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Nonnull
	@Override
	public Map<String, Object> values() {
		return Collections.emptyMap();
	}

	@Nonnull
	@Override
	public Collection<String> keys() {
		return Collections.emptyList();
	}

	@Nonnull
	@Override
	public String toJson() {
		return "{}";
	}

	@Override
	public boolean isReadonly() {
		return true;
	}
}
