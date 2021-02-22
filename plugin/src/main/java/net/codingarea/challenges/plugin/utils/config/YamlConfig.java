package net.codingarea.challenges.plugin.utils.config;

import net.codingarea.challenges.plugin.utils.annotations.DeprecatedSince;
import net.codingarea.challenges.plugin.utils.annotations.ReplaceWith;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
@Deprecated
@DeprecatedSince("2.0")
@ReplaceWith("YamlDocument")
public class YamlConfig implements Configuration {

	protected final FileConfiguration config;
	protected final File file;

	public YamlConfig(@Nonnull File file) {
		this.file = file;
		config = YamlConfiguration.loadConfiguration(file);
	}

	public void save() throws IOException {
		config.save(file);
	}

	@Override
	public void addDefault(@Nonnull String path, @Nullable Object value) {
		config.addDefault(path, value);
	}

	@Override
	public void addDefaults(@Nonnull Map<String, Object> map) {
		config.addDefaults(map);
	}

	@Override
	public void addDefaults(@Nonnull Configuration configuration) {
		configuration.addDefaults(configuration);
	}

	@Override
	public void setDefaults(@Nonnull Configuration configuration) {
		configuration.setDefaults(configuration);
	}

	@Nullable
	@Override
	public Configuration getDefaults() {
		return config.getDefaults();
	}

	@Nonnull
	@Override
	public ConfigurationOptions options() {
		return config.options();
	}

	@Nonnull
	@Override
	public Set<String> getKeys(boolean deep) {
		return config.getKeys(deep);
	}

	@Nonnull
	@Override
	public Map<String, Object> getValues(boolean deep) {
		return config.getValues(deep);
	}

	@Override
	public boolean contains(@Nonnull String path) {
		return config.contains(path);
	}

	@Override
	public boolean contains(@Nonnull String path, boolean deep) {
		return config.contains(path, deep);
	}

	@Override
	public boolean isSet(@Nonnull String path) {
		return config.isSet(path);
	}

	@Nullable
	@Override
	public String getCurrentPath() {
		return config.getCurrentPath();
	}

	@Nonnull
	@Override
	public String getName() {
		return config.getName();
	}

	@Nullable
	@Override
	public Configuration getRoot() {
		return config.getRoot();
	}

	@Nullable
	@Override
	public ConfigurationSection getParent() {
		return config.getParent();
	}

	@Nullable
	@Override
	public Object get(@Nonnull String path) {
		return config.get(path);
	}

	@Nullable
	@Override
	public Object get(@Nonnull String path, @Nullable Object def) {
		return config.get(path, def);
	}

	@Override
	public void set(@Nonnull String path, @Nullable Object value) {
		config.set(path, value);
	}

	@Nonnull
	@Override
	public ConfigurationSection createSection(@Nonnull String path) {
		return config.createSection(path);
	}

	@Nonnull
	@Override
	public ConfigurationSection createSection(@Nonnull String path, @Nonnull Map<?, ?> map) {
		return config.createSection(path, map);
	}

	@Nullable
	@Override
	public String getString(@Nonnull String path) {
		return config.getString(path);
	}

	@Nullable
	@Override
	public String getString(@Nonnull String path, @Nullable String def) {
		return config.getString(path, def);
	}

	@Override
	public boolean isString(@Nonnull String path) {
		return config.isString(path);
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
	public boolean isInt(@Nonnull String path) {
		return config.isInt(path);
	}

	@Override
	public boolean getBoolean(@Nonnull String path) {
		return config.getBoolean(path);
	}

	@Override
	public boolean getBoolean(@Nonnull String path, boolean def) {
		return config.getBoolean(path, def);
	}

	@Override
	public boolean isBoolean(@Nonnull String path) {
		return config.isBoolean(path);
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
	public boolean isDouble(@Nonnull String path) {
		return config.isDouble(path);
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
	public boolean isLong(@Nonnull String path) {
		return config.isLong(path);
	}

	@Nullable
	@Override
	public List<?> getList(@Nonnull String path) {
		return config.getList(path);
	}

	@Nullable
	@Override
	public List<?> getList(@Nonnull String path, @Nullable List<?> def) {
		return config.getList(path, def);
	}

	@Override
	public boolean isList(@Nonnull String path) {
		return config.isList(path);
	}

	@Nonnull
	@Override
	public List<String> getStringList(@Nonnull String path) {
		return config.getStringList(path);
	}

	@Nonnull
	@Override
	public List<Integer> getIntegerList(@Nonnull String path) {
		return config.getIntegerList(path);
	}

	@Nonnull
	@Override
	public List<Boolean> getBooleanList(@Nonnull String path) {
		return config.getBooleanList(path);
	}

	@Nonnull
	@Override
	public List<Double> getDoubleList(@Nonnull String path) {
		return config.getDoubleList(path);
	}

	@Nonnull
	@Override
	public List<Float> getFloatList(@Nonnull String path) {
		return config.getFloatList(path);
	}

	@Nonnull
	@Override
	public List<Long> getLongList(@Nonnull String path) {
		return config.getLongList(path);
	}

	@Nonnull
	@Override
	public List<Byte> getByteList(@Nonnull String path) {
		return config.getByteList(path);
	}

	@Nonnull
	@Override
	public List<Character> getCharacterList(@Nonnull String path) {
		return config.getCharacterList(path);
	}

	@Nonnull
	@Override
	public List<Short> getShortList(@Nonnull String path) {
		return config.getShortList(path);
	}

	@Nonnull
	@Override
	public List<Map<?, ?>> getMapList(@Nonnull String path) {
		return config.getMapList(path);
	}

	@Nullable
	@Override
	public <T> T getObject(@Nonnull String path, @Nonnull Class<T> clazz) {
		return config.getObject(path, clazz);
	}

	@Nullable
	@Override
	public <T> T getObject(@Nonnull String path, @Nonnull Class<T> clazz, @Nullable T def) {
		return config.getObject(path, clazz, def);
	}

	@Nullable
	@Override
	public <T extends ConfigurationSerializable> T getSerializable(@Nonnull String path, @Nonnull Class<T> clazz) {
		return config.getSerializable(path, clazz);
	}

	@Nullable
	@Override
	public <T extends ConfigurationSerializable> T getSerializable(@Nonnull String path, @Nonnull Class<T> clazz, @Nullable T t) {
		return config.getSerializable(path, clazz, t);
	}

	@Nullable
	@Override
	public Vector getVector(@Nonnull String path) {
		return config.getVector(path);
	}

	@Nullable
	@Override
	public Vector getVector(@Nonnull String path, @Nullable Vector vector) {
		return config.getVector(path, vector);
	}

	@Override
	public boolean isVector(@Nonnull String path) {
		return config.isVector(path);
	}

	@Nullable
	@Override
	public OfflinePlayer getOfflinePlayer(@Nonnull String path) {
		return config.getOfflinePlayer(path);
	}


	@Override
	public OfflinePlayer getOfflinePlayer(@Nonnull String path, @Nullable OfflinePlayer def) {
		return config.getOfflinePlayer(path, def);
	}

	@Override
	public boolean isOfflinePlayer(@Nonnull String s) {
		return config.isOfflinePlayer(s);
	}

	@Nullable
	@Override
	public ItemStack getItemStack(@Nonnull String path) {
		return config.getItemStack(path);
	}

	@Nullable
	@Override
	public ItemStack getItemStack(@Nonnull String path, @Nullable ItemStack def) {
		return config.getItemStack(path, def);
	}

	@Override
	public boolean isItemStack(@Nonnull String path) {
		return config.isItemStack(path);
	}

	@Nullable
	@Override
	public Color getColor(@Nonnull String path) {
		return config.getColor(path);
	}

	@Nullable
	@Override
	public Color getColor(@Nonnull String path, @Nullable Color def) {
		return config.getColor(path, def);
	}

	@Override
	public boolean isColor(@Nonnull String path) {
		return config.isColor(path);
	}

	@Nullable
	public Location getLocation(@Nonnull String path) {
		return config.getSerializable(path, Location.class);
	}

	@Nullable
	public Location getLocation(@Nonnull String path, @Nullable Location def) {
		return config.getSerializable(path, Location.class, def);
	}

	@Nullable
	@Override
	public ConfigurationSection getConfigurationSection(@Nonnull String path) {
		return config.getConfigurationSection(path);
	}

	@Override
	public boolean isConfigurationSection(@Nonnull String path) {
		return config.isConfigurationSection(path);
	}

	@Nullable
	@Override
	public ConfigurationSection getDefaultSection() {
		return config.getDefaultSection();
	}

}
