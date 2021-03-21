package net.codingarea.challenges.plugin.management.player;

import net.anweisen.utilities.commons.config.Config;
import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.commons.config.document.AbstractConfig;
import net.anweisen.utilities.commons.config.document.AbstractDocument;
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
public final class ChallengePlayer extends AbstractConfig {

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

	@Override
	public long getLong(@Nonnull String path, long def) {
		return NumberConversions.toLong(cache.getOrDefault(path, def));
	}

	@Override
	public int getInt(@Nonnull String path, int def) {
		return NumberConversions.toInt(cache.getOrDefault(path, def));
	}

	@Override
	public short getShort(@Nonnull String path, short def) {
		return NumberConversions.toShort(cache.getOrDefault(path, def));
	}

	@Override
	public byte getByte(@Nonnull String path, byte def) {
		return NumberConversions.toByte(cache.getOrDefault(path, def));
	}

	@Override
	public float getFloat(@Nonnull String path, float def) {
		return NumberConversions.toFloat(cache.getOrDefault(path, def));
	}

	@Override
	public double getDouble(@Nonnull String path, double def) {
		return NumberConversions.toDouble(cache.getOrDefault(path, def));
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

	@Nullable
	public Location getLocation(@Nonnull String path) {
		return (Location) cache.get(path);
	}

	@Nonnull
	public Location getLocation(@Nonnull String path, @Nonnull Location def) {
		return (Location) cache.getOrDefault(path, def);
	}

	@Nullable
	public ItemStack getItemStack(@Nonnull String path) {
		return (ItemStack) cache.get(path);
	}

	@Nonnull
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

	@Nullable
	@Override
	public <T> T getSerializable(@Nonnull String path, @Nonnull Class<T> classOfT) {
		return classOfT.cast(getObject(path));
	}

	@Override
	public boolean contains(@Nonnull String path) {
		return cache.containsKey(path);
	}

	@Override
	public int size() {
		return cache.size();
	}

	@Nonnull
	@Override
	public ChallengePlayer set(@Nonnull String path, @Nullable Object value) {
		cache.put(path, value);
		return this;
	}

	@Nonnull
	@Override
	public ChallengePlayer clear() {
		cache.clear();
		return this;
	}

	@Nonnull
	@Override
	public ChallengePlayer remove(@Nonnull String path) {
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
