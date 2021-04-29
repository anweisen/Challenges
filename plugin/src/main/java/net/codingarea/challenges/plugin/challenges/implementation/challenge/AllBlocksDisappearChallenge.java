package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.bukkit.utils.misc.BukkitReflectionUtils;
import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.MenuSetting;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
public class AllBlocksDisappearChallenge extends MenuSetting {

	public AllBlocksDisappearChallenge() {
		super(MenuType.CHALLENGES, "All Blocks Disappear");
		registerSetting("break", new BooleanSubSetting(
				() -> new ItemBuilder(Material.DIAMOND_PICKAXE, Message.forName("item-all-blocks-disappear-break-challenge")),
				true
				));
		registerSetting("place", new BooleanSubSetting(
				() -> new ItemBuilder(Material.DIAMOND_BLOCK, Message.forName("item-all-blocks-disappear-place-challenge"))
		));

	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.TNT, Message.forName("item-all-blocks-disappear-challenge"));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		if (!shouldExecuteEffect()) return;
		if (!getSetting("break").getAsBoolean()) return;
		if (ignorePlayer(event.getPlayer())) return;
		PlayerInventory inventory = event.getPlayer().getInventory();
		event.setDropItems(false);
		breakBlocks(event.getBlock(), inventory.getItemInMainHand(), inventory);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(@Nonnull BlockPlaceEvent event) {
		if (!shouldExecuteEffect()) return;
		if (!getSetting("place").getAsBoolean()) return;
		if (ignorePlayer(event.getPlayer())) return;
		if (event.getBlockAgainst().getType() == Material.BEDROCK) return;
		breakBlocks(event.getBlockAgainst(), null, event.getPlayer().getInventory());
	}

	private void breakBlocks(@Nonnull Block block, @Nullable ItemStack tool, @Nonnull Inventory inventory) {
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

		dropList(allDrops, block.getLocation(), inventory);
	}

	private void dropList(@Nonnull Collection<ItemStack> itemStacks, @Nonnull Location location, @Nonnull Inventory inventory) {
		if (location.getWorld() == null) return;
		for (ItemStack itemStack : itemStacks) {
			ChallengeHelper.dropItem(itemStack, location, inventory);
			location.getWorld().dropItemNaturally(location, itemStack);
		}
	}

	protected List<Block> getAllBlocksToBreak(@Nonnull Chunk chunk, @Nonnull Material material) {
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
