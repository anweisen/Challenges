package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class MoreBlocksChallenge extends Setting implements Listener {

	int currentCount = 1;

	public MoreBlocksChallenge() {
		super(MenuType.CHALLENGES);
	}

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.GRASS_BLOCK, "§2More Blocks").build();
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
	}

	@Override
	public void onDisable(ChallengeEditEvent event) {
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (!this.enabled || !Challenges.timerIsStarted()) {
			return;
		}

		if (!event.isDropItems()) {
			return;
		}

			int stackCount = (int) Math.ceil((double) currentCount / 64);

			for (ItemStack drop : event.getBlock().getDrops()) {
				int itemsToDrop = currentCount;

				for (int i = 0; i < stackCount; i++) {
					ItemStack dropItem = drop.clone();

					if (itemsToDrop > 64) {
						dropItem.setAmount(64);
						itemsToDrop-= 64;
					} else {
						drop.setAmount(itemsToDrop);
						itemsToDrop = 0;
					}
					Utils.safeGiveItemToPlayer(event.getPlayer(), dropItem);
				}

			}

			currentCount *= 2;

	}

}