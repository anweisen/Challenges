package net.codingarea.challenges.plugin.utils.misc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class BlockUtils {

	private BlockUtils() {}

	public static boolean isSameBlock(@Nonnull Location loc1, @Nonnull Location loc2) {
		return loc1.getBlockX() == loc2.getBlockX()
		 	&& loc1.getBlockY() == loc2.getBlockY()
			&& loc1.getBlockZ() == loc2.getBlockZ();
	}

	public static boolean isSameBlockIgnoreHeight(@Nullable Location loc1, @Nullable Location loc2) {
		if (loc1 == null || loc2 == null) return false;
		return loc1.getBlockX() == loc2.getBlockX()
			&& loc1.getBlockZ() == loc2.getBlockZ();
	}

	public static boolean isSameLocation(@Nonnull Location loc1, @Nonnull Location loc2) {
		return loc1.distance(loc2) < 0.1;
	}

	public static boolean isSameLocationIgnoreHeight(@Nonnull Location loc1, @Nonnull Location loc2) {
		return loc1.getX() == loc2.getX()
			&& loc1.getZ() == loc2.getZ();
	}

	private static final BlockFace[] faces = {
			BlockFace.UP, BlockFace.DOWN,
			BlockFace.NORTH, BlockFace.EAST,
			BlockFace.SOUTH, BlockFace.WEST
	};

	/**
	 * @param block middle block
	 * @return returns the block above, under, in the front, behind, to the left and to the right of the middle block
	 */
	public static List<Block> getBlocksAroundBlock(Block block) {
		List<Block> list = new ArrayList<>();

		for (BlockFace face : faces) {
			list.add(block.getRelative(face));
		}

		return list;
	}

	public static Material getTerracotta(int subid) {
		switch (subid) {
			case 2:
				return Material.ORANGE_TERRACOTTA;
			case 3:
				return Material.MAGENTA_TERRACOTTA;
			case 4:
				return Material.LIGHT_BLUE_TERRACOTTA;
			case 5:
				return Material.YELLOW_TERRACOTTA;
			case 6:
				return Material.LIME_TERRACOTTA;
			case 7:
				return Material.PINK_TERRACOTTA;
			case 8:
				return Material.GRAY_TERRACOTTA;
			case 9:
				return Material.LIGHT_GRAY_TERRACOTTA;
			case 10:
				return Material.CYAN_TERRACOTTA;
			case 11:
				return Material.PURPLE_TERRACOTTA;
			case 12:
				return Material.BLUE_TERRACOTTA;
			case 13:
				return Material.BROWN_TERRACOTTA;
			case 14:
				return Material.GREEN_TERRACOTTA;
			case 15:
				return Material.RED_TERRACOTTA;
			case 16:
				return Material.BLACK_TERRACOTTA;
			default:
				return Material.WHITE_TERRACOTTA;
		}
	}

}
