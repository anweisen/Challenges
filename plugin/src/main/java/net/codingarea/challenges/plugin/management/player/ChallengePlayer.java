package net.codingarea.challenges.plugin.management.player;

import net.codingarea.challenges.plugin.utils.config.Config;
import net.codingarea.challenges.plugin.utils.config.Propertyable;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ChallengePlayer implements Config {

	private final Map<String, Object> cache = new HashMap<>();
	private final Player player;

	ChallengePlayer(@Nonnull Player player) {
		this.player = player;
	}

	@Nullable
	@Override
	public Object getObject(@Nonnull String path) {
		return cache.get(path);
	}

	@Nullable
	@Override
	public String getString(@Nonnull String path) {
		return (String) cache.get(path);
	}

	@Nonnull
	@Override
	public String getString(@Nonnull String path, @Nonnull String def) {
		return (String) cache.getOrDefault(path, def);
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
		return NumberConversions.toLong(cache.get(path));
	}

	@Override
	public long getLong(@Nonnull String path, long def) {
		return NumberConversions.toLong(cache.getOrDefault(path, def));
	}

	@Override
	public int getInt(@Nonnull String path) {
		return NumberConversions.toInt(cache.get(path));
	}

	@Override
	public int getInt(@Nonnull String path, int def) {
		return NumberConversions.toInt(cache.getOrDefault(path, def));
	}

	@Override
	public short getShort(@Nonnull String path) {
		return NumberConversions.toShort(cache.get(path));
	}

	@Override
	public short getShort(@Nonnull String path, short def) {
		return NumberConversions.toShort(cache.getOrDefault(path, def));
	}

	@Override
	public byte getByte(@Nonnull String path) {
		return NumberConversions.toByte(cache.get(path));
	}

	@Override
	public byte getByte(@Nonnull String path, byte def) {
		return NumberConversions.toByte(cache.getOrDefault(path, def));
	}

	@Override
	public float getFloat(@Nonnull String path) {
		return NumberConversions.toFloat(cache.get(path));
	}

	@Override
	public float getFloat(@Nonnull String path, float def) {
		return NumberConversions.toFloat(cache.getOrDefault(path, def));
	}

	@Override
	public double getDouble(@Nonnull String path) {
		return NumberConversions.toDouble(cache.get(path));
	}

	@Override
	public double getDouble(@Nonnull String path, double def) {
		return NumberConversions.toDouble(cache.getOrDefault(path, def));
	}

	@Override
	public boolean getBoolean(@Nonnull String path) {
		return Boolean.parseBoolean(cache.getOrDefault(path, false).toString());
	}

	@Override
	public boolean getBoolean(@Nonnull String path, boolean def) {
		return Boolean.parseBoolean(cache.getOrDefault(path, def).toString());
	}

	@Nonnull
	@Override
	public List<String> getList(@Nonnull String path) {
		return (List<String>) cache.getOrDefault(path, new ArrayList<>());
	}

	@Nullable
	@Override
	public UUID getUUID(@Nonnull String path) {
		return (UUID) cache.get(path);
	}

	@Nonnull
	@Override
	public UUID getUUID(@Nonnull String path, @Nonnull UUID def) {
		return (UUID) cache.getOrDefault(path, def);
	}

	@Nullable
	@Override
	public Location getLocation(@Nonnull String path) {
		return (Location) cache.get(path);
	}

	@Nonnull
	@Override
	public Location getLocation(@Nonnull String path, @Nonnull Location def) {
		return (Location) cache.getOrDefault(path, def);
	}

	@Nullable
	@Override
	public ItemStack getItemStack(@Nonnull String path) {
		return (ItemStack) cache.get(path);
	}

	@Nonnull
	@Override
	public ItemStack getItemStack(@Nonnull String path, @Nonnull ItemStack def) {
		return (ItemStack) cache.getOrDefault(path, def);
	}

	@Nullable
	@Override
	public <E extends Enum<E>> E getEnum(@Nonnull String path, @Nonnull Class<E> classOfEnum) {
		return (E) cache.get(path);
	}

	@Nonnull
	@Override
	public <E extends Enum<E>> E getEnum(@Nonnull String path, @Nonnull E def) {
		return (E) cache.getOrDefault(path, def);
	}

	@Override
	public boolean contains(@Nonnull String path) {
		return cache.containsKey(path);
	}

	@Override
	public boolean isEmpty() {
		return cache.isEmpty();
	}

	@Nonnull
	@Override
	public Config set(@Nonnull String path, @Nullable Object value) {
		cache.put(path, value);
		return this;
	}

	@Nonnull
	@Override
	public Config clear() {
		cache.clear();
		return this;
	}

	@Nonnull
	@Override
	public Config remove(@Nonnull String path) {
		cache.remove(path);
		return this;
	}

	@Nonnull
	@Override
	public Map<String, Object> values() {
		return Collections.unmodifiableMap(cache);
	}

	@Nonnull
	@Override
	public Collection<String> keys() {
		return cache.keySet();
	}

	@Override
	public void forEach(@Nonnull BiConsumer<? super String, ? super Object> action) {
		cache.forEach(action);
	}

	@Override
	public boolean isReadonly() {
		return false;
	}

	@Nonnull
	public Player getPlayer() {
		return player;
	}

	@Nonnull
	public Location getLocation() {
		return player.getLocation();
	}

	public void sendMessage(@Nullable Object message) {
		player.sendMessage(message == null ? "" : message.toString());
	}

	public void teleport(@Nonnull Location location) {
		player.teleport(location);
	}

	public void openInventory(@Nonnull Inventory inventory) {
		player.openInventory(inventory);
	}

}
