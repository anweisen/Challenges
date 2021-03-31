package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.anntations.Since;
import net.codingarea.challenges.plugin.challenges.type.TimedChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
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
public class RandomItemDroppingChallenge extends TimedChallenge {

	public RandomItemDroppingChallenge() {
		super(MenuType.CHALLENGES, 1, 60, 5);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.DROPPER, Message.forName("item-random-dropping-challenge"));
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
				dropRandomItem(player.getLocation(), player.getInventory());
			});
		}
	}

	private void dropRandomItem(@Nonnull Location location, @Nonnull Inventory inventory) {
		if (location.getWorld() == null) return;
		int slot = InventoryUtils.getRandomFullSlot(inventory);
		if (slot == -1) return;
		ItemStack item = inventory.getItem(slot);
		inventory.setItem(slot, null);
		Item droppedItem = location.getWorld().dropItemNaturally(location.clone().add(0, 1, 0), item);
		droppedItem.setVelocity(location.getDirection().multiply(0.4));
	}

}