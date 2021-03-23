package net.codingarea.challenges.plugin.utils.bukkit.container;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class PlayerData {

	private final Collection<PotionEffect> effects;
	private final GameMode gamemode;
	private final Location location;
	private final ItemStack[] inventory;
	private final ItemStack[] armor;

	private final double health;
	private final int food;
	private final float saturation;

	public PlayerData(@Nonnull Player player) {
		this(player.getGameMode(), player.getLocation(), player.getInventory().getContents(), player.getInventory().getArmorContents(), player.getActivePotionEffects(), player.getHealth(), player.getFoodLevel(), player.getSaturation());
	}

	public PlayerData(@Nonnull GameMode gamemode, @Nonnull Location location, @Nonnull ItemStack[] inventory, @Nonnull ItemStack[] armor, @Nonnull Collection<PotionEffect> effects, double health, int food, float saturation) {
		this.gamemode = gamemode;
		this.location = location;
		this.inventory = inventory;
		this.armor = armor;
		this.effects = effects;
		this.health = health;
		this.food = food;
		this.saturation = saturation;
	}

	public void apply(@Nonnull Player player) {
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
		player.setGameMode(gamemode);
		player.teleport(location);
		player.setHealth(health);
		player.setFoodLevel(food);
		player.setSaturation(saturation);
		player.getInventory().setContents(inventory);
		player.getInventory().setArmorContents(armor);
		player.addPotionEffects(effects);
	}

	@Override
	public String toString() {
		return "PlayerData{" +
				"effects=" + effects +
				", gamemode=" + gamemode +
				", location=" + location +
				", inventory=" + Arrays.toString(inventory) +
				", armor=" + Arrays.toString(armor) +
				", health=" + health +
				", food=" + food +
				", saturation=" + saturation +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PlayerData data = (PlayerData) o;
		return Double.compare(data.health, health) == 0
				&& food == data.food
				&& Float.compare(data.saturation, saturation) == 0
				&& Objects.equals(effects, data.effects)
				&& gamemode == data.gamemode
				&& Objects.equals(location, data.location)
				&& Arrays.equals(inventory, data.inventory)
				&& Arrays.equals(armor, data.armor);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(effects, gamemode, location, health, food, saturation);
		result = 31 * result + Arrays.hashCode(inventory);
		result = 31 * result + Arrays.hashCode(armor);
		return result;
	}

}
