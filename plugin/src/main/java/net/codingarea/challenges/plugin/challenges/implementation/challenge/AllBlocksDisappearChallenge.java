package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.bukkit.wrapper.BukkitReflectionUtils;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.ListBuilder;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class AllBlocksDisappearChallenge extends Setting {

	public AllBlocksDisappearChallenge() {
		super(MenuType.CHALLENGES);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.TNT, Message.forName("item-all-blocks-disappear-challenge"));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		if (!shouldExecuteEffect()) return;
		breakBlocks(event.getBlock(), event.getPlayer().getInventory().getItemInMainHand());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(@Nonnull BlockPlaceEvent event) {
		if (!shouldExecuteEffect()) return;
		breakBlocks(event.getBlockAgainst(), null);
	}

	private void breakBlocks(@Nonnull Block block, @Nullable ItemStack tool) {
		Chunk chunk = block.getChunk();
		List<Block> blocks = getAllBlocksToBreak(chunk, block.getType());

		List<ItemStack> allDrops = new ArrayList<>();

		for (Block current : blocks) {
			Collection<ItemStack> drops = Challenges.getInstance().getBlockDropManager().getDrops(current, tool);
			current.setType(Material.AIR);

			for (ItemStack currentBlockDrop : drops) {
				boolean containsType = false;

				for (ItemStack currentAllDrop : allDrops) {
					if (currentAllDrop.getType() == currentBlockDrop.getType() && (currentAllDrop.getAmount() + currentBlockDrop.getAmount()) <= currentAllDrop.getMaxStackSize()) {
						containsType = true;
						currentAllDrop.setAmount(currentAllDrop.getAmount() + currentBlockDrop.getAmount());
					}
				}

				if (!containsType) {
					allDrops.add(currentBlockDrop);
				}

			}

		}

		dropList(allDrops, block.getLocation());
	}

	private void dropList(@Nonnull Collection<ItemStack> itemStacks, @Nonnull Location location) {
		if (location.getWorld() == null) return;
		for (ItemStack itemStack : itemStacks) {
			location.getWorld().dropItemNaturally(location, itemStack);
		}
	}

	public List<Block> getAllBlocksToBreak(@Nonnull Chunk chunk, @Nonnull Material material) {
		return new ListBuilder<Block>()
				.fill(builder -> {
					for (int x = 0; x < 16; x++) {
						for (int z = 0; z < 16; z++) {
							for (int y = BukkitReflectionUtils.getMinHeight(chunk.getWorld()); y < chunk.getWorld().getMaxHeight(); y++) {
								Block block = chunk.getBlock(x, y, z);
								if (block.getType() == material) {
									builder.add(block);
								}
							}
						}
					}
				})
				.build();
	}

}