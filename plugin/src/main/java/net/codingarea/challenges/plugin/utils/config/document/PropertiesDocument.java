package net.codingarea.challenges.plugin.utils.config.document;

import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.misc.FileUtils;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.Map.Entry;

/**
 * This document only supports basic objects like {@link Number numbers}, {@link String strings}, {@link Character characters} and {@link Boolean booleans}.
 * You may use more advanced documents which are fully supported like {@link YamlDocument} or {@link GsonDocument}
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class PropertiesDocument implements Document {

	protected final Properties properties;

	public PropertiesDocument(@Nonnull Properties properties) {
		this.properties = properties;
	}

	public PropertiesDocument(@Nonnull File file) throws IOException {
		properties = new Properties();
		properties.load(FileUtils.createReader(file));
	}

	@Nonnull
	@Override
	public Document getDocument(@Nonnull String path) {
		throw new UnsupportedOperationException("PropertiesDocument.getDocument(String)");
	}

	@Nonnull
	@Override
	public List<String> getList(@Nonnull String path) {
		throw new UnsupportedOperationException("PropertiesDocument.getList(String)");
	}

	@Override
	public char getChar(@Nonnull String path) {
		try {
			return getString(path).charAt(0);
		} catch (NullPointerException | StringIndexOutOfBoundsException ex) {
			return 0;
		}
	}

	@Nullable
	@Override
	public Object getObject(@Nonnull String path) {
		return properties.get(path);
	}

	@Nullable
	@Override
	public String getString(@Nonnull String path) {
		return properties.getProperty(path);
	}

	@Nonnull
	@Override
	public String getString(@Nonnull String path, @Nonnull String def) {
		return properties.getProperty(path, def);
	}

	@Override
	public long getLong(@Nonnull String path) {
		return NumberConversions.toLong(getString(path));
	}

	@Override
	public int getInt(@Nonnull String path) {
		return NumberConversions.toInt(getString(path));
	}

	@Override
	public short getShort(@Nonnull String path) {
		return NumberConversions.toShort(getString(path));
	}

	@Override
	public byte getByte(@Nonnull String path) {
		return NumberConversions.toByte(getString(path));
	}

	@Override
	public float getFloat(@Nonnull String path) {
		return NumberConversions.toFloat(getString(path));
	}

	@Override
	public double getDouble(@Nonnull String path) {
		return NumberConversions.toDouble(getString(path));
	}

	@Override
	public boolean getBoolean(@Nonnull String path) {
		return Boolean.getBoolean(getString(path));
	}

	@Nullable
	@Override
	public Location getLocation(@Nonnull String path) {
		throw new UnsupportedOperationException("PropertiesDocument.getLocation(String)");
	}

	@Nonnull
	@Override
	public Location getLocation(@Nonnull String path, @Nonnull Location def) {
		throw new UnsupportedOperationException("PropertiesDocument.getLocation(String, Location)");
	}

	@Nullable
	@Override
	public ItemStack getItemStack(@Nonnull String path) {
		throw new UnsupportedOperationException("PropertiesDocument.getItemStack(String)");
	}

	@Nonnull
	@Override
	public ItemStack getItemStack(@Nonnull String path, @Nonnull ItemStack def) {
		throw new UnsupportedOperationException("PropertiesDocument.getItemStack(String, ItemStack)");
	}

	@Override
	public boolean contains(@Nonnull String path) {
		return properties.containsKey(path);
	}

	@Nonnull
	@Override
	public Map<String, Object> values() {
		Map<String, Object> map = new LinkedHashMap<>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			map.put((String) entry.getKey(), entry.getValue());
		}
		return map;
	}

	@Nonnull
	@Override
	public Collection<String> keys() {
		return properties.stringPropertyNames();
	}

	@Nonnull
	@Override
	public Document set(@Nonnull String path, @Nullable Object value) {
		properties.setProperty(path, String.valueOf(value));
		return this;
	}

	@Nonnull
	@Override
	public Document clear() {
		properties.clear();
		return this;
	}

	@Nonnull
	@Override
	public Document remove(@Nonnull String path) {
		properties.remove(path);
		return this;
	}

	@Override
	public void write(@Nonnull Writer writer) throws IOException {
		properties.store(writer, null);
	}

	@Nonnull
	public Properties getProperties() {
		return properties;
	}

}
