package net.codingarea.challenges.plugin.utils.config.document.wrapper;

import net.codingarea.challenges.plugin.utils.config.Document;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 *
 * @see net.codingarea.challenges.plugin.utils.config.document.wrapper.FileDocumentWrapper
 * @see net.codingarea.challenges.plugin.utils.config.document.readonly.ReadOnlyDocumentWrapper
 */
public abstract class DocumentWrapper implements Document {

	protected final Document document;

	public DocumentWrapper(@Nonnull Document document) {
		this.document = document;
	}

	@Override
	public boolean isReadonly() {
		return document.isReadonly();
	}

	@Nonnull
	@Override
	public Document getDocument(@Nonnull String path) {
		return document.getDocument(path);
	}

	@Nonnull
	@Override
	public Document set(@Nonnull String path, @Nullable Object value) {
		return document.set(path, value);
	}

	@Nonnull
	@Override
	public Document clear() {
		return document.clear();
	}

	@Nonnull
	@Override
	public Document remove(@Nonnull String path) {
		return document.remove(path);
	}

	@Override
	public void write(@Nonnull Writer writer) throws IOException {
		document.write(writer);
	}

	@Nonnull
	@Override
	public String toJson() {
		return document.toJson();
	}

	@Nullable
	@Override
	public Object getObject(@Nonnull String path) {
		return document.getObject(path);
	}

	@Nullable
	@Override
	public String getString(@Nonnull String path) {
		return document.getString(path);
	}

	@Nonnull
	@Override
	public String getString(@Nonnull String path, @Nonnull String def) {
		return document.getString(path, def);
	}

	@Override
	public char getChar(@Nonnull String path) {
		return document.getChar(path);
	}

	@Override
	public char getChar(@Nonnull String path, char def) {
		return document.getChar(path, def);
	}

	@Override
	public long getLong(@Nonnull String path) {
		return document.getLong(path);
	}

	@Override
	public long getLong(@Nonnull String path, long def) {
		return document.getLong(path, def);
	}

	@Override
	public int getInt(@Nonnull String path) {
		return document.getInt(path);
	}

	@Override
	public int getInt(@Nonnull String path, int def) {
		return document.getInt(path, def);
	}

	@Override
	public short getShort(@Nonnull String path) {
		return document.getShort(path);
	}

	@Override
	public short getShort(@Nonnull String path, short def) {
		return document.getShort(path, def);
	}

	@Override
	public byte getByte(@Nonnull String path) {
		return document.getByte(path);
	}

	@Override
	public byte getByte(@Nonnull String path, byte def) {
		return document.getByte(path, def);
	}

	@Override
	public float getFloat(@Nonnull String path) {
		return document.getFloat(path);
	}

	@Override
	public float getFloat(@Nonnull String path, float def) {
		return document.getFloat(path, def);
	}

	@Override
	public double getDouble(@Nonnull String path) {
		return document.getDouble(path);
	}

	@Override
	public double getDouble(@Nonnull String path, double def) {
		return document.getDouble(path, def);
	}

	@Override
	public boolean getBoolean(@Nonnull String path) {
		return document.getBoolean(path);
	}

	@Override
	public boolean getBoolean(@Nonnull String path, boolean def) {
		return document.getBoolean(path, def);
	}

	@Nonnull
	@Override
	public List<String> getList(@Nonnull String path) {
		return document.getList(path);
	}

	@Nullable
	@Override
	public UUID getUUID(@Nonnull String path) {
		return document.getUUID(path);
	}

	@Nonnull
	@Override
	public UUID getUUID(@Nonnull String path, @Nonnull UUID def) {
		return document.getUUID(path, def);
	}

	@Nullable
	@Override
	public Location getLocation(@Nonnull String path) {
		return document.getLocation(path);
	}

	@Nonnull
	@Override
	public Location getLocation(@Nonnull String path, @Nonnull Location def) {
		return document.getLocation(path, def);
	}

	@Nullable
	@Override
	public ItemStack getItemStack(@Nonnull String path) {
		return document.getItemStack(path);
	}

	@Nonnull
	@Override
	public ItemStack getItemStack(@Nonnull String path, @Nonnull ItemStack def) {
		return document.getItemStack(path, def);
	}

	@Nullable
	@Override
	public <E extends Enum<E>> E getEnum(@Nonnull String path, @Nonnull Class<E> classOfEnum) {
		return document.getEnum(path, classOfEnum);
	}

	@Nonnull
	@Override
	public <E extends Enum<E>> E getEnum(@Nonnull String path, @Nonnull E def) {
		return document.getEnum(path, def);
	}

	@Override
	public boolean contains(@Nonnull String path) {
		return document.contains(path);
	}

	@Override
	public boolean isEmpty() {
		return document.isEmpty();
	}

	@Nonnull
	@Override
	public Map<String, Object> values() {
		return document.values();
	}

	@Nonnull
	@Override
	public Collection<String> keys() {
		return document.keys();
	}

	@Override
	public void forEach(@Nonnull BiConsumer<? super String, ? super Object> action) {
		document.forEach(action);
	}

}
