package net.codingarea.challenges.plugin.utils.misc;

import org.bukkit.Material;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ItemUtils {

	private ItemUtils() {}

	@Nonnull
	public static Material convertFoodToCookedFood(@Nonnull Material material) {
		try {
			return Material.valueOf("COOKED_" + material.name());
		} catch (Exception ex) {
			return material; // No cooked material is available
		}
	}

	public static boolean isObtainableInSurvival(@Nonnull Material material) {
		String name = material.name();
		if (material.isAir()) return false;
		if (name.endsWith("_SPAWN_EGG")) return false;
		if (name.startsWith("INFESTED_")) return false;
		if (name.startsWith("LEGACY_")) return isObtainableInSurvival(Material.valueOf(name.substring("LEGACY_".length())));
		switch (name) { // Use name instead of enum its self, to prevent NoSuchFieldErrors in older versions where this specific enum does not exist
			case "CHAIN_COMMAND_BLOCK":
			case "REPEATING_COMMAND_BLOCK":
			case "COMMAND_BLOCK":
			case "COMMAND_BLOCK_MINECART":
			case "JIGSAW":
			case "STRUCTURE_BLOCK":
			case "STRUCTURE_VOID":
			case "BARRIER":
			case "BEDROCK":
			case "KNOWLEDGE_BOOK":
			case "DEBUG_STICK":
			case "END_PORTAL_FRAME":
			case "END_PORTAL":
			case "NETHER_PORTAL":
			case "END_GATEWAY":
			case "LAVA":
			case "WATER":
			case "LARGE_FERN":
			case "TALL_GRASS":
			case "TALL_SEAGRASS":
				return false;
		}

		return true;
	}

	public static Material[] getArmor() {
		return new Material[] {
				Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.IRON_HELMET,
				Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.DIAMOND_HELMET,
				Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS, Material.GOLDEN_HELMET,
				Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.LEATHER_HELMET,
				Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_HELMET,
				Utils.getMaterial("NETHERITE_CHESTPLATE"), Utils.getMaterial("NETHERITE_LEGGINGS"), Utils.getMaterial("NETHERITE_BOOTS"), Utils.getMaterial("NETHERITE_HELMET"),
				Material.TURTLE_HELMET
		};
	}

}
