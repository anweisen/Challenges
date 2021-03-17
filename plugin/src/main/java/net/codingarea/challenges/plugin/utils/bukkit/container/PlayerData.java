package net.codingarea.challenges.plugin.utils.bukkit.container;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class PlayerData {

	private final GameMode gamemode;
	private final Location location;
	private final ItemStack[] inventory;
	private final ItemStack[] armor;

	public PlayerData(@Nonnull Player player) {
		this(player.getGameMode(), player.getLocation(), player.getInventory().getContents(), player.getInventory().getArmorContents());
	}

	public PlayerData(@Nonnull GameMode gamemode, @Nonnull Location location, @Nonnull ItemStack[] inventory, @Nonnull ItemStack[] armor) {
		this.gamemode = gamemode;
		this.location = location;
		this.inventory = inventory;
		this.armor = armor;
	}

	public void apply(@Nonnull Player player) {
		player.setGameMode(gamemode);
		player.teleport(location);
		player.getInventory().setContents(inventory);
		player.getInventory().setArmorContents(armor);
	}

	@Override
	public String toString() {
		return "PlayerData{" +
				"location=" + location +
				", inventory=" + Arrays.toString(inventory) +
				", armor=" + Arrays.toString(armor) +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PlayerData that = (PlayerData) o;
		return location.equals(that.location) && Arrays.equals(inventory, that.inventory) && Arrays.equals(armor, that.armor);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(location);
		result = 31 * result + Arrays.hashCode(inventory);
		result = 31 * result + Arrays.hashCode(armor);
		return result;
	}

}
