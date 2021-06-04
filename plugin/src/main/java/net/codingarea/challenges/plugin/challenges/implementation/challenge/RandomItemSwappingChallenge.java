package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.TimedChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class RandomItemSwappingChallenge extends TimedChallenge {

	public RandomItemSwappingChallenge() {
		super(MenuType.CHALLENGES, 1, 60, 5);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.HOPPER, Message.forName("item-random-swapping-challenge"));
	}


	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return Message.forName("item-time-seconds-description").asArray(getValue());
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChallengeSecondsValueChangeTitle(this, getValue());
	}

	@Override
	protected int getSecondsUntilNextActivation() {
		return getValue();
	}

	@Override
	protected void onTimeActivation() {
		restartTimer();

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (ignorePlayer(player)) continue;
			if (player.getInventory().getContents().length <= 0) continue;

			Bukkit.getScheduler().runTask(plugin, () -> {
				int slot = InventoryUtils.getRandomFullSlot(player.getInventory());
				if (slot == -1) return;
				swapItemToRandomSlot(
						player.getInventory(),
						InventoryUtils.getRandomFullSlot(player.getInventory()),
						InventoryUtils.getRandomSlot(player.getInventory())
				);

			});

		}

	}

	private void swapItemToRandomSlot(@Nonnull Inventory inventory, int slot1, int slot2) {
		if (slot1 == -1 || slot2 == -1) return;
		ItemStack item1 = inventory.getItem(slot1);
		ItemStack item2 = inventory.getItem(slot2);
		inventory.setItem(slot1, item2);
		inventory.setItem(slot2, item1);
	}

}