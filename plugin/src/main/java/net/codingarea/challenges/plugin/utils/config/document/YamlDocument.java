package net.codingarea.challenges.plugin.utils.config.document;

import net.codingarea.challenges.plugin.utils.config.Document;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class YamlDocument implements Document {

	protected final ConfigurationSection config;

	public YamlDocument() {
		this.config = new YamlConfiguration();
	}

	public YamlDocument(@Nonnull ConfigurationSection config) {
		this.config = config;
	}

	public YamlDocument(@Nonnull File file) {
		this.config = YamlConfiguration.loadConfiguration(file);
	}

	@Nullable
	@Override
	public String getString(@Nonnull String path) {
		return config.getString(path);
	}

	@Nonnull
	@Override
	public String getString(@Nonnull String path, @Nonnull String def) {
		String string = config.getString(path, def);
		return string == null ? def : string;
	}

	@Nullable
	@Override
	public Object getObject(@Nonnull String path) {
		return config.get(path);
	}

	@Nonnull
	@Override
	public Document getDocument(@Nonnull String path) {
		ConfigurationSection section = config.getConfigurationSection(path);
		if (section == null) section = config.createSection(path);
		return new YamlDocument(section);
	}

	@Override
	public long getLong(@Nonnull String path) {
		return config.getLong(path);
	}

	@Override
	public long getLong(@Nonnull String path, long def) {
		return config.getLong(path, def);
	}

	@Override
	public int getInt(@Nonnull String path) {
		return config.getInt(path);
	}

	@Override
	public int getInt(@Nonnull String path, int def) {
		return config.getInt(path, def);
	}

	@Override
	public short getShort(@Nonnull String path) {
		return (short) config.getInt(path);
	}

	@Override
	public short getShort(@Nonnull String path, short def) {
		return (short) config.getInt(path, def);
	}

	@Override
	public byte getByte(@Nonnull String path) {
		return (byte) config.getInt(path);
	}

	@Override
	public byte getByte(@Nonnull String path, byte def) {
		return (byte) config.getInt(path, def);
	}

	@Override
	public char getChar(@Nonnull String path) {
		return getChar(path, (char) 0);
	}

	@Override
	public char getChar(@Nonnull String path, char def) {
		try {
			return getString(path).charAt(0);
		} catch (NullPointerException | StringIndexOutOfBoundsException ex) {
			return def;
		}
	}

	@Override
	public double getDouble(@Nonnull String path) {
		return config.getDouble(path);
	}

	@Override
	public double getDouble(@Nonnull String path, double def) {
		return config.getDouble(path, def);
	}

	@Override
	public float getFloat(@Nonnull String path) {
		return (float) config.getDouble(path);
	}

	@Override
	public float getFloat(@Nonnull String path, float def) {
		return (float) config.getDouble(path, def);
	}

	@Override
	public boolean getBoolean(@Nonnull String path) {
		return config.getBoolean(path);
	}

	@Override
	public boolean getBoolean(@Nonnull String path, boolean def) {
		return config.getBoolean(path, def);
	}

	@Nonnull
	@Override
	public List<String> getList(@Nonnull String path) {
		return config.getStringList(path);
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
	public Location getLocation(@Nonnull String path) {
		return config.getSerializable(path, Location.class);
	}

	@Nonnull
	@Override
	public Location getLocation(@Nonnull String path, @Nonnull Location def) {
		if (!contains(path)) return def;
		return config.getSerializable(path, Location.class, def);
	}

	@Nullable
	@Override
	public ItemStack getItemStack(@Nonnull String path) {
		if (!contains(path)) return null;
		return config.getSerializable(path, ItemStack.class);
	}

	@Nonnull
	@Override
	public ItemStack getItemStack(@Nonnull String path, @Nonnull ItemStack def) {
		if (!contains(path)) return def;
		return config.getSerializable(path, ItemStack.class, def);
	}

	@Nullable
	@Override
	public <E extends Enum<E>> E getEnum(@Nonnull String path, @Nonnull Class<E> classOfEnum) {
		try {
			String name = getString(path);
			if (name == null) return null;
			return Enum.valueOf(classOfEnum, name);
		} catch (Throwable ex) {
			return null;
		}
	}

	@Nonnull
	@Override
	public <E extends Enum<E>> E getEnum(@Nonnull String path, @Nonnull E def) {
		E value = getEnum(path, (Class<E>) def.getClass());
		return value == null ? def : value;
	}

	@Override
	public boolean contains(@Nonnull String path) {
		return config.contains(path, true);
	}

	@Override
	public boolean isEmpty() {
		return config.getValues(false).isEmpty();
	}

	@Nonnull
	@Override
	public Document set(@Nonnull String path, @Nullable Object value) {
		if (value instanceof Enum<?>) {
			Enum<?> enun = (Enum<?>) value;
			value = enun.name();
		}
		config.set(path, value);
		return this;
	}

	@Nonnull
	@Override
	public Document remove(@Nonnull String path) {
		config.set(path, null);
		return this;
	}

	@Nonnull
	@Override
	public Document clear() {
		for (String key : config.getKeys(true)) {
			config.set(key, null);
		}
		return this;
	}

	@Nonnull
	@Override
	public Map<String, Object> values() {
		return config.getValues(true);
	}

	@Nonnull
	@Override
	public Collection<String> keys() {
		return config.getKeys(false);
	}

	@Override
	public String toString() {

		DumperOptions yamlOptions = new DumperOptions();
		LoaderOptions loaderOptions = new LoaderOptions();
		Representer yamlRepresenter = new YamlRepresenter();
		Yaml yaml = new Yaml(new YamlConstructor(), yamlRepresenter, yamlOptions, loaderOptions);

		yamlOptions.setIndent(2);
		yamlOptions.setDefaultFlowStyle(FlowStyle.BLOCK);
		yamlRepresenter.setDefaultFlowStyle(FlowStyle.BLOCK);
		String dump = yaml.dump(config.getValues(false));
		if (dump.equals("{}\n")) {
			dump = "";
		}

		return dump;

	}

	@Override
	public void write(@Nonnull Writer writer) throws IOException {
		writer.write(toString());
	}

	@Nonnull
	@Override
	public String toJson() {
		return new GsonDocument(values()).toJson();
	}

	@Override
	public boolean isReadonly() {
		return false;
	}

}
