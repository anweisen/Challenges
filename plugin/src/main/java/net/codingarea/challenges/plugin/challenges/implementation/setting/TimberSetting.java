package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.Modifier;
import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import net.codingarea.challenges.plugin.utils.misc.ListBuilder;
import net.codingarea.challenges.plugin.utils.misc.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author anweisen | https://github.com/anweisen
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class TimberSetting extends SettingModifier {

	public TimberSetting() {
		super(MenuType.SETTINGS, 2);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBreak(@Nonnull BlockBreakEvent event) {
		//if (!shouldExecuteEffect()) return;
		if (!isLog(event.getBlock().getType())) return;

		List<Block> treeBlocks = getAllTreeBlocks(event.getBlock(), getValue() == 2);
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

		final int[] index = {0};

		Bukkit.getScheduler().runTaskTimer(plugin, runnable -> {

			for (int i = 0; i < 2; i++) {
				Block block = treeBlocks.get(index[0]);
				breakBlock(block, item);

				index[0]++;
				if (index[0] >= treeBlocks.size()) {
					runnable.cancel();
				}

			}

		}, 0, 1);

	}

	private void breakBlock(@Nonnull Block block, @Nonnull ItemStack item) {

		if (isLog(block.getType())) {
			block.breakNaturally(item);
		} else if (isLeaves(block.getType())) {
			block.breakNaturally(item);
			int random = new Random().nextInt(5);
			if (random == 0) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.STICK));
			} else if (random == 1) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.APPLE));
			}
		}

	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.DIAMOND_AXE, Message.forName("item-timber-setting"));
	}

	@Nonnull
	@Override
	public ItemBuilder createSettingsItem() {
		if (getValue() == 1) {
			return new ItemBuilder(Material.OAK_LOG, "§6Logs");
		}
		return new ItemBuilder(Material.OAK_LEAVES, "§2Logs & Leaves");
	}

	private List<Block> getAllTreeBlocks(@Nonnull Block block, boolean leaves) {
		List<Block> allBlocks = new ArrayList<>();

		List<Block> currentBlocks = new ArrayList<>();
		currentBlocks.add(block);

		for (int i = 0; i < 8; i++) {

			ArrayList<Block> lastBlocks = new ArrayList<>(currentBlocks);
			currentBlocks.clear();

			for (Block currentBlock : lastBlocks) {


				for (Block blockAround : BlockUtils.getBlocksAroundBlock(currentBlock)) {
					if (blockAround.getType() == Material.AIR) continue;
					if (allBlocks.contains(blockAround)) continue;

					if (currentBlock.getType() == blockAround.getType() || (leaves && isLeaveMaterial(currentBlock.getType(), blockAround.getType()))) {
						allBlocks.add(blockAround);
						currentBlocks.add(blockAround);
					}

				}

			}

			if (currentBlocks.isEmpty()) {
				break;
			}

		}

		return allBlocks;
	}

	private boolean isLog(Material material) {
		String name = material.name();
		return name.contains("LOG") || name.contains("STEM");
	}

	private boolean isLeaves(Material material) {
		return material.name().endsWith("LEAVES") || material.name().endsWith("WART_BLOCk");
	}

	public boolean isLeaveMaterial(@Nonnull Material logMaterial, @Nonnull Material leaveMaterial) {
		if (logMaterial == Material.CRIMSON_STEM) return leaveMaterial == Material.NETHER_WART_BLOCK;

		String logPrefix = logMaterial.name().substring(0, logMaterial.name().indexOf("_"));

		return leaveMaterial.name().startsWith(logPrefix) && (leaveMaterial.name().endsWith("LEAVES") || leaveMaterial.name().endsWith("WART_BLOCK"));
	}

}