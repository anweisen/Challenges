package net.codingarea.challenges.plugin.spigot.listener;

import javax.annotation.Nonnull;
import net.anweisen.utilities.bukkit.utils.misc.BukkitReflectionUtils;
import net.codingarea.challenges.plugin.challenges.type.abstraction.AbstractChallenge;
import net.codingarea.challenges.plugin.spigot.events.EntityDeathByPlayerEvent;
import net.codingarea.challenges.plugin.spigot.events.PlayerIgnoreStatusChangeEvent;
import net.codingarea.challenges.plugin.spigot.events.PlayerInventoryClickEvent;
import net.codingarea.challenges.plugin.spigot.events.PlayerJumpEvent;
import net.codingarea.challenges.plugin.spigot.events.PlayerPickupItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public class CustomEventListener implements Listener {

	/**
	 * Detecting jumps and calls a {@link PlayerJumpEvent}
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerStatisticIncrement(@Nonnull PlayerStatisticIncrementEvent event) {
		if (BukkitReflectionUtils.isInWater(event.getPlayer())) return;
		if (event.getStatistic() == Statistic.JUMP) {
			Bukkit.getPluginManager().callEvent(new PlayerJumpEvent(event.getPlayer(), event));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(@Nonnull InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		PlayerInventoryClickEvent eventCall = new PlayerInventoryClickEvent(event);
		eventCall.setCancelled(event.isCancelled());
		Bukkit.getPluginManager().callEvent(eventCall);
		event.setCancelled(eventCall.isCancelled());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityPickupItem(@Nonnull EntityPickupItemEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		PlayerPickupItemEvent eventCall = new PlayerPickupItemEvent(((Player) event.getEntity()), event.getItem(), event.getRemaining());
		eventCall.setCancelled(event.isCancelled());
		Bukkit.getPluginManager().callEvent(eventCall);
		event.setCancelled(eventCall.isCancelled());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) return;
		if (!(event.getEntity() instanceof LivingEntity)) return;
		LivingEntity entity = (LivingEntity) event.getEntity();
		if (entity.getHealth() - event.getDamage() > 0) return;
		EntityDeathByPlayerEvent deathEvent = new EntityDeathByPlayerEvent(event.getEntity(), (Player) event.getDamager(), event);
		Bukkit.getPluginManager().callEvent(deathEvent);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGameModeChange(PlayerGameModeChangeEvent event) {
		boolean execute = false;
		boolean isIgnored = false;
		if (AbstractChallenge.ignoreGameMode(event.getNewGameMode()) && !AbstractChallenge.ignoreGameMode(event.getPlayer().getGameMode())) {
			execute = true;
			isIgnored = true;
		} else if (!AbstractChallenge.ignoreGameMode(event.getNewGameMode()) && AbstractChallenge.ignoreGameMode(event.getPlayer().getGameMode())) {
			execute = true;
			isIgnored = false;
		}

		if (execute) {
			PlayerIgnoreStatusChangeEvent statusEvent = new PlayerIgnoreStatusChangeEvent(event.getPlayer(), isIgnored);
			Bukkit.getPluginManager().callEvent(statusEvent);
		}
	}

}