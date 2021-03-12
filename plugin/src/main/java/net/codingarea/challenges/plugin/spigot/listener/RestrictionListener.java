package net.codingarea.challenges.plugin.spigot.listener;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.misc.ParticleUtils;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.util.BoundingBox;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class RestrictionListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamage(@Nonnull EntityDamageEvent event) {
		if (ChallengeAPI.isStarted()) return;
		Entity entity = event.getEntity();
		if (event.getCause() != DamageCause.VOID && (entity instanceof Player || event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.PROJECTILE)) {
			entity.setFireTicks(entity instanceof Player ? 0 : entity.getFireTicks());
			event.setCancelled(true);
			BoundingBox box = entity.getBoundingBox();
			ParticleUtils.spawnUpGoingParticleCircle(Challenges.getInstance(), entity.getLocation(), Particle.SPELL_INSTANT, 17, box.getWidthX(), 0.25);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDeath(@Nonnull EntityDeathEvent event) {
		if (ChallengeAPI.isStarted()) return;
		event.getDrops().clear();
		event.setDroppedExp(0);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		if (ChallengeAPI.isPaused() && event.getPlayer().getGameMode() != GameMode.CREATIVE)
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlace(@Nonnull BlockPlaceEvent event) {
		if (ChallengeAPI.isPaused() && event.getPlayer().getGameMode() != GameMode.CREATIVE)
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onFoodLevelChange(@Nonnull FoodLevelChangeEvent event) {
		if (ChallengeAPI.isPaused())
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onItemPickup(@Nonnull EntityPickupItemEvent event) {
		if (ChallengeAPI.isStarted()) return;
		if (!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if (player.getGameMode() != GameMode.CREATIVE)
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onInteract(@Nonnull PlayerInteractEvent event) {
		if (ChallengeAPI.isPaused() && event.getPlayer().getGameMode() != GameMode.CREATIVE)
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onInteract(@Nonnull PlayerInteractAtEntityEvent event) {
		if (ChallengeAPI.isPaused() && event.getPlayer().getGameMode() != GameMode.CREATIVE)
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onInteract(@Nonnull PlayerInteractEntityEvent event) {
		if (ChallengeAPI.isPaused() && event.getPlayer().getGameMode() != GameMode.CREATIVE)
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onOffHandItemSwitch(@Nonnull PlayerSwapHandItemsEvent event) {
		if (ChallengeAPI.isPaused() && event.getPlayer().getGameMode() != GameMode.CREATIVE)
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onDamage(@Nonnull VehicleDamageEvent event) {
		if (ChallengeAPI.isStarted()) return;
		Entity entity = event.getVehicle();
		event.setCancelled(true);
		if (event.getAttacker() instanceof Player) {
			if (((Player) event.getAttacker()).getGameMode() == GameMode.CREATIVE)
				event.setCancelled(true);

			BoundingBox box = entity.getBoundingBox();
			ParticleUtils.spawnUpGoingParticleCircle(Challenges.getInstance(), entity.getLocation(), Particle.SPELL_INSTANT, 17, box.getWidthX(), 0.25);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onDamage(@Nonnull VehicleDestroyEvent event) {
		if (ChallengeAPI.isPaused() && !(event.getAttacker() instanceof Player && ((Player)event.getAttacker()).getGameMode() == GameMode.CREATIVE))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onWeatherChange(@Nonnull WeatherChangeEvent event) {
		if (ChallengeAPI.isPaused() && event.toWeatherState())
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onTarget(@Nonnull EntityTargetEvent event) {
		if (ChallengeAPI.isPaused() && event.getTarget() != null)
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onClick(@Nonnull InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		Player player = (Player) event.getWhoClicked();
		if (ChallengeAPI.isPaused() && player.getGameMode() != GameMode.CREATIVE)
			event.setCancelled(true);
	}

}
