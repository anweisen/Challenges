package net.codingarea.challengesplugin.listener;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.util.BoundingBox;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-06-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class TimerNotStartedListener implements Listener {

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (Challenges.timerIsStarted()) return;
		if (event.getCause() != DamageCause.VOID && (event.getEntity() instanceof Player || event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.PROJECTILE)) {
			event.setCancelled(true);
			BoundingBox box = event.getEntity().getBoundingBox();
			Utils.spawnUpGoingParticleCircle(event.getEntity().getLocation(), Particle.SPELL_INSTANT, Challenges.getInstance(), 0.25, 17, box.getWidthX());
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (Challenges.timerIsStarted()) return;
		event.getDrops().clear();
		event.setDroppedExp(0);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!Challenges.timerIsStarted() && event.getPlayer().getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!Challenges.timerIsStarted() && event.getPlayer().getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (!Challenges.timerIsStarted()) event.setCancelled(true);
	}

	@EventHandler
	public void onPickupItem(PlayerPickupItemEvent event) {
		if (!Challenges.timerIsStarted() && event.getPlayer().getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
	}

	@EventHandler
	public void onTarget(EntityTargetEvent event) {
		if (!Challenges.timerIsStarted() && event.getTarget() != null) event.setCancelled(true);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (!Challenges.timerIsStarted() && event.getPlayer().getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		if (!Challenges.timerIsStarted() && event.toWeatherState()) event.setCancelled(true);
	}

}