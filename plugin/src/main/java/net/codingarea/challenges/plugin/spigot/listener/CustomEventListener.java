package net.codingarea.challenges.plugin.spigot.listener;

import net.codingarea.challenges.plugin.spigot.events.PlayerInventoryClickEvent;
import net.codingarea.challenges.plugin.spigot.events.PlayerJumpEvent;
import net.codingarea.challenges.plugin.spigot.events.PlayerPickupItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public class CustomEventListener implements Listener {

	/**
	 * Detecting jumps and calls {@link PlayerJumpEvent}
	 * @param event the event which is called
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerStatisticIncrement(@Nonnull PlayerStatisticIncrementEvent event) {
		if (event.getStatistic() == Statistic.JUMP) {
			Bukkit.getPluginManager().callEvent(new PlayerJumpEvent(event.getPlayer()));
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryClick(@Nonnull InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		PlayerInventoryClickEvent eventCall = new PlayerInventoryClickEvent(event);
		Bukkit.getPluginManager().callEvent(eventCall);
		event.setCancelled(eventCall.isCancelled());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityPickupItem(@Nonnull EntityPickupItemEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		PlayerPickupItemEvent eventCall = new PlayerPickupItemEvent(((Player) event.getEntity()), event.getItem(), event.getRemaining());
		Bukkit.getPluginManager().callEvent(eventCall);
		event.setCancelled(eventCall.isCancelled());
	}

}