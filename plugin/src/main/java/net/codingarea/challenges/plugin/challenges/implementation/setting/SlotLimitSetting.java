package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.anweisen.utilities.commons.anntations.Since;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.Modifier;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.task.TimerTask;
import net.codingarea.challenges.plugin.management.scheduler.timer.TimerStatus;
import net.codingarea.challenges.plugin.spigot.events.PlayerInventoryClickEvent;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class SlotLimitSetting extends Modifier {

	private final ItemStack blockedItem = new ItemBuilder(Material.BARRIER, "§cBlocked").build();

	public SlotLimitSetting() {
		super(MenuType.SETTINGS, 1, 36, 36);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.BARRIER, Message.forName("item-slot-limit-setting"));
	}
	
	@Override
	protected void onValueChange() {
		updateSlots();

	}

	@TimerTask(status = TimerStatus.PAUSED)
	public void onPause() {
		updateSlots();
	}

	@TimerTask(status = TimerStatus.RUNNING)
	public void onRun() {
		updateSlots();
	}

	private void updateSlots() {
		for (int i = 0; i < 36; i++) {
			if (isBlocked(i) && ChallengeAPI.isStarted()) {
				Bukkit.broadcastMessage(Prefix.CHALLENGES + "§7Blocking Slot " + i);
				blockSlot(i);
			} else {
				unBlockSlot(i);
			}

		}
	}

	private boolean isBlocked(int slot) {
		int value = getValue() - 1;

		if (slot >= 9 && slot <= 17) {
			slot += 9 * 2;
		} else if (slot >= 27 && slot <= 35) {
			slot -= 9 * 2;
		}

		return slot > value;
	}

	private void blockSlot(int slot) {
		Bukkit.getOnlinePlayers().forEach(player -> blockSlot(player, slot));
	}

	private void blockSlot(@Nonnull Player player, int slot) {
		if (ignorePlayer(player)) return;

		ItemStack item = player.getInventory().getItem(slot);
		if (item != null && !item.isSimilar(blockedItem)) {
			player.getWorld().dropItemNaturally(player.getLocation(), item);
		}
		player.getInventory().setItem(slot, blockedItem);
	}

	private void unBlockSlot(int slot) {
		Bukkit.getOnlinePlayers().forEach(player -> unBlockSlot(player, slot));
	}

	private void unBlockSlot(@Nonnull Player player, int slot) {

		ItemStack item = player.getInventory().getItem(slot);
		if (item != null && item.isSimilar(blockedItem)) {
			player.getInventory().setItem(slot, null);
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerInventoryClick(@Nonnull PlayerInventoryClickEvent event) {
		if (event.getClickedInventory() == null) return;
		if (event.getClickedInventory().getType() != InventoryType.PLAYER) return;
		System.out.println("b");
		if (isBlocked(event.getSlot())) {
			System.out.println("c");
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
		if (ignorePlayer(event.getPlayer())) return;
		if (!event.getItemDrop().getItemStack().isSimilar(blockedItem)) return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onClick(@Nonnull BlockPlaceEvent event) {
		if (ignorePlayer(event.getPlayer())) return;
		if (!event.getItemInHand().isSimilar(blockedItem)) return;
		event.setCancelled(true);
	}

}